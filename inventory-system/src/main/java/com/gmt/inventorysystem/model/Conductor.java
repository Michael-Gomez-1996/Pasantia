package com.gmt.inventorysystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conductores")
public class Conductor {

    @Id
    @Column(name = "cedula", nullable = false, unique = true, length = 20)
    private String cedula;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "empresa_transporte", length = 100)
    private String empresaTransporte;

    @Column(name = "transportadora", nullable = false, length = 100)
    private String transportadora;

    // NUEVO CAMPO - agregar esta línea
    @Column(name = "placa_vehiculo", nullable = false, length = 20)
    private String placaVehiculo = "SIN PLACA"; // Valor por defecto

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Relación con documentos de compra
    @OneToMany(mappedBy = "conductor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DocumentoCompra> documentosCompra = new ArrayList<>();

    // Constructores
    public Conductor() {}

    public Conductor(String cedula, String nombre, String empresaTransporte) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.empresaTransporte = empresaTransporte;
        this.transportadora = empresaTransporte; // Usar empresa_transporte como transportadora por defecto
        this.placaVehiculo = "SIN PLACA"; // Valor por defecto
    }

    public Conductor(String cedula, String nombre, String empresaTransporte, String transportadora, String placaVehiculo) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.empresaTransporte = empresaTransporte;
        this.transportadora = transportadora;
        this.placaVehiculo = placaVehiculo;
    }

    // Getters y Setters
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmpresaTransporte() { return empresaTransporte; }
    public void setEmpresaTransporte(String empresaTransporte) {
        this.empresaTransporte = empresaTransporte;
        // Si no hay transportadora, usar empresa_transporte
        if (this.transportadora == null) {
            this.transportadora = empresaTransporte;
        }
    }

    public String getTransportadora() { return transportadora; }
    public void setTransportadora(String transportadora) { this.transportadora = transportadora; }

    // NUEVO GETTER Y SETTER
    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo != null ? placaVehiculo : "SIN PLACA";
    }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public List<DocumentoCompra> getDocumentosCompra() { return documentosCompra; }
    public void setDocumentosCompra(List<DocumentoCompra> documentosCompra) { this.documentosCompra = documentosCompra; }

    @Override
    public String toString() {
        return "Conductor{" +
                "cedula='" + cedula + '\'' +
                ", nombre='" + nombre + '\'' +
                ", empresaTransporte='" + empresaTransporte + '\'' +
                ", transportadora='" + transportadora + '\'' +
                ", placaVehiculo='" + placaVehiculo + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}