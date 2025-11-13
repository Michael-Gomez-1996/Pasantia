package com.gmt.inventorysystem.dto;

import java.time.LocalDate;
import java.util.List;

public class EntradaManualRequest {
    private String numeroRemision;
    private String origenIngenio;      // NUEVO: Campo para el origen
    private String placaVehiculo;
    private LocalDate fechaEntrada;
    private String observaciones;
    private List<ProductoEntrada> productos;

    // Constructor vacío
    public EntradaManualRequest() {}

    // Constructor completo
    public EntradaManualRequest(String numeroRemision, String origenIngenio, String placaVehiculo,
                                LocalDate fechaEntrada, String observaciones, List<ProductoEntrada> productos) {
        this.numeroRemision = numeroRemision;
        this.origenIngenio = origenIngenio;
        this.placaVehiculo = placaVehiculo;
        this.fechaEntrada = fechaEntrada;
        this.observaciones = observaciones;
        this.productos = productos;
    }

    // Getters y Setters
    public String getNumeroRemision() { return numeroRemision; }
    public void setNumeroRemision(String numeroRemision) { this.numeroRemision = numeroRemision; }

    // NUEVO: Getter y Setter para origenIngenio
    public String getOrigenIngenio() { return origenIngenio; }
    public void setOrigenIngenio(String origenIngenio) { this.origenIngenio = origenIngenio; }

    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) { this.placaVehiculo = placaVehiculo; }

    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDate fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<ProductoEntrada> getProductos() { return productos; }
    public void setProductos(List<ProductoEntrada> productos) { this.productos = productos; }

    // Clase interna para productos (se mantiene igual)
    public static class ProductoEntrada {
        private String referencia;
        private Integer cantidad;

        public ProductoEntrada() {}

        public ProductoEntrada(String referencia, Integer cantidad) {
            this.referencia = referencia;
            this.cantidad = cantidad;
        }

        // Getters y Setters
        public String getReferencia() { return referencia; }
        public void setReferencia(String referencia) { this.referencia = referencia; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}