package com.gmt.inventorysystem.controller;

import com.gmt.inventorysystem.model.MovimientoInventario;
import com.gmt.inventorysystem.service.MovimientoInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoInventarioController {

    @Autowired
    private MovimientoInventarioService movimientoService;

    // Registrar entrada de productos
    @PostMapping("/entrada")
    public ResponseEntity<?> registrarEntrada(@RequestBody MovimientoRequest request) {
        try {
            MovimientoInventario movimiento = movimientoService.registrarEntrada(
                    request.getProductoReferencia(), // ← CAMBIADO
                    request.getCantidad(),
                    request.getMotivo(),
                    "SISTEMA"
            );
            return ResponseEntity.ok(movimiento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Registrar salida de productos
    @PostMapping("/salida")
    public ResponseEntity<?> registrarSalida(@RequestBody MovimientoRequest request) {
        try {
            MovimientoInventario movimiento = movimientoService.registrarSalida(
                    request.getProductoReferencia(), // ← CAMBIADO
                    request.getCantidad(),
                    request.getMotivo(),
                    "SISTEMA"
            );
            return ResponseEntity.ok(movimiento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Registrar ajuste de inventario
    @PostMapping("/ajuste")
    public ResponseEntity<?> registrarAjuste(@RequestBody AjusteRequest request) {
        try {
            MovimientoInventario movimiento = movimientoService.registrarAjuste(
                    request.getProductoReferencia(), // ← CAMBIADO
                    request.getNuevoStock(),
                    request.getMotivo(),
                    "SISTEMA"
            );
            return ResponseEntity.ok(movimiento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Obtener todos los movimientos
    @GetMapping
    public List<MovimientoInventario> obtenerTodosLosMovimientos() {
        return movimientoService.obtenerTodosLosMovimientos();
    }

    // Obtener movimientos por producto
    // Obtener movimientos por producto (por referencia)
    @GetMapping("/producto/{productoReferencia}")
    public List<MovimientoInventario> obtenerMovimientosPorProducto(@PathVariable String productoReferencia) {
        return movimientoService.obtenerMovimientosPorProducto(productoReferencia);
    }

    // Obtener movimientos por tipo
    @GetMapping("/tipo/{tipoMovimiento}")
    public List<MovimientoInventario> obtenerMovimientosPorTipo(@PathVariable String tipoMovimiento) {
        return movimientoService.obtenerMovimientosPorTipo(tipoMovimiento);
    }

    // Obtener movimientos manuales (sin documento compra)
    @GetMapping("/manuales")
    public List<MovimientoInventario> obtenerMovimientosManuales() {
        return movimientoService.obtenerMovimientosManuales();
    }

    // Obtener movimiento por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerMovimientoPorId(@PathVariable Long id) {
        Optional<MovimientoInventario> movimiento = movimientoService.obtenerMovimientoPorId(id);
        if (movimiento.isPresent()) {
            return ResponseEntity.ok(movimiento.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Clases internas para los request bodies - CORREGIDAS
    public static class MovimientoRequest {
        private String productoReferencia; // ← CAMBIADO
        private Integer cantidad;
        private String motivo;

        // Getters y Setters
        public String getProductoReferencia() { return productoReferencia; }
        public void setProductoReferencia(String productoReferencia) { this.productoReferencia = productoReferencia; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }
    }

    public static class AjusteRequest {
        private String productoReferencia; // ← CAMBIADO
        private Integer nuevoStock;
        private String motivo;

        // Getters y Setters
        public String getProductoReferencia() { return productoReferencia; }
        public void setProductoReferencia(String productoReferencia) { this.productoReferencia = productoReferencia; }
        public Integer getNuevoStock() { return nuevoStock; }
        public void setNuevoStock(Integer nuevoStock) { this.nuevoStock = nuevoStock; }
        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }
    }
}