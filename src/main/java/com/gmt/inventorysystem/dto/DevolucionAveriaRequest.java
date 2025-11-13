package com.gmt.inventorysystem.dto;

import java.time.LocalDate;
import java.util.List;

public class DevolucionAveriaRequest {
    private String numeroRemision;
    private String ingenioDestino;
    private String placaVehiculo;
    private LocalDate fechaDevolucion;
    private String observaciones;
    private List<ProductoAveria> productos;

    // Clase interna para productos con averías
    public static class ProductoAveria {
        private String referencia;
        private Integer cantidad;
        private Long averiaId; // Opcional: ID específico de avería

        // Constructores
        public ProductoAveria() {}

        public ProductoAveria(String referencia, Integer cantidad) {
            this.referencia = referencia;
            this.cantidad = cantidad;
        }

        public ProductoAveria(String referencia, Integer cantidad, Long averiaId) {
            this.referencia = referencia;
            this.cantidad = cantidad;
            this.averiaId = averiaId;
        }

        // Getters y Setters
        public String getReferencia() { return referencia; }
        public void setReferencia(String referencia) { this.referencia = referencia; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public Long getAveriaId() { return averiaId; }
        public void setAveriaId(Long averiaId) { this.averiaId = averiaId; }
    }

    // Constructores
    public DevolucionAveriaRequest() {}

    public DevolucionAveriaRequest(String numeroRemision, String ingenioDestino, String placaVehiculo,
                                   LocalDate fechaDevolucion, String observaciones, List<ProductoAveria> productos) {
        this.numeroRemision = numeroRemision;
        this.ingenioDestino = ingenioDestino;
        this.placaVehiculo = placaVehiculo;
        this.fechaDevolucion = fechaDevolucion;
        this.observaciones = observaciones;
        this.productos = productos;
    }

    // Getters y Setters
    public String getNumeroRemision() { return numeroRemision; }
    public void setNumeroRemision(String numeroRemision) { this.numeroRemision = numeroRemision; }

    public String getIngenioDestino() { return ingenioDestino; }
    public void setIngenioDestino(String ingenioDestino) { this.ingenioDestino = ingenioDestino; }

    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) { this.placaVehiculo = placaVehiculo; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<ProductoAveria> getProductos() { return productos; }
    public void setProductos(List<ProductoAveria> productos) { this.productos = productos; }
}