package com.gmt.inventorysystem.service;

import com.gmt.inventorysystem.dto.ReporteInventarioDTO;
import com.gmt.inventorysystem.dto.ReporteMovimientoDTO;
import com.gmt.inventorysystem.model.*;
import com.gmt.inventorysystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private MovimientoInventarioRepository movimientoRepository;

    @Autowired
    private DocumentoCompraRepository documentoCompraRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<ReporteMovimientoDTO> generarReporteMovimientosExcel(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<ReporteMovimientoDTO> reporte = new ArrayList<>();

        List<DocumentoCompra> documentos;
        if (fechaInicio != null && fechaFin != null) {
            documentos = documentoCompraRepository.findByFechaFacturacionBetween(fechaInicio, fechaFin);
        } else {
            documentos = documentoCompraRepository.findAllWithClienteAndConductor();
        }

        for (DocumentoCompra documento : documentos) {
            if (documento.getMovimientos() != null && !documento.getMovimientos().isEmpty()) {
                String tipoMovimiento = documento.getNumeroFactura() != null &&
                        !documento.getNumeroFactura().startsWith("ENTRADA_") ? "SALIDA" : "ENTRADA";

                Integer totalUnidades = documento.getMovimientos().stream()
                        .mapToInt(MovimientoInventario::getCantidad)
                        .sum();

                Double pesoTotalKilos = documento.getPesoTotal() != null ? documento.getPesoTotal() : 0.0;
                Double valorTotal = documento.getValorTotal() != null ? documento.getValorTotal() : 0.0;

                String conductorNombre = "N/A";
                String conductorCedula = "N/A";
                if (documento.getConductor() != null) {
                    conductorNombre = documento.getConductor().getNombre();
                    conductorCedula = documento.getConductor().getCedula();
                }

                String origen = documento.getOrigenIngenio() != null ? documento.getOrigenIngenio() : "N/A";
                String destino = documento.getCliente() != null ? documento.getCliente().getNombre() : "N/A";

                if (documento.getNumeroFactura() == null && documento.getClienteNombre().equals("ENTRADA MANUAL")) {
                    tipoMovimiento = "ENTRADA";
                    destino = "ALMACÉN";
                }

                ReporteMovimientoDTO dto = new ReporteMovimientoDTO(
                        documento.getFechaFacturacion(),
                        documento.getNumeroRemision(),
                        documento.getNumeroFactura(),
                        tipoMovimiento,
                        origen,
                        destino,
                        totalUnidades,
                        valorTotal,
                        pesoTotalKilos,
                        conductorNombre,
                        conductorCedula
                );
                reporte.add(dto);
            }
        }

        List<MovimientoInventario> movimientosManuales;
        if (fechaInicio != null && fechaFin != null) {
            List<MovimientoInventario> todosMovimientos = movimientoRepository.findAllWithDocumentoCompraAndProducto();
            movimientosManuales = todosMovimientos.stream()
                    .filter(m -> m.getDocumentoCompra() == null)
                    .filter(m -> m.getFechaMovimiento() != null &&
                            !m.getFechaMovimiento().isBefore(fechaInicio) &&
                            !m.getFechaMovimiento().isAfter(fechaFin))
                    .collect(Collectors.toList());
        } else {
            movimientosManuales = movimientoRepository.findByDocumentoCompraIsNullOrderByFechaMovimientoDesc();
        }

        // Procesar salidas manuales agrupadas
        Map<String, List<MovimientoInventario>> salidasManualesAgrupadas = movimientosManuales.stream()
                .filter(m -> "SALIDA".equals(m.getTipoMovimiento()))
                .collect(Collectors.groupingBy(m -> {
                    if (m.getMotivo() != null && m.getMotivo().startsWith("SAL-")) {
                        String[] partes = m.getMotivo().split(" - ");
                        if (partes.length > 0) {
                            return partes[0];
                        }
                    }
                    return "SIN_NUMERO";
                }));

        for (Map.Entry<String, List<MovimientoInventario>> entry : salidasManualesAgrupadas.entrySet()) {
            String numeroDocumento = entry.getKey();
            List<MovimientoInventario> movimientosGrupo = entry.getValue();

            if (!movimientosGrupo.isEmpty()) {
                MovimientoInventario primerMovimiento = movimientosGrupo.get(0);

                Integer totalUnidades = movimientosGrupo.stream()
                        .mapToInt(MovimientoInventario::getCantidad)
                        .sum();

                Double pesoTotal = movimientosGrupo.stream()
                        .mapToDouble(m -> calcularPesoDeProducto(m.getProducto(), m.getCantidad()))
                        .sum();

                String destino = "CLIENTE";
                if (primerMovimiento.getMotivo() != null && primerMovimiento.getMotivo().contains(" - ")) {
                    String[] partes = primerMovimiento.getMotivo().split(" - ");
                    if (partes.length > 1) {
                        destino = partes[1];
                    }
                }

                String numeroRemision = numeroDocumento;
                if (numeroDocumento.startsWith("SAL-")) {
                    numeroRemision = numeroDocumento.substring(4);
                }

                ReporteMovimientoDTO dto = new ReporteMovimientoDTO(
                        primerMovimiento.getFechaMovimiento(),
                        numeroRemision,
                        "SALIDA MANUAL",
                        "SALIDA",
                        "ALMACÉN",
                        destino,
                        totalUnidades,
                        0.0,
                        pesoTotal,
                        "SISTEMA",
                        "SALIDA MANUAL"
                );
                reporte.add(dto);
            }
        }

        // Procesar entradas manuales
        List<MovimientoInventario> entradasManuales = movimientosManuales.stream()
                .filter(m -> "ENTRADA".equals(m.getTipoMovimiento()))
                .collect(Collectors.toList());

        for (MovimientoInventario movimiento : entradasManuales) {
            Double pesoTotal = calcularPesoDeProducto(movimiento.getProducto(), movimiento.getCantidad());

            ReporteMovimientoDTO dto = new ReporteMovimientoDTO(
                    movimiento.getFechaMovimiento(),
                    "ENTRADA MANUAL",
                    "ENTRADA MANUAL",
                    "ENTRADA",
                    "PROVEEDOR",
                    "ALMACÉN",
                    movimiento.getCantidad(),
                    0.0,
                    pesoTotal,
                    "SISTEMA",
                    "ENTRADA MANUAL"
            );
            reporte.add(dto);
        }

        // ✅ NUEVO: Procesar devoluciones de averías al ingenio
        List<MovimientoInventario> devolucionesAverias;
        if (fechaInicio != null && fechaFin != null) {
            List<MovimientoInventario> todosMovimientos = movimientoRepository.findAllWithDocumentoCompraAndProducto();
            devolucionesAverias = todosMovimientos.stream()
                    .filter(m -> "SALIDA_AVERIAS".equals(m.getTipoMovimiento()))
                    .filter(m -> m.getFechaMovimiento() != null &&
                            !m.getFechaMovimiento().isBefore(fechaInicio) &&
                            !m.getFechaMovimiento().isAfter(fechaFin))
                    .collect(Collectors.toList());
        } else {
            devolucionesAverias = movimientoRepository.findAllWithDocumentoCompraAndProducto().stream()
                    .filter(m -> "SALIDA_AVERIAS".equals(m.getTipoMovimiento()))
                    .collect(Collectors.toList());
        }

        // Agrupar devoluciones de averías por número de remisión (motivo)
        Map<String, List<MovimientoInventario>> devolucionesAgrupadas = devolucionesAverias.stream()
                .collect(Collectors.groupingBy(m -> {
                    if (m.getMotivo() != null && m.getMotivo().startsWith("DEV_AVERIA-")) {
                        String[] partes = m.getMotivo().split(" - ");
                        if (partes.length > 0) {
                            return partes[0]; // DEV_AVERIA-{numeroRemision}
                        }
                    }
                    return "SIN_NUMERO";
                }));

        for (Map.Entry<String, List<MovimientoInventario>> entry : devolucionesAgrupadas.entrySet()) {
            String numeroDocumento = entry.getKey();
            List<MovimientoInventario> movimientosGrupo = entry.getValue();

            if (!movimientosGrupo.isEmpty()) {
                MovimientoInventario primerMovimiento = movimientosGrupo.get(0);

                Integer totalUnidades = movimientosGrupo.stream()
                        .mapToInt(MovimientoInventario::getCantidad)
                        .sum();

                Double pesoTotal = movimientosGrupo.stream()
                        .mapToDouble(m -> calcularPesoDeProducto(m.getProducto(), m.getCantidad()))
                        .sum();

                String ingenioDestino = "INGENIO";
                String numeroRemision = "SIN_REMISION";

                // Extraer información del motivo
                if (primerMovimiento.getMotivo() != null) {
                    String[] partes = primerMovimiento.getMotivo().split(" - ");
                    if (partes.length > 1 && partes[0].startsWith("DEV_AVERIA-")) {
                        numeroRemision = partes[0].substring(11); // Extraer número después de "DEV_AVERIA-"
                    }
                    if (partes.length > 1) {
                        ingenioDestino = partes[1]; // El destino está después del número de remisión
                    }
                }

                ReporteMovimientoDTO dto = new ReporteMovimientoDTO(
                        primerMovimiento.getFechaMovimiento(),
                        numeroRemision,
                        "DEVOLUCIÓN AVERÍAS",
                        "SALIDA_AVERIAS",
                        "ALMACÉN",
                        ingenioDestino,
                        totalUnidades,
                        0.0,
                        pesoTotal,
                        "SISTEMA",
                        "DEVOLUCIÓN AVERÍAS"
                );
                reporte.add(dto);
            }
        }

        reporte.sort((d1, d2) -> d2.getFecha().compareTo(d1.getFecha()));

        return reporte;
    }

    public List<ReporteInventarioDTO> generarReporteInventarioExcel() {
        List<Producto> productos = productoRepository.findAll();

        return productos.stream()
                .map(producto -> new ReporteInventarioDTO(
                        producto.getReferencia(),
                        producto.getNombre(),
                        producto.getLote(),
                        producto.getDescripcion(),
                        producto.getCategoria(),
                        producto.getCantidadStock(),
                        producto.getCantidadAveriada(),
                        producto.getStockMinimo(),
                        producto.getPesoPorPaca(),
                        producto.getUnidadesPorPaca(),
                        producto.getPacasPorEstiba(),
                        producto.getProveedor(),
                        producto.getUbicacion(),
                        producto.getFechaCreacion()
                ))
                .collect(Collectors.toList());
    }

    private Double calcularPesoDeProducto(Producto producto, Integer cantidad) {
        if (producto != null && producto.getPesoPorPaca() != null && cantidad != null) {
            return producto.getPesoPorPaca() * cantidad;
        }
        return 0.0;
    }
}