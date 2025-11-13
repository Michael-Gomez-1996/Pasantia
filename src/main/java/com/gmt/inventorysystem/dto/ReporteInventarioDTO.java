package com.gmt.inventorysystem.dto;

import java.time.LocalDateTime;

public class ReporteInventarioDTO {
    private String referencia;
    private String nombre;
    private String lote;
    private String descripcion;
    private String categoria;
    private Integer cantidadStock;
    private Integer cantidadAveriada;
    private Integer stockMinimo;
    private Double pesoPorPaca;
    private Integer unidadesPorPaca;
    private Integer pacasPorEstiba;
    private String proveedor;
    private String ubicacion;
    private LocalDateTime fechaCreacion;
    private Integer totalEstibas;
    private Integer pacasSueltas;
    private Double pesoPorEstiba;
    private Double pesoTotalEstibas;

    public ReporteInventarioDTO() {}

    public ReporteInventarioDTO(String referencia, String nombre, String lote, String descripcion,
                                String categoria, Integer cantidadStock, Integer cantidadAveriada,
                                Integer stockMinimo, Double pesoPorPaca, Integer unidadesPorPaca,
                                Integer pacasPorEstiba, String proveedor, String ubicacion,
                                LocalDateTime fechaCreacion) {
        this.referencia = referencia;
        this.nombre = nombre;
        this.lote = lote;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.cantidadStock = cantidadStock;
        this.cantidadAveriada = cantidadAveriada;
        this.stockMinimo = stockMinimo;
        this.pesoPorPaca = pesoPorPaca;
        this.unidadesPorPaca = unidadesPorPaca;
        this.pacasPorEstiba = pacasPorEstiba;
        this.proveedor = proveedor;
        this.ubicacion = ubicacion;
        this.fechaCreacion = fechaCreacion;
        calcularValoresEstiba();
    }

    private void calcularValoresEstiba() {
        if (pacasPorEstiba != null && pacasPorEstiba > 0) {
            this.totalEstibas = cantidadStock / pacasPorEstiba;
            this.pacasSueltas = cantidadStock % pacasPorEstiba;
            this.pesoPorEstiba = pacasPorEstiba * pesoPorPaca;
            this.pesoTotalEstibas = totalEstibas * pesoPorEstiba;
        } else {
            this.totalEstibas = 0;
            this.pacasSueltas = cantidadStock;
            this.pesoPorEstiba = 0.0;
            this.pesoTotalEstibas = 0.0;
        }
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(Integer cantidadStock) {
        this.cantidadStock = cantidadStock;
        calcularValoresEstiba();
    }

    public Integer getCantidadAveriada() {
        return cantidadAveriada;
    }

    public void setCantidadAveriada(Integer cantidadAveriada) {
        this.cantidadAveriada = cantidadAveriada;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Double getPesoPorPaca() {
        return pesoPorPaca;
    }

    public void setPesoPorPaca(Double pesoPorPaca) {
        this.pesoPorPaca = pesoPorPaca;
        calcularValoresEstiba();
    }

    public Integer getUnidadesPorPaca() {
        return unidadesPorPaca;
    }

    public void setUnidadesPorPaca(Integer unidadesPorPaca) {
        this.unidadesPorPaca = unidadesPorPaca;
    }

    public Integer getPacasPorEstiba() {
        return pacasPorEstiba;
    }

    public void setPacasPorEstiba(Integer pacasPorEstiba) {
        this.pacasPorEstiba = pacasPorEstiba;
        calcularValoresEstiba();
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getTotalEstibas() {
        return totalEstibas;
    }

    public void setTotalEstibas(Integer totalEstibas) {
        this.totalEstibas = totalEstibas;
    }

    public Integer getPacasSueltas() {
        return pacasSueltas;
    }

    public void setPacasSueltas(Integer pacasSueltas) {
        this.pacasSueltas = pacasSueltas;
    }

    public Double getPesoPorEstiba() {
        return pesoPorEstiba;
    }

    public void setPesoPorEstiba(Double pesoPorEstiba) {
        this.pesoPorEstiba = pesoPorEstiba;
    }

    public Double getPesoTotalEstibas() {
        return pesoTotalEstibas;
    }

    public void setPesoTotalEstibas(Double pesoTotalEstibas) {
        this.pesoTotalEstibas = pesoTotalEstibas;
    }

    public Integer getInventarioTotal() {
        return (cantidadStock != null ? cantidadStock : 0) +
                (cantidadAveriada != null ? cantidadAveriada : 0);
    }
}