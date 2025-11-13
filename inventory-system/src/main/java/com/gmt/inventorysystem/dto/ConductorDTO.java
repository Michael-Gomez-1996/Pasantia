package com.gmt.inventorysystem.dto;

import com.gmt.inventorysystem.model.Conductor;

public class ConductorDTO {
    private String cedula;
    private String nombre;
    private String empresaTransporte;
    private String transportadora;
    private String placaVehiculo;

    public ConductorDTO(Conductor conductor) {
        this.cedula = conductor.getCedula();
        this.nombre = conductor.getNombre();
        this.empresaTransporte = conductor.getEmpresaTransporte();
        this.transportadora = conductor.getTransportadora();
        this.placaVehiculo = conductor.getPlacaVehiculo();
    }

    // Getters y Setters
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmpresaTransporte() { return empresaTransporte; }
    public void setEmpresaTransporte(String empresaTransporte) { this.empresaTransporte = empresaTransporte; }

    public String getTransportadora() { return transportadora; }
    public void setTransportadora(String transportadora) { this.transportadora = transportadora; }

    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) { this.placaVehiculo = placaVehiculo; }
}