package com.gmt.inventorysystem.controller;

import com.gmt.inventorysystem.dto.EntradaManualRequest;
import com.gmt.inventorysystem.dto.MovimientoDTO;
import com.gmt.inventorysystem.dto.ProcesamientoEntradaResponse;
import com.gmt.inventorysystem.dto.DevolucionAveriaRequest;
import com.gmt.inventorysystem.model.MovimientoInventario;
import com.gmt.inventorysystem.service.MovimientoInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoInventarioController extends BaseController<MovimientoInventario, Long> {

    @Autowired
    private MovimientoInventarioService movimientoService;

    // Registrar entrada de productos
    @PostMapping("/entrada")
    public ResponseEntity<?> registrarEntrada(@RequestBody MovimientoRequest request) {
        try {
            MovimientoInventario movimiento = movimientoService.registrarEntrada(
                    request.getProductoReferencia(),
                    request.getCantidad(),
                    request.getMotivo(),
                    "SISTEMA"
            );
            return ResponseEntity.ok(movimiento);
        } catch (RuntimeException e) {
            return handleCreate(e);
        }
    }

    // Registrar salida de productos
    @PostMapping("/salida")
    public ResponseEntity<?> registrarSalida(@RequestBody MovimientoRequest request) {
        try {
            MovimientoInventario movimiento = movimientoService.registrarSalida(
                    request.getProductoReferencia(),
                    request.getCantidad(),
                    request.getMotivo(),
                    "SISTEMA"
            );
            return ResponseEntity.ok(movimiento);
        } catch (RuntimeException e) {
            return handleCreate(e);
        }
    }

    // Registrar salida manual
    @PostMapping("/salida-manual")
    public ResponseEntity<?> registrarSalidaManual(@RequestBody SalidaManualRequest request) {
        try {
            ProcesamientoEntradaResponse response = movimientoService.registrarSalidaManual(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ProcesamientoEntradaResponse(false, e.getMessage()));
        }
    }

    // Registrar ajuste de inventario
    @PostMapping("/ajuste")
    public ResponseEntity<?> registrarAjuste(@RequestBody AjusteRequest request) {
        try {
            MovimientoInventario movimiento = movimientoService.registrarAjuste(
                    request.getProductoReferencia(),
                    request.getNuevoStock(),
                    request.getMotivo(),
                    "SISTEMA"
            );
            return ResponseEntity.ok(movimiento);
        } catch (RuntimeException e) {
            return handleCreate(e);
        }
    }

    // ✅ NUEVO ENDPOINT: Devolución de averías al ingenio
    @PostMapping("/devolver-averias")
    public ResponseEntity<?> devolverAveriasAlIngenio(@RequestBody DevolucionAveriaRequest request) {
        try {
            ProcesamientoEntradaResponse response = movimientoService.registrarDevolucionAverias(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ProcesamientoEntradaResponse(false, e.getMessage()));
        }
    }

    // Validar referencia de producto
    @GetMapping("/validar-referencia/{referencia}")
    public ResponseEntity<?> validarReferencia(@PathVariable String referencia) {
        try {
            boolean existe = movimientoService.validarReferenciaProducto(referencia);
            if (existe) {
                return ResponseEntity.ok().body("{\"existe\": true, \"mensaje\": \"Referencia válida\"}");
            } else {
                return ResponseEntity.ok().body("{\"existe\": false, \"mensaje\": \"Referencia no encontrada\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"existe\": false, \"mensaje\": \"Error validando referencia\"}");
        }
    }

    // Registrar entrada manual
    @PostMapping("/entrada-manual")
    public ResponseEntity<?> registrarEntradaManual(@RequestBody EntradaManualRequest request) {
        try {
            ProcesamientoEntradaResponse response = movimientoService.registrarEntradaManual(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ProcesamientoEntradaResponse(false, e.getMessage()));
        }
    }

    // ✅ NUEVO ENDPOINT: Obtener productos con averías disponibles
    @GetMapping("/productos-con-averias")
    public ResponseEntity<?> obtenerProductosConAverias() {
        try {
            List<Map<String, Object>> productos = movimientoService.obtenerProductosConAverias();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error obteniendo productos con averías: " + e.getMessage());
        }
    }

    // Obtener todos los movimientos como DTOs
    @GetMapping
    public List<MovimientoDTO> obtenerTodosLosMovimientos() {
        return movimientoService.obtenerTodosLosMovimientosDTO();
    }

    // Obtener movimientos por producto como DTOs
    @GetMapping("/producto/{productoReferencia}")
    public List<MovimientoDTO> obtenerMovimientosPorProducto(@PathVariable String productoReferencia) {
        return movimientoService.obtenerMovimientosPorProductoDTO(productoReferencia);
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
        return handleFindById(movimiento, "Movimiento");
    }

    // Clases internas para los request bodies
    public static class MovimientoRequest {
        private String productoReferencia;
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
        private String productoReferencia;
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

    // Clase para salida manual
    public static class SalidaManualRequest {
        private String numeroDocumento;
        private String destino;
        private String placaVehiculo;
        private java.time.LocalDate fechaSalida;
        private String observaciones;
        private List<ProductoSalida> productos;

        // Constructores
        public SalidaManualRequest() {}

        // Getters y Setters
        public String getNumeroDocumento() { return numeroDocumento; }
        public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

        public String getDestino() { return destino; }
        public void setDestino(String destino) { this.destino = destino; }

        public String getPlacaVehiculo() { return placaVehiculo; }
        public void setPlacaVehiculo(String placaVehiculo) { this.placaVehiculo = placaVehiculo; }

        public java.time.LocalDate getFechaSalida() { return fechaSalida; }
        public void setFechaSalida(java.time.LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }

        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

        public List<ProductoSalida> getProductos() { return productos; }
        public void setProductos(List<ProductoSalida> productos) { this.productos = productos; }

        // Clase interna para productos de salida
        public static class ProductoSalida {
            private String referencia;
            private Integer cantidad;
            private Double peso;

            // Constructores
            public ProductoSalida() {}

            public ProductoSalida(String referencia, Integer cantidad, Double peso) {
                this.referencia = referencia;
                this.cantidad = cantidad;
                this.peso = peso;
            }

            // Getters y Setters
            public String getReferencia() { return referencia; }
            public void setReferencia(String referencia) { this.referencia = referencia; }

            public Integer getCantidad() { return cantidad; }
            public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

            public Double getPeso() { return peso; }
            public void setPeso(Double peso) { this.peso = peso; }
        }
    }
}