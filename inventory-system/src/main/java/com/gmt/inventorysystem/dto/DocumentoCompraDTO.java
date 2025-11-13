package com.gmt.inventorysystem.dto;

import com.gmt.inventorysystem.model.DocumentoCompra;
import com.gmt.inventorysystem.model.Cliente;
import com.gmt.inventorysystem.model.Conductor;

import java.time.LocalDateTime;

public class DocumentoCompraDTO {
    private Long id;
    private String numeroFactura;
    private String numeroRemision;
    private String clienteNombre;
    private String transportadora;
    private LocalDateTime fechaFacturacion;
    private Double pesoTotal;
    private Double valorTotal;
    private String placaVehiculo;
    private ClienteDTO cliente;
    private ConductorDTO conductor;

    public DocumentoCompraDTO(DocumentoCompra documento) {
        this.id = documento.getId();
        this.numeroFactura = documento.getNumeroFactura();
        this.numeroRemision = documento.getNumeroRemision();
        this.clienteNombre = documento.getClienteNombre();
        this.transportadora = documento.getTransportadora();
        this.fechaFacturacion = documento.getFechaFacturacion();
        this.pesoTotal = documento.getPesoTotal();
        this.valorTotal = documento.getValorTotal();
        this.placaVehiculo = documento.getPlacaVehiculo();

        if (documento.getCliente() != null) {
            this.cliente = new ClienteDTO(documento.getCliente());
        }

        if (documento.getConductor() != null) {
            this.conductor = new ConductorDTO(documento.getConductor());
        }
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public String getNumeroRemision() { return numeroRemision; }
    public void setNumeroRemision(String numeroRemision) { this.numeroRemision = numeroRemision; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getTransportadora() { return transportadora; }
    public void setTransportadora(String transportadora) { this.transportadora = transportadora; }

    public LocalDateTime getFechaFacturacion() { return fechaFacturacion; }
    public void setFechaFacturacion(LocalDateTime fechaFacturacion) { this.fechaFacturacion = fechaFacturacion; }

    public Double getPesoTotal() { return pesoTotal; }
    public void setPesoTotal(Double pesoTotal) { this.pesoTotal = pesoTotal; }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }

    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) { this.placaVehiculo = placaVehiculo; }

    public ClienteDTO getCliente() { return cliente; }
    public void setCliente(ClienteDTO cliente) { this.cliente = cliente; }

    public ConductorDTO getConductor() { return conductor; }
    public void setConductor(ConductorDTO conductor) { this.conductor = conductor; }
}