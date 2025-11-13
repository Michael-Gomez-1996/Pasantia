package com.gmt.inventorysystem.dto;

import com.gmt.inventorysystem.model.Producto;

public class ProductoDTO {
    private String referencia;
    private String nombre;
    private String categoria;
    private String lote;
    private String proveedor;

    public ProductoDTO(Producto producto) {
        this.referencia = producto.getReferencia();
        this.nombre = producto.getNombre();
        this.categoria = producto.getCategoria();
        this.lote = producto.getLote();
        this.proveedor = producto.getProveedor();
    }

    // Getters y Setters
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }

    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }
}