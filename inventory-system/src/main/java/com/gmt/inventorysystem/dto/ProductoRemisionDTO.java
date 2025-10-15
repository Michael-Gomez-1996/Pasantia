package com.gmt.inventorysystem.dto;

public class ProductoRemisionDTO {
    private String numeroPedido;
    private String referencia;
    private String detalle;
    private String lote;
    private Double cantidad;
    private String unidad;

    public ProductoRemisionDTO() {}

    public ProductoRemisionDTO(String numeroPedido, String referencia, String detalle,
                               String lote, Double cantidad, String unidad) {
        this.numeroPedido = numeroPedido;
        this.referencia = referencia;
        this.detalle = detalle;
        this.lote = lote;
        this.cantidad = cantidad;
        this.unidad = unidad;
    }

    // Getters y Setters
    public String getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(String numeroPedido) { this.numeroPedido = numeroPedido; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }

    public Double getCantidad() { return cantidad; }
    public void setCantidad(Double cantidad) { this.cantidad = cantidad; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
}