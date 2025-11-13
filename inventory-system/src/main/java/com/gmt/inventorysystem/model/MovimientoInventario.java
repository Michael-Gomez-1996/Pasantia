    package com.gmt.inventorysystem.model;

    import jakarta.persistence.*;
    import org.hibernate.annotations.CreationTimestamp;

    import java.time.LocalDateTime;

    @Entity
    @Table(name = "movimientos_inventario")
    public class MovimientoInventario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "producto_referencia", referencedColumnName = "referencia", nullable = false)
        private Producto producto;

        @Column(nullable = false, length = 20)
        private String tipoMovimiento; // ENTRADA, SALIDA, AJUSTE

        @Column(nullable = false)
        private Integer cantidad;

        @Column(length = 500)
        private String motivo;

        @Column(length = 100)
        private String usuario;

        @Column(name = "stock_anterior", nullable = false)
        private Integer stockAnterior;

        @Column(name = "stock_nuevo", nullable = false)
        private Integer stockNuevo;

        @CreationTimestamp
        @Column(name = "fecha_movimiento", updatable = false)
        private LocalDateTime fechaMovimiento;

        // Relación con DocumentoCompra para agrupar movimientos de una misma remisión/factura
        @ManyToOne
        @JoinColumn(name = "documento_compra_id")
        private DocumentoCompra documentoCompra;

        // Constructores
        public MovimientoInventario() {}

        public MovimientoInventario(Producto producto, String tipoMovimiento, Integer cantidad,
                                    String motivo, String usuario, Integer stockAnterior, Integer stockNuevo) {
            this.producto = producto;
            this.tipoMovimiento = tipoMovimiento;
            this.cantidad = cantidad;
            this.motivo = motivo;
            this.usuario = usuario;
            this.stockAnterior = stockAnterior;
            this.stockNuevo = stockNuevo;
        }

        public MovimientoInventario(Producto producto, String tipoMovimiento, Integer cantidad,
                                    String motivo, String usuario, Integer stockAnterior,
                                    Integer stockNuevo, DocumentoCompra documentoCompra) {
            this.producto = producto;
            this.tipoMovimiento = tipoMovimiento;
            this.cantidad = cantidad;
            this.motivo = motivo;
            this.usuario = usuario;
            this.stockAnterior = stockAnterior;
            this.stockNuevo = stockNuevo;
            this.documentoCompra = documentoCompra;
        }

        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Producto getProducto() { return producto; }
        public void setProducto(Producto producto) { this.producto = producto; }

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

        public DocumentoCompra getDocumentoCompra() { return documentoCompra; }
        public void setDocumentoCompra(DocumentoCompra documentoCompra) { this.documentoCompra = documentoCompra; }

        @Override
        public String toString() {
            return "MovimientoInventario{" +
                    "id=" + id +
                    ", producto=" + (producto != null ? producto.getReferencia() : "null") +
                    ", tipoMovimiento='" + tipoMovimiento + '\'' +
                    ", cantidad=" + cantidad +
                    ", motivo='" + motivo + '\'' +
                    ", usuario='" + usuario + '\'' +
                    ", stockAnterior=" + stockAnterior +
                    ", stockNuevo=" + stockNuevo +
                    ", fechaMovimiento=" + fechaMovimiento +
                    ", documentoCompra=" + (documentoCompra != null ? documentoCompra.getId() : "null") +
                    '}';
        }
    }