package com.gmt.inventorysystem.dto;

import com.gmt.inventorysystem.model.MovimientoInventario;
import com.gmt.inventorysystem.model.DocumentoCompra;
import com.gmt.inventorysystem.model.Producto;

import java.time.LocalDateTime;

public class MovimientoDTO {
    private Long id;
    private String tipoMovimiento;
    private Integer cantidad;
    private String motivo;
    private String usuario;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private LocalDateTime fechaMovimiento;
    private ProductoDTO producto;
    private DocumentoCompraDTO documentoCompra;

    // Constructor
    public MovimientoDTO(MovimientoInventario movimiento) {
        this.id = movimiento.getId();
        this.tipoMovimiento = movimiento.getTipoMovimiento();
        this.cantidad = movimiento.getCantidad();
        this.motivo = movimiento.getMotivo();
        this.usuario = movimiento.getUsuario();
        this.stockAnterior = movimiento.getStockAnterior();
        this.stockNuevo = movimiento.getStockNuevo();
        this.fechaMovimiento = movimiento.getFechaMovimiento();

        if (movimiento.getProducto() != null) {
            this.producto = new ProductoDTO(movimiento.getProducto());
        }

        if (movimiento.getDocumentoCompra() != null) {
            this.documentoCompra = new DocumentoCompraDTO(movimiento.getDocumentoCompra());
        }
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public Integer getStockAnterior() { return stockAnterior; }
    public void setStockAnterior(Integer stockAnterior) { this.stockAnterior = stockAnterior; }

    public Integer getStockNuevo() { return stockNuevo; }
    public void setStockNuevo(Integer stockNuevo) { this.stockNuevo = stockNuevo; }

    public LocalDateTime getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(LocalDateTime fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }

    public ProductoDTO getProducto() { return producto; }
    public void setProducto(ProductoDTO producto) { this.producto = producto; }

    public DocumentoCompraDTO getDocumentoCompra() { return documentoCompra; }
    public void setDocumentoCompra(DocumentoCompraDTO documentoCompra) { this.documentoCompra = documentoCompra; }
}