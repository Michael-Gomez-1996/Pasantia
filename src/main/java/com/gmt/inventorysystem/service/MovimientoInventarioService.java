package com.gmt.inventorysystem.service;

import com.gmt.inventorysystem.controller.MovimientoInventarioController;
import com.gmt.inventorysystem.dto.EntradaManualRequest;
import com.gmt.inventorysystem.dto.MovimientoDTO;
import com.gmt.inventorysystem.dto.ProcesamientoEntradaResponse;
import com.gmt.inventorysystem.dto.DevolucionAveriaRequest;
import com.gmt.inventorysystem.model.*;
import com.gmt.inventorysystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class MovimientoInventarioService {

    @Autowired
    private MovimientoInventarioRepository movimientoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private DocumentoCompraRepository documentoCompraRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ConductorRepository conductorRepository;

    @Autowired
    private AveriaRepository averiaRepository;

    // Registrar entrada de productos
    public MovimientoInventario registrarEntrada(String productoReferencia, Integer cantidad, String motivo, String usuario) {
        Optional<Producto> productoOpt = productoRepository.findByReferencia(productoReferencia);
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("Producto con referencia " + productoReferencia + " no encontrado");
        }

        Producto producto = productoOpt.get();
        Integer stockAnterior = producto.getCantidadStock();
        Integer stockNuevo = stockAnterior + cantidad;

        // Actualizar stock del producto
        producto.setCantidadStock(stockNuevo);
        productoRepository.save(producto);

        // Crear movimiento
        MovimientoInventario movimiento = new MovimientoInventario(
                producto, "ENTRADA", cantidad, motivo, usuario, stockAnterior, stockNuevo
        );

        return movimientoRepository.save(movimiento);
    }

    // Registrar salida de productos
    public MovimientoInventario registrarSalida(String productoReferencia, Integer cantidad, String motivo, String usuario) {
        Optional<Producto> productoOpt = productoRepository.findByReferencia(productoReferencia);
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("Producto con referencia " + productoReferencia + " no encontrado");
        }

        Producto producto = productoOpt.get();

        // Validar stock suficiente
        if (producto.getCantidadStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Stock actual: " + producto.getCantidadStock() + ", solicitado: " + cantidad);
        }

        Integer stockAnterior = producto.getCantidadStock();
        Integer stockNuevo = stockAnterior - cantidad;

        // Actualizar stock del producto
        producto.setCantidadStock(stockNuevo);
        productoRepository.save(producto);

        // Crear movimiento
        MovimientoInventario movimiento = new MovimientoInventario(
                producto, "SALIDA", cantidad, motivo, usuario, stockAnterior, stockNuevo
        );

        return movimientoRepository.save(movimiento);
    }

    // Registrar salida manual
    @Transactional
    public ProcesamientoEntradaResponse registrarSalidaManual(MovimientoInventarioController.SalidaManualRequest request) {
        // Validaciones básicas
        if (request.getProductos() == null || request.getProductos().isEmpty()) {
            throw new RuntimeException("Debe incluir al menos un producto");
        }

        if (request.getNumeroDocumento() == null || request.getNumeroDocumento().trim().isEmpty()) {
            throw new RuntimeException("El número de documento es requerido");
        }

        if (request.getDestino() == null || request.getDestino().trim().isEmpty()) {
            throw new RuntimeException("El destino es requerido");
        }

        int productosProcesados = 0;
        int movimientosGenerados = 0;
        List<String> errores = new ArrayList<>();

        // Crear un motivo único para todos los productos (para agrupar en reporte)
        String motivoUnico = "SAL-" + request.getNumeroDocumento() + " - " + request.getDestino();
        if (request.getObservaciones() != null && !request.getObservaciones().trim().isEmpty()) {
            motivoUnico += " - " + request.getObservaciones();
        }

        // Procesar cada producto
        for (MovimientoInventarioController.SalidaManualRequest.ProductoSalida productoSolicitud : request.getProductos()) {
            try {
                // Validar que el producto existe
                Optional<Producto> productoOpt = productoRepository.findByReferencia(productoSolicitud.getReferencia());
                if (productoOpt.isEmpty()) {
                    errores.add("Producto con referencia " + productoSolicitud.getReferencia() + " no encontrado");
                    continue;
                }

                Producto producto = productoOpt.get();

                // Validar stock suficiente
                if (producto.getCantidadStock() < productoSolicitud.getCantidad()) {
                    errores.add("Stock insuficiente para " + productoSolicitud.getReferencia() +
                            ". Stock actual: " + producto.getCantidadStock() +
                            ", solicitado: " + productoSolicitud.getCantidad());
                    continue;
                }

                // USAR MOTIVO ÚNICO PARA TODOS LOS PRODUCTOS
                String motivoProducto = motivoUnico;
                if (productoSolicitud.getPeso() != null && productoSolicitud.getPeso() > 0) {
                    motivoProducto += " - Ref: " + productoSolicitud.getReferencia() + " - Peso: " + productoSolicitud.getPeso() + " kg";
                } else {
                    motivoProducto += " - Ref: " + productoSolicitud.getReferencia();
                }

                // Registrar movimiento de salida
                MovimientoInventario movimiento = registrarSalida(
                        productoSolicitud.getReferencia(),
                        productoSolicitud.getCantidad(),
                        motivoProducto, // Mismo patrón de motivo para agrupar
                        "SISTEMA"
                );

                if (movimiento != null) {
                    productosProcesados++;
                    movimientosGenerados++;
                }

            } catch (Exception e) {
                errores.add("Error procesando " + productoSolicitud.getReferencia() + ": " + e.getMessage());
            }
        }

        // Verificar si hubo errores
        if (!errores.isEmpty()) {
            if (productosProcesados == 0) {
                // Todos fallaron
                throw new RuntimeException("Error procesando salida: " + String.join(", ", errores));
            } else {
                // Algunos fallaron, otros tuvieron éxito
                return new ProcesamientoEntradaResponse(
                        true,
                        "Salida parcialmente procesada. " + productosProcesados + " productos exitosos, " +
                                errores.size() + " con errores: " + String.join("; ", errores),
                        request.getNumeroDocumento(), // Pasar el número real
                        productosProcesados,
                        movimientosGenerados
                );
            }
        }

        // Éxito completo
        return new ProcesamientoEntradaResponse(
                true,
                "Salida manual procesada correctamente",
                request.getNumeroDocumento(), // Pasar el número real
                productosProcesados,
                movimientosGenerados
        );
    }

    // Registrar ajuste de inventario
    public MovimientoInventario registrarAjuste(String productoReferencia, Integer nuevoStock, String motivo, String usuario) {
        Optional<Producto> productoOpt = productoRepository.findByReferencia(productoReferencia);
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("Producto con referencia " + productoReferencia + " no encontrado");
        }

        Producto producto = productoOpt.get();
        Integer stockAnterior = producto.getCantidadStock();

        // Validar que el nuevo stock no sea negativo
        if (nuevoStock < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }

        // Actualizar stock del producto
        producto.setCantidadStock(nuevoStock);
        productoRepository.save(producto);

        // Calcular la diferencia (para el campo cantidad)
        Integer diferencia = nuevoStock - stockAnterior;
        String tipoMovimiento = diferencia >= 0 ? "AJUSTE_POSITIVO" : "AJUSTE_NEGATIVO";

        // Crear movimiento
        MovimientoInventario movimiento = new MovimientoInventario(
                producto, tipoMovimiento, Math.abs(diferencia), motivo, usuario, stockAnterior, nuevoStock
        );

        return movimientoRepository.save(movimiento);
    }

    // ✅ NUEVO MÉTODO: Devolución de averías al ingenio
    @Transactional
    public ProcesamientoEntradaResponse registrarDevolucionAverias(DevolucionAveriaRequest request) {
        // Validaciones básicas
        if (request.getProductos() == null || request.getProductos().isEmpty()) {
            throw new RuntimeException("Debe incluir al menos un producto con averías");
        }

        if (request.getNumeroRemision() == null || request.getNumeroRemision().trim().isEmpty()) {
            throw new RuntimeException("El número de remisión es requerido");
        }

        if (request.getIngenioDestino() == null || request.getIngenioDestino().trim().isEmpty()) {
            throw new RuntimeException("El ingenio destino es requerido");
        }

        int productosProcesados = 0;
        int movimientosGenerados = 0;
        List<String> errores = new ArrayList<>();

        // Motivo único para agrupar en reportes
        String motivo = "DEV_AVERIA-" + request.getNumeroRemision() + " - " + request.getIngenioDestino();
        if (request.getObservaciones() != null && !request.getObservaciones().trim().isEmpty()) {
            motivo += " - " + request.getObservaciones();
        }

        // Procesar cada producto con averías
        for (DevolucionAveriaRequest.ProductoAveria productoSolicitud : request.getProductos()) {
            try {
                // Validar que el producto existe
                Optional<Producto> productoOpt = productoRepository.findByReferencia(productoSolicitud.getReferencia());
                if (productoOpt.isEmpty()) {
                    errores.add("Producto con referencia " + productoSolicitud.getReferencia() + " no encontrado");
                    continue;
                }

                Producto producto = productoOpt.get();

                // ✅ Validar que hay suficiente stock AVERIADO
                if (producto.getCantidadAveriada() < productoSolicitud.getCantidad()) {
                    errores.add("Stock averiado insuficiente para " + productoSolicitud.getReferencia() +
                            ". Stock averiado actual: " + producto.getCantidadAveriada() +
                            ", solicitado: " + productoSolicitud.getCantidad());
                    continue;
                }

                // Motivo detallado
                String motivoProducto = motivo + " - Ref: " + productoSolicitud.getReferencia();

                // ✅ Registrar movimiento de SALIDA de AVERÍAS
                MovimientoInventario movimiento = new MovimientoInventario();
                movimiento.setProducto(producto);
                movimiento.setTipoMovimiento("SALIDA_AVERIAS");
                movimiento.setCantidad(productoSolicitud.getCantidad());
                movimiento.setMotivo(motivoProducto);
                movimiento.setUsuario("SISTEMA");

                // Stock anterior y nuevo (solo afecta stock averiado)
                Integer stockAveriadoAnterior = producto.getCantidadAveriada();
                movimiento.setStockAnterior(stockAveriadoAnterior);

                // ✅ Descontar SOLO del stock averiado
                producto.setCantidadAveriada(stockAveriadoAnterior - productoSolicitud.getCantidad());
                productoRepository.save(producto);

                movimiento.setStockNuevo(producto.getCantidadAveriada());
                movimiento.setFechaMovimiento(LocalDateTime.now());

                // Guardar movimiento
                movimientoRepository.save(movimiento);

                productosProcesados++;
                movimientosGenerados++;

                // ✅ Opcional: Marcar avería específica como devuelta
                if (productoSolicitud.getAveriaId() != null) {
                    Optional<Averia> averiaOpt = averiaRepository.findById(productoSolicitud.getAveriaId());
                    if (averiaOpt.isPresent()) {
                        Averia averia = averiaOpt.get();
                        averia.setObservaciones(averia.getObservaciones() + " - DEVUELTA AL INGENIO: " +
                                request.getNumeroRemision() + " (" + productoSolicitud.getCantidad() + " unidades)");
                        averiaRepository.save(averia);
                    }
                }

            } catch (Exception e) {
                errores.add("Error procesando " + productoSolicitud.getReferencia() + ": " + e.getMessage());
            }
        }

        // Manejar errores
        if (!errores.isEmpty()) {
            if (productosProcesados == 0) {
                throw new RuntimeException("Error procesando devolución: " + String.join(", ", errores));
            } else {
                return new ProcesamientoEntradaResponse(
                        true,
                        "Devolución parcialmente procesada. " + productosProcesados + " productos exitosos, " +
                                errores.size() + " con errores: " + String.join("; ", errores),
                        request.getNumeroRemision(),
                        productosProcesados,
                        movimientosGenerados
                );
            }
        }

        // Éxito completo
        return new ProcesamientoEntradaResponse(
                true,
                "Devolución de averías al ingenio procesada correctamente",
                request.getNumeroRemision(),
                productosProcesados,
                movimientosGenerados
        );
    }

    // ✅ NUEVO MÉTODO: Obtener productos con averías disponibles
    public List<Map<String, Object>> obtenerProductosConAverias() {
        List<Producto> productos = productoRepository.findAll();

        return productos.stream()
                .filter(p -> p.getCantidadAveriada() > 0)
                .map(p -> {
                    Map<String, Object> productoInfo = new HashMap<>();
                    productoInfo.put("referencia", p.getReferencia());
                    productoInfo.put("nombre", p.getNombre());
                    productoInfo.put("stockAveriado", p.getCantidadAveriada());
                    productoInfo.put("categoria", p.getCategoria());
                    productoInfo.put("proveedor", p.getProveedor());
                    productoInfo.put("lote", p.getLote());
                    productoInfo.put("descripcion", p.getDescripcion());

                    // ✅ Información adicional útil para el frontend
                    productoInfo.put("stockBueno", p.getCantidadStock());
                    productoInfo.put("inventarioTotal", p.getInventarioTotal());

                    return productoInfo;
                })
                .collect(Collectors.toList());
    }

    // Validar referencia de producto
    public boolean validarReferenciaProducto(String referencia) {
        return productoRepository.existsByReferencia(referencia);
    }

    // Registrar entrada manual
    public ProcesamientoEntradaResponse registrarEntradaManual(EntradaManualRequest request) {
        // Validaciones básicas
        if (request.getProductos() == null || request.getProductos().isEmpty()) {
            throw new RuntimeException("Debe incluir al menos un producto");
        }

        // Verificar si ya existe un documento con este número de remisión
        if (documentoCompraRepository.existsByNumeroRemision(request.getNumeroRemision())) {
            throw new RuntimeException("Ya existe un documento con el número de remisión: " + request.getNumeroRemision());
        }

        // Crear cliente temporal para entrada manual
        Cliente clienteTemporal = new Cliente("TEMP_ENTRADA", "ENTRADA MANUAL", "ENTRADA MANUAL");
        clienteRepository.save(clienteTemporal);

        // Crear conductor temporal
        Conductor conductorTemporal = new Conductor("TEMP_ENTRADA", "CONDUCTOR TEMPORAL", "TRANSPORTE TEMPORAL");
        conductorRepository.save(conductorTemporal);

        // Crear documento de compra para entrada manual
        DocumentoCompra documento = new DocumentoCompra(
                request.getNumeroRemision(),
                request.getOrigenIngenio(),
                clienteTemporal,
                conductorTemporal,
                LocalDateTime.now(),
                request.getPlacaVehiculo()
        );

        documentoCompraRepository.save(documento);

        int productosProcesados = 0;
        int movimientosGenerados = 0;

        // Procesar cada producto
        for (EntradaManualRequest.ProductoEntrada productoSolicitud : request.getProductos()) {
            try {
                // Validar que el producto existe
                Optional<Producto> productoOpt = productoRepository.findByReferencia(productoSolicitud.getReferencia());
                if (productoOpt.isEmpty()) {
                    throw new RuntimeException("Producto con referencia " + productoSolicitud.getReferencia() + " no encontrado");
                }

                Producto producto = productoOpt.get();

                // Crear motivo detallado
                String motivo = "Entrada manual - " + request.getNumeroRemision();
                if (request.getObservaciones() != null && !request.getObservaciones().trim().isEmpty()) {
                    motivo += " - " + request.getObservaciones();
                }

                // Registrar movimiento de entrada
                MovimientoInventario movimiento = registrarEntrada(
                        productoSolicitud.getReferencia(),
                        productoSolicitud.getCantidad(),
                        motivo,
                        "SISTEMA"
                );

                // Asociar movimiento con el documento
                movimiento.setDocumentoCompra(documento);
                movimientoRepository.save(movimiento);

                productosProcesados++;
                movimientosGenerados++;

            } catch (Exception e) {
                throw new RuntimeException("Error procesando producto " + productoSolicitud.getReferencia() + ": " + e.getMessage());
            }
        }

        return new ProcesamientoEntradaResponse(
                true,
                "Entrada manual procesada correctamente",
                request.getNumeroRemision(),
                productosProcesados,
                movimientosGenerados
        );
    }

    // Obtener todos los movimientos como DTOs
    public List<MovimientoDTO> obtenerTodosLosMovimientosDTO() {
        List<MovimientoInventario> movimientos = movimientoRepository.findAllWithDocumentoCompraAndProducto();
        return movimientos.stream()
                .map(MovimientoDTO::new)
                .collect(Collectors.toList());
    }

    // Obtener movimientos por producto como DTOs
    public List<MovimientoDTO> obtenerMovimientosPorProductoDTO(String productoReferencia) {
        List<MovimientoInventario> movimientos = movimientoRepository.findByProductoReferenciaOrderByFechaMovimientoDesc(productoReferencia);
        return movimientos.stream()
                .map(MovimientoDTO::new)
                .collect(Collectors.toList());
    }

    // Obtener movimientos por tipo
    public List<MovimientoInventario> obtenerMovimientosPorTipo(String tipoMovimiento) {
        return movimientoRepository.findByTipoMovimientoOrderByFechaMovimientoDesc(tipoMovimiento);
    }

    // Obtener movimientos manuales (sin documento compra)
    public List<MovimientoInventario> obtenerMovimientosManuales() {
        return movimientoRepository.findByDocumentoCompraIsNullOrderByFechaMovimientoDesc();
    }

    // Obtener movimiento por ID
    public Optional<MovimientoInventario> obtenerMovimientoPorId(Long id) {
        return movimientoRepository.findByIdWithDocumentoCompra(id);
    }
}