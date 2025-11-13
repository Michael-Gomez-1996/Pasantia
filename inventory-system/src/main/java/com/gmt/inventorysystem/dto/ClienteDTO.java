package com.gmt.inventorysystem.dto;

import com.gmt.inventorysystem.model.Cliente;

public class ClienteDTO {
    private String nit;
    private String nombre;
    private String razonSocial;

    public ClienteDTO(Cliente cliente) {
        this.nit = cliente.getNit();
        this.nombre = cliente.getNombre();
        this.razonSocial = cliente.getRazonSocial();
    }

    // Getters y Setters
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
}