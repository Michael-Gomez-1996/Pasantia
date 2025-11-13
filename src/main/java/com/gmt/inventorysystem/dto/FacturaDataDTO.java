package com.gmt.inventorysystem.dto;

import java.time.LocalDate;

public class FacturaDataDTO {
    private String numeroFactura;
    private String nitCliente;
    private LocalDate fechaFacturacion;
    private Double pesoTotal;
    private Double valorTotal;
    private String numeroRemision;
    private String origenIngenio; // ✅ NUEVO CAMPO

    public FacturaDataDTO() {}

    // Getters y Setters
    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getNitCliente() {
        return nitCliente;
    }

    public void setNitCliente(String nitCliente) {
        this.nitCliente = nitCliente;
    }

    public LocalDate getFechaFacturacion() {
        return fechaFacturacion;
    }

    public void setFechaFacturacion(LocalDate fechaFacturacion) {
        this.fechaFacturacion = fechaFacturacion;
    }

    public Double getPesoTotal() {
        return pesoTotal;
    }

    public void setPesoTotal(Double pesoTotal) {
        this.pesoTotal = pesoTotal;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getNumeroRemision() {
        return numeroRemision;
    }

    public void setNumeroRemision(String numeroRemision) {
        this.numeroRemision = numeroRemision;
    }

    // ✅ NUEVO GETTER Y SETTER
    public String getOrigenIngenio() {
        return origenIngenio;
    }

    public void setOrigenIngenio(String origenIngenio) {
        this.origenIngenio = origenIngenio;
    }
}