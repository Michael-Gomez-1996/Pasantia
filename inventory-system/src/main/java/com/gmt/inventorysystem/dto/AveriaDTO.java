package com.gmt.inventorysystem.dto;

import com.gmt.inventorysystem.model.Averia;

import java.time.LocalDateTime;

public class AveriaDTO {
    private Long id;
    private String productoReferencia;
    private String productoNombre;
    private Integer cantidad;
    private String tipoAveria;
    private String numeroRemision;
    private String observaciones;
    private String usuario;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaDeteccion;

    public AveriaDTO(Averia averia) {
        this.id = averia.getId();
        this.productoReferencia = averia.getProducto().getReferencia();
        this.productoNombre = averia.getProducto().getNombre();
        this.cantidad = averia.getCantidad();
        this.tipoAveria = averia.getTipoAveria();
        this.numeroRemision = averia.getNumeroRemision();
        this.observaciones = averia.getObservaciones();
        this.usuario = averia.getUsuario();
        this.fechaRegistro = averia.getFechaRegistro();
        this.fechaDeteccion = averia.getFechaDeteccion();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductoReferencia() { return productoReferencia; }
    public void setProductoReferencia(String productoReferencia) { this.productoReferencia = productoReferencia; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

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

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public LocalDateTime getFechaDeteccion() { return fechaDeteccion; }
    public void setFechaDeteccion(LocalDateTime fechaDeteccion) { this.fechaDeteccion = fechaDeteccion; }
}