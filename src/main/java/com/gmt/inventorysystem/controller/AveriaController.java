package com.gmt.inventorysystem.controller;

import com.gmt.inventorysystem.dto.AveriaDTO;
import com.gmt.inventorysystem.model.Averia;
import com.gmt.inventorysystem.service.AveriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/averias")
public class AveriaController {

    @Autowired
    private AveriaService averiaService;

    // Registrar nueva avería - CORREGIDO
    @PostMapping
    public ResponseEntity<?> registrarAveria(@RequestBody AveriaRequest request) {
        try {
            // Crear objeto Averia
            Averia averia = new Averia();

            // ✅ CORRECCIÓN: Solo establecer la referencia, el servicio se encargará del producto completo
            com.gmt.inventorysystem.model.Producto producto = new com.gmt.inventorysystem.model.Producto();
            producto.setReferencia(request.getProductoReferencia());
            averia.setProducto(producto);

            averia.setCantidad(request.getCantidad());
            averia.setTipoAveria(request.getTipoAveria());
            averia.setNumeroRemision(request.getNumeroRemision());
            averia.setObservaciones(request.getObservaciones());
            averia.setUsuario(request.getUsuario() != null ? request.getUsuario() : "SISTEMA");
            averia.setFechaDeteccion(request.getFechaDeteccion() != null ? request.getFechaDeteccion() : LocalDateTime.now());

            Averia nuevaAveria = averiaService.registrarAveria(averia);
            return ResponseEntity.ok(nuevaAveria);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Obtener todas las averías
    @GetMapping
    public List<AveriaDTO> obtenerTodasLasAverias() {
        return averiaService.obtenerTodasLasAverias();
    }

    // Obtener avería por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerAveriaPorId(@PathVariable Long id) {
        Optional<Averia> averia = averiaService.obtenerAveriaPorId(id);
        if (averia.isPresent()) {
            return ResponseEntity.ok(new AveriaDTO(averia.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener averías por producto
    @GetMapping("/producto/{productoReferencia}")
    public List<AveriaDTO> obtenerAveriasPorProducto(@PathVariable String productoReferencia) {
        return averiaService.obtenerAveriasPorProducto(productoReferencia);
    }

    // Obtener averías por tipo
    @GetMapping("/tipo/{tipoAveria}")
    public List<AveriaDTO> obtenerAveriasPorTipo(@PathVariable String tipoAveria) {
        return averiaService.obtenerAveriasPorTipo(tipoAveria);
    }

    // Obtener averías por remisión
    @GetMapping("/remision/{numeroRemision}")
    public List<AveriaDTO> obtenerAveriasPorRemision(@PathVariable String numeroRemision) {
        return averiaService.obtenerAveriasPorRemision(numeroRemision);
    }

    // Eliminar avería
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAveria(@PathVariable Long id) {
        try {
            averiaService.eliminarAveria(id);
            return ResponseEntity.ok().body("Avería eliminada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Clase interna para el request
    public static class AveriaRequest {
        private String productoReferencia;
        private Integer cantidad;
        private String tipoAveria;
        private String numeroRemision;
        private String observaciones;
        private String usuario;
        private LocalDateTime fechaDeteccion;

        // Getters y Setters
        public String getProductoReferencia() { return productoReferencia; }
        public void setProductoReferencia(String productoReferencia) { this.productoReferencia = productoReferencia; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public String getTipoAveria() { return tipoAveria; }
        public void setTipoAveria(String tipoAveria) { this.tipoAveria = tipoAveria; }

        public String getNumeroRemision() { return numeroRemision; }
        public void setNumeroRemision(String numeroRemision) { this.numeroRemision = numeroRemision; }

        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }

        public LocalDateTime getFechaDeteccion() { return fechaDeteccion; }
        public void setFechaDeteccion(LocalDateTime fechaDeteccion) { this.fechaDeteccion = fechaDeteccion; }
    }
}