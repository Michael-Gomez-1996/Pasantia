package com.gmt.inventorysystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "documentos_compra")
public class DocumentoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_factura", unique = true, nullable = false, length = 50)
    private String numeroFactura;

    @Column(name = "numero_remision", unique = true, nullable = false, length = 50)
    private String numeroRemision;

    @Column(name = "cliente", nullable = false, length = 200)
    private String cliente;

    @Column(name = "fecha_facturacion", nullable = false)
    private LocalDateTime fechaFacturacion;

    @Column(name = "peso_total")
    private Double pesoTotal;

    @Column(name = "valor_total")
    private Double valorTotal;

    @Column(name = "placa_vehiculo", length = 20)
    private String placaVehiculo;

    @Column(name = "transportador", length = 100)
    private String transportador;

    @Column(name = "conductor", length = 100)
    private String conductor;

    // Relación con los movimientos de inventario
    @OneToMany(mappedBy = "documentoCompra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovimientoInventario> movimientos = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    // Constructores
    public DocumentoCompra() {}

    public DocumentoCompra(String numeroFactura, String numeroRemision, String cliente,
                           LocalDateTime fechaFacturacion, Double pesoTotal, Double valorTotal) {
        this.numeroFactura = numeroFactura;
        this.numeroRemision = numeroRemision;
        this.cliente = cliente;
        this.fechaFacturacion = fechaFacturacion;
        this.pesoTotal = pesoTotal;
        this.valorTotal = valorTotal;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public String getNumeroRemision() { return numeroRemision; }
    public void setNumeroRemision(String numeroRemision) { this.numeroRemision = numeroRemision; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public LocalDateTime getFechaFacturacion() { return fechaFacturacion; }
    public void setFechaFacturacion(LocalDateTime fechaFacturacion) { this.fechaFacturacion = fechaFacturacion; }

    public Double getPesoTotal() { return pesoTotal; }
    public void setPesoTotal(Double pesoTotal) { this.pesoTotal = pesoTotal; }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }

    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) { this.placaVehiculo = placaVehiculo; }

    public String getTransportador() { return transportador; }
    public void setTransportador(String transportador) { this.transportador = transportador; }

    public String getConductor() { return conductor; }
    public void setConductor(String conductor) { this.conductor = conductor; }

    public List<MovimientoInventario> getMovimientos() { return movimientos; }
    public void setMovimientos(List<MovimientoInventario> movimientos) { this.movimientos = movimientos; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    // Método helper para agregar movimiento
    public void addMovimiento(MovimientoInventario movimiento) {
        movimientos.add(movimiento);
        movimiento.setDocumentoCompra(this);
    }

    @Override
    public String toString() {
        return "DocumentoCompra{" +
                "id=" + id +
                ", numeroFactura='" + numeroFactura + '\'' +
                ", numeroRemision='" + numeroRemision + '\'' +
                ", cliente='" + cliente + '\'' +
                ", fechaFacturacion=" + fechaFacturacion +
                ", pesoTotal=" + pesoTotal +
                ", valorTotal=" + valorTotal +
                ", movimientos=" + movimientos.size() +
                '}';
    }
}