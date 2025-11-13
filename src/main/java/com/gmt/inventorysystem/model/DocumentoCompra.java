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

    @Column(name = "numero_factura", unique = true, nullable = true, length = 50) // nullable = true
    private String numeroFactura;

    @Column(name = "numero_remision", unique = true, nullable = false, length = 50)
    private String numeroRemision;

    // NUEVO CAMPO: Origen del ingenio
    @Column(name = "origen_ingenio", length = 20)
    private String origenIngenio; // ING_MAYAGUEZ, ING_SAN_CARLOS, OTRO

    // RELACIONES ACTUALIZADAS
    @ManyToOne
    @JoinColumn(name = "cliente_nit", referencedColumnName = "nit", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "conductor_cedula", referencedColumnName = "cedula", nullable = false)
    private Conductor conductor;

    // NUEVOS CAMPOS PARA LAS COLUMNAS EXISTENTES EN LA BD
    @Column(name = "cliente", nullable = false, length = 200)
    private String clienteNombre = "CLIENTE TEMPORAL";

    @Column(name = "transportadora", length = 100)
    private String transportadora = "TRANSPORTADORA TEMPORAL";

    @Column(name = "fecha_facturacion", nullable = false)
    private LocalDateTime fechaFacturacion;

    @Column(name = "peso_total", nullable = false)
    private Double pesoTotal = 0.0;

    @Column(name = "valor_total", nullable = false)
    private Double valorTotal = 0.0;

    @Column(name = "placa_vehiculo", length = 20)
    private String placaVehiculo = "SIN PLACA";

    // Relación con los movimientos de inventario
    @OneToMany(mappedBy = "documentoCompra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovimientoInventario> movimientos = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    // Constructores
    public DocumentoCompra() {}

    // Constructor para documentos PDF
    public DocumentoCompra(String numeroFactura, String numeroRemision, Cliente cliente, Conductor conductor,
                           LocalDateTime fechaFacturacion, Double pesoTotal, Double valorTotal) {
        this.numeroFactura = numeroFactura;
        this.numeroRemision = numeroRemision;
        this.cliente = cliente;
        this.conductor = conductor;
        this.fechaFacturacion = fechaFacturacion;
        this.pesoTotal = pesoTotal != null ? pesoTotal : 0.0;
        this.valorTotal = valorTotal != null ? valorTotal : 0.0;
        this.placaVehiculo = "SIN PLACA";
        // Asignar nombres automáticamente
        this.clienteNombre = cliente != null ? cliente.getNombre() : "CLIENTE TEMPORAL";
        this.transportadora = conductor != null ? conductor.getEmpresaTransporte() : "TRANSPORTADORA TEMPORAL";
        // Origen se determinará por número de factura
        this.origenIngenio = determinarOrigenPorFactura(numeroFactura);
    }

    // Constructor para entradas manuales
    public DocumentoCompra(String numeroRemision, String origenIngenio, Cliente cliente, Conductor conductor,
                           LocalDateTime fechaFacturacion, String placaVehiculo) {
        this.numeroFactura = null; // ✅ NULL para entradas manuales
        this.numeroRemision = numeroRemision;
        this.origenIngenio = origenIngenio;
        this.cliente = cliente;
        this.conductor = conductor;
        this.fechaFacturacion = fechaFacturacion;
        this.pesoTotal = 0.0;
        this.valorTotal = 0.0;
        this.placaVehiculo = placaVehiculo != null ? placaVehiculo : "SIN PLACA";
        this.clienteNombre = "ENTRADA MANUAL";
        this.transportadora = "TRANSPORTE TEMPORAL";
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
        // Auto-determinar origen si es documento PDF
        if (numeroFactura != null && !numeroFactura.isEmpty() && !numeroFactura.startsWith("ENTRADA_")) {
            this.origenIngenio = determinarOrigenPorFactura(numeroFactura);
        }
    }

    public String getNumeroRemision() { return numeroRemision; }
    public void setNumeroRemision(String numeroRemision) { this.numeroRemision = numeroRemision; }

    // NUEVO: Getter y Setter para origenIngenio
    public String getOrigenIngenio() { return origenIngenio; }
    public void setOrigenIngenio(String origenIngenio) { this.origenIngenio = origenIngenio; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente != null && cliente.getNombre() != null) {
            this.clienteNombre = cliente.getNombre();
        }
    }

    public Conductor getConductor() { return conductor; }
    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
        if (conductor != null && conductor.getEmpresaTransporte() != null) {
            this.transportadora = conductor.getEmpresaTransporte();
        }
    }

    // Resto de getters y setters se mantienen igual...
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre != null ? clienteNombre : "CLIENTE TEMPORAL";
    }

    public String getTransportadora() { return transportadora; }
    public void setTransportadora(String transportadora) {
        this.transportadora = transportadora != null ? transportadora : "TRANSPORTADORA TEMPORAL";
    }

    public LocalDateTime getFechaFacturacion() { return fechaFacturacion; }
    public void setFechaFacturacion(LocalDateTime fechaFacturacion) { this.fechaFacturacion = fechaFacturacion; }

    public Double getPesoTotal() { return pesoTotal; }
    public void setPesoTotal(Double pesoTotal) {
        this.pesoTotal = pesoTotal != null ? pesoTotal : 0.0;
    }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal != null ? valorTotal : 0.0;
    }

    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo != null ? placaVehiculo : "SIN PLACA";
    }

    public List<MovimientoInventario> getMovimientos() { return movimientos; }
    public void setMovimientos(List<MovimientoInventario> movimientos) { this.movimientos = movimientos; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    // Método helper para agregar movimiento
    public void addMovimiento(MovimientoInventario movimiento) {
        movimientos.add(movimiento);
        movimiento.setDocumentoCompra(this);
    }

    // Método para determinar origen por factura
    private String determinarOrigenPorFactura(String numeroFactura) {
        if (numeroFactura == null) return "OTRO";

        String facturaUpper = numeroFactura.toUpperCase();
        if (facturaUpper.startsWith("MN")) {
            return "ING_MAYAGUEZ";
        } else if (facturaUpper.startsWith("SN")) {
            return "ING_SAN_CARLOS";
        } else {
            return "OTRO";
        }
    }

    @Override
    public String toString() {
        return "DocumentoCompra{" +
                "id=" + id +
                ", numeroFactura='" + numeroFactura + '\'' +
                ", numeroRemision='" + numeroRemision + '\'' +
                ", origenIngenio='" + origenIngenio + '\'' +
                ", cliente=" + (cliente != null ? cliente.getNit() : "null") +
                ", conductor=" + (conductor != null ? conductor.getCedula() : "null") +
                ", clienteNombre='" + clienteNombre + '\'' +
                ", transportadora='" + transportadora + '\'' +
                ", fechaFacturacion=" + fechaFacturacion +
                ", pesoTotal=" + pesoTotal +
                ", valorTotal=" + valorTotal +
                ", placaVehiculo='" + placaVehiculo + '\'' +
                ", movimientos=" + movimientos.size() +
                '}';
    }
}