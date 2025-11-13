package com.gmt.inventorysystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @Column(name = "referencia", unique = true, nullable = false, length = 50)
    private String referencia;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 20)
    private String lote;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, length = 20)
    private String categoria;

    @Column(name = "cantidad_stock", nullable = false)
    private Integer cantidadStock = 0;

    @Column(name = "cantidad_averiada", nullable = false)
    private Integer cantidadAveriada = 0;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 50;

    @Column(name = "peso_por_paca", nullable = false)
    private Double pesoPorPaca;

    @Column(name = "unidades_por_paca", nullable = false)
    private Integer unidadesPorPaca;

    @Column(name = "pacas_por_estiba", nullable = false)
    private Integer pacasPorEstiba = 100;

    @Column(nullable = false, length = 50)
    private String proveedor;

    @Column(nullable = false, length = 20)
    private String ubicacion = "ESTIBA";

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public Producto() {
    }

    public Producto(String referencia, String nombre, String lote, String descripcion,
                    String categoria, Integer cantidadStock, Integer stockMinimo,
                    Double pesoPorPaca, Integer unidadesPorPaca, Integer pacasPorEstiba,
                    String proveedor) {
        this.referencia = referencia;
        this.nombre = nombre;
        this.lote = lote;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.cantidadStock = cantidadStock;
        this.cantidadAveriada = 0;
        this.stockMinimo = stockMinimo;
        this.pesoPorPaca = pesoPorPaca;
        this.unidadesPorPaca = unidadesPorPaca;
        this.pacasPorEstiba = pacasPorEstiba;
        this.proveedor = proveedor;
        this.ubicacion = "ESTIBA";
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
        if (categoria != null &&
                !categoria.equals("AZUCAR_BLANCA") &&
                !categoria.equals("AZUCAR_NATURAL")) {
            throw new IllegalArgumentException("Categoría debe ser AZUCAR_BLANCA o AZUCAR_NATURAL");
        }
        this.categoria = categoria;
    }

    public Integer getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(Integer cantidadStock) {
        this.cantidadStock = cantidadStock;
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
        this.pacasPorEstiba = pacasPorEstiba != null ? pacasPorEstiba : 100;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        if (proveedor != null &&
                !proveedor.equals("ING_MAYAGUEZ") &&
                !proveedor.equals("ING_SAN_CARLOS")) {
            throw new IllegalArgumentException("Proveedor debe ser ING_MAYAGUEZ o ING_SAN_CARLOS");
        }
        this.proveedor = proveedor;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = "ESTIBA";
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Integer getInventarioTotal() {
        return cantidadStock + cantidadAveriada;
    }

    public Double getPesoTotalInventario() {
        return cantidadStock * pesoPorPaca;
    }

    public Integer getTotalEstibas() {
        if (pacasPorEstiba == null || pacasPorEstiba == 0) return 0;
        return cantidadStock / pacasPorEstiba;
    }

    public Integer getPacasSueltas() {
        if (pacasPorEstiba == null || pacasPorEstiba == 0) return cantidadStock;
        return cantidadStock % pacasPorEstiba;
    }

    public Double getPesoPorEstiba() {
        if (pacasPorEstiba == null || pesoPorPaca == null) return 0.0;
        return pacasPorEstiba * pesoPorPaca;
    }

    public Double getPesoTotalEstibas() {
        return getTotalEstibas() * getPesoPorEstiba();
    }

    public boolean isBajoStockMinimo() {
        return cantidadStock <= stockMinimo;
    }

    public static String[] getCategoriasPermitidas() {
        return new String[]{"AZUCAR_BLANCA", "AZUCAR_NATURAL"};
    }

    public static String[] getProveedoresPermitidos() {
        return new String[]{"ING_MAYAGUEZ", "ING_SAN_CARLOS"};
    }

    @Override
    public String toString() {
        return "Producto{" +
                "referencia='" + referencia + '\'' +
                ", nombre='" + nombre + '\'' +
                ", lote='" + lote + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", categoria='" + categoria + '\'' +
                ", cantidadStock=" + cantidadStock +
                ", cantidadAveriada=" + cantidadAveriada +
                ", stockMinimo=" + stockMinimo +
                ", pesoPorPaca=" + pesoPorPaca +
                ", unidadesPorPaca=" + unidadesPorPaca +
                ", pacasPorEstiba=" + pacasPorEstiba +
                ", proveedor='" + proveedor + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}