package com.gmt.inventorysystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @Column(name = "nit", nullable = false, unique = true, length = 20)
    private String nit;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    // NUEVO CAMPO - agregar esta línea
    @Column(name = "razon_social", nullable = false, length = 200)
    private String razonSocial;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Relación con documentos de compra
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DocumentoCompra> documentosCompra = new ArrayList<>();

    // Constructores
    public Cliente() {}

    public Cliente(String nit, String nombre) {
        this.nit = nit;
        this.nombre = nombre;
        this.razonSocial = nombre; // Usar el nombre como razón social por defecto
    }

    public Cliente(String nit, String nombre, String razonSocial) {
        this.nit = nit;
        this.nombre = nombre;
        this.razonSocial = razonSocial;
    }

    // Getters y Setters
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        this.nombre = nombre;
        // Si no hay razón social, usar el nombre
        if (this.razonSocial == null) {
            this.razonSocial = nombre;
        }
    }

    // NUEVO GETTER Y SETTER
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public List<DocumentoCompra> getDocumentosCompra() { return documentosCompra; }
    public void setDocumentosCompra(List<DocumentoCompra> documentosCompra) { this.documentosCompra = documentosCompra; }

    @Override
    public String toString() {
        return "Cliente{" +
                "nit='" + nit + '\'' +
                ", nombre='" + nombre + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}