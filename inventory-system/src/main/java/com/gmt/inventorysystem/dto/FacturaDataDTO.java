package com.gmt.inventorysystem.dto;

import java.time.LocalDate;

public class FacturaDataDTO {
    private String numeroFactura;
    private String cliente;
    private LocalDate fechaFacturacion;
    private Double pesoTotal;
    private Double valorTotal;

    public FacturaDataDTO() {}

    // Getters y Setters
    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public LocalDate getFechaFacturacion() { return fechaFacturacion; }
    public void setFechaFacturacion(LocalDate fechaFacturacion) { this.fechaFacturacion = fechaFacturacion; }

    public Double getPesoTotal() { return pesoTotal; }
    public void setPesoTotal(Double pesoTotal) { this.pesoTotal = pesoTotal; }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
}