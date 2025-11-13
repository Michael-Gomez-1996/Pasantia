package com.gmt.inventorysystem.dto;

import java.time.LocalDateTime;

public class ReporteMovimientoDTO {
    private LocalDateTime fecha;
    private String numeroRemision;
    private String numeroFactura;
    private String tipoMovimiento;
    private String origen;
    private String destino;
    private Integer totalUnidades;
    private Double valorTotal;
    private Double pesoTotalKilos;
    private String conductorNombre;
    private String conductorCedula;

    // Constructores
    public ReporteMovimientoDTO() {}

    public ReporteMovimientoDTO(LocalDateTime fecha, String numeroRemision, String numeroFactura,
                                String tipoMovimiento, String origen, String destino,
                                Integer totalUnidades, Double valorTotal, Double pesoTotalKilos,
                                String conductorNombre, String conductorCedula) {
        this.fecha = fecha;
        this.numeroRemision = numeroRemision;
        this.numeroFactura = numeroFactura;
        this.tipoMovimiento = tipoMovimiento;
        this.origen = origen;
        this.destino = destino;
        this.totalUnidades = totalUnidades;
        this.valorTotal = valorTotal;
        this.pesoTotalKilos = pesoTotalKilos;
        this.conductorNombre = conductorNombre;
        this.conductorCedula = conductorCedula;
    }

    // Getters y Setters
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getNumeroRemision() { return numeroRemision; }
    public void setNumeroRemision(String numeroRemision) { this.numeroRemision = numeroRemision; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public Integer getTotalUnidades() { return totalUnidades; }
    public void setTotalUnidades(Integer totalUnidades) { this.totalUnidades = totalUnidades; }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }

    public Double getPesoTotalKilos() { return pesoTotalKilos; }
    public void setPesoTotalKilos(Double pesoTotalKilos) { this.pesoTotalKilos = pesoTotalKilos; }

    public String getConductorNombre() { return conductorNombre; }
    public void setConductorNombre(String conductorNombre) { this.conductorNombre = conductorNombre; }

    public String getConductorCedula() { return conductorCedula; }
    public void setConductorCedula(String conductorCedula) { this.conductorCedula = conductorCedula; }
}