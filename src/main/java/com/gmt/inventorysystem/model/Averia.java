package com.gmt.inventorysystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "averias")
public class Averia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_referencia", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "tipo_averia", nullable = false, length = 20)
    private String tipoAveria; // ENTRADA, DEVOLUCION, OPERACION

    @Column(name = "numero_remision", length = 50)
    private String numeroRemision; // ✅ OPCIONAL

    @Column(length = 500)
    private String observaciones;

    @Column(nullable = false, length = 100)
    private String usuario = "SISTEMA";

    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_deteccion", nullable = false)
    private LocalDateTime fechaDeteccion;

    // Constructores
    public Averia() {}

    public Averia(Producto producto, Integer cantidad, String tipoAveria,
                  String numeroRemision, String observaciones, String usuario,
                  LocalDateTime fechaDeteccion) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.tipoAveria = tipoAveria;
        this.numeroRemision = numeroRemision;
        this.observaciones = observaciones;
        this.usuario = usuario;
        this.fechaDeteccion = fechaDeteccion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

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