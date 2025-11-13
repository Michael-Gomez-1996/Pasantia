package com.gmt.inventorysystem.service;

import com.gmt.inventorysystem.dto.KardexDiarioDTO;
import com.gmt.inventorysystem.model.*;
import com.gmt.inventorysystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KardexService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoRepository;

    @Autowired
    private AveriaRepository averiaRepository;

    public List<List<KardexDiarioDTO>> generarKardexDiario(LocalDate fechaInicio, LocalDate fechaFin) {
        List<List<KardexDiarioDTO>> kardexCompleto = new ArrayList<>();

        // 1. Obtener todos los productos
        List<Producto> productos = productoRepository.findAll();

        // 2. Generar lista de fechas en el rango
        List<LocalDate> fechas = generarRangoFechas(fechaInicio, fechaFin);

        // 3. Para cada producto, generar datos día por día
        for (Producto producto : productos) {
            List<KardexDiarioDTO> kardexProducto = generarKardexParaProducto(producto, fechas, fechaInicio);
            kardexCompleto.add(kardexProducto);
        }

        return kardexCompleto;
    }

    private List<LocalDate> generarRangoFechas(LocalDate inicio, LocalDate fin) {
        List<LocalDate> fechas = new ArrayList<>();
        LocalDate fecha = inicio;

        while (!fecha.isAfter(fin)) {
            fechas.add(fecha);
            fecha = fecha.plusDays(1);
        }

        return fechas;
    }

    private List<KardexDiarioDTO> generarKardexParaProducto(Producto producto, List<LocalDate> fechas, LocalDate fechaInicio) {
        List<KardexDiarioDTO> kardexProducto = new ArrayList<>();

        Integer existenciaAcumulada = producto.getCantidadStock() + producto.getCantidadAveriada();
        Integer movimientosPosteriores = calcularMovimientosPosteriores(producto.getReferencia(), fechaInicio);
        existenciaAcumulada = existenciaAcumulada - movimientosPosteriores;

        for (LocalDate fecha : fechas) {
            KardexDiarioDTO kardexDia = new KardexDiarioDTO(
                    producto.getReferencia(),
                    producto.getNombre(),
                    producto.getPacasPorEstiba()
            );

            kardexDia.setFecha(fecha);

            // Obtener movimientos del día
            Integer entradasDia = obtenerEntradasDia(producto.getReferencia(), fecha);
            Integer salidasDia = obtenerSalidasDia(producto.getReferencia(), fecha);
            Integer devolucionesDia = obtenerDevolucionesDia(producto.getReferencia(), fecha);

            // ❌ QUITAR: Integer averiasEntradaOperacionDia = obtenerAveriasEntradaOperacionDia(...);

            kardexDia.setEntradas(entradasDia);
            kardexDia.setSalida(salidasDia);
            kardexDia.setDevolucion(devolucionesDia); // ✅ Solo averías devolución

            // ✅ CÁLCULO CORREGIDO: Solo devoluciones afectan el stock
            existenciaAcumulada = existenciaAcumulada + entradasDia - salidasDia + devolucionesDia;

            kardexDia.setExistencias(existenciaAcumulada);

            // Calcular estibas (se mantiene igual)
            if (producto.getPacasPorEstiba() != null && producto.getPacasPorEstiba() > 0) {
                if (existenciaAcumulada > 0) {
                    double estibasDecimal = (double) existenciaAcumulada / producto.getPacasPorEstiba();
                    kardexDia.setEstibas((int) Math.ceil(estibasDecimal));
                } else {
                    kardexDia.setEstibas(0);
                }
            } else {
                kardexDia.setEstibas(0);
            }

            kardexProducto.add(kardexDia);
        }

        return kardexProducto;
    }

    // ✅ NUEVO MÉTODO: Calcular movimientos que ocurrieron DESPUÉS de la fecha base
    private Integer calcularMovimientosPosteriores(String referencia, LocalDate fechaBase) {
        LocalDateTime fechaBaseInicio = fechaBase.atStartOfDay();

        List<MovimientoInventario> movimientosPosteriores = movimientoRepository
                .findByProductoReferenciaOrderByFechaMovimientoDesc(referencia)
                .stream()
                .filter(m -> !m.getFechaMovimiento().isBefore(fechaBaseInicio))
                .collect(Collectors.toList());

        List<Averia> averiasPosteriores = averiaRepository
                .findByProductoReferenciaOrderByFechaRegistroDesc(referencia)
                .stream()
                .filter(a -> !a.getFechaRegistro().isBefore(fechaBaseInicio))
                .collect(Collectors.toList());

        Integer totalMovimientos = 0;

        // Procesar movimientos posteriores
        for (MovimientoInventario movimiento : movimientosPosteriores) {
            switch (movimiento.getTipoMovimiento()) {
                case "ENTRADA":
                    totalMovimientos += movimiento.getCantidad();
                    break;
                case "SALIDA":
                case "SALIDA_AVERIAS":
                    totalMovimientos -= movimiento.getCantidad();
                    break;
            }
        }

        // Procesar averías posteriores
        for (Averia averia : averiasPosteriores) {
            switch (averia.getTipoAveria()) {
                case "ENTRADA":
                case "OPERACION":
                    // ❌ Estas NO deberían sumarse
                    break;
                case "DEVOLUCION":
                    totalMovimientos += averia.getCantidad(); // ✅ Esta SÍ
                    break;
            }
        }

        return totalMovimientos;
    }

    private Integer obtenerEntradasDia(String referencia, LocalDate fecha) {
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(23, 59, 59);

        List<MovimientoInventario> movimientos = movimientoRepository
                .findByProductoReferenciaOrderByFechaMovimientoDesc(referencia);

        return movimientos.stream()
                .filter(m -> m.getTipoMovimiento().equals("ENTRADA"))
                .filter(m -> !m.getFechaMovimiento().isBefore(inicioDia) && !m.getFechaMovimiento().isAfter(finDia))
                .mapToInt(MovimientoInventario::getCantidad)
                .sum();
    }

    private Integer obtenerSalidasDia(String referencia, LocalDate fecha) {
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(23, 59, 59);

        List<MovimientoInventario> movimientos = movimientoRepository
                .findByProductoReferenciaOrderByFechaMovimientoDesc(referencia);

        return movimientos.stream()
                .filter(m -> "SALIDA".equals(m.getTipoMovimiento()) || "SALIDA_AVERIAS".equals(m.getTipoMovimiento()))
                .filter(m -> !m.getFechaMovimiento().isBefore(inicioDia) && !m.getFechaMovimiento().isAfter(finDia))
                .mapToInt(MovimientoInventario::getCantidad)
                .sum();
    }

    private Integer obtenerDevolucionesDia(String referencia, LocalDate fecha) {
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(23, 59, 59);

        List<Averia> averias = averiaRepository
                .findByProductoReferenciaOrderByFechaRegistroDesc(referencia);

        return averias.stream()
                .filter(a -> a.getTipoAveria().equals("DEVOLUCION"))
                .filter(a -> !a.getFechaRegistro().isBefore(inicioDia) && !a.getFechaRegistro().isAfter(finDia))
                .mapToInt(Averia::getCantidad)
                .sum();
    }

    private Integer obtenerAveriasEntradaOperacionDia(String referencia, LocalDate fecha) {
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(23, 59, 59);

        List<Averia> averias = averiaRepository
                .findByProductoReferenciaOrderByFechaRegistroDesc(referencia);

        return averias.stream()
                .filter(a -> a.getTipoAveria().equals("ENTRADA") || a.getTipoAveria().equals("OPERACION"))
                .filter(a -> !a.getFechaRegistro().isBefore(inicioDia) && !a.getFechaRegistro().isAfter(finDia))
                .mapToInt(Averia::getCantidad)
                .sum();
    }

    // Método para obtener las fechas del rango
    public List<LocalDate> obtenerFechasRango(LocalDate fechaInicio, LocalDate fechaFin) {
        return generarRangoFechas(fechaInicio, fechaFin);
    }
}