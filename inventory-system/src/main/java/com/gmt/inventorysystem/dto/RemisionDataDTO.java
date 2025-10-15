package com.gmt.inventorysystem.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RemisionDataDTO {
    private String numeroRemision;
    private String placaVehiculo;
    private String transportador;
    private String conductor;
    private String cedulaConductor;
    private LocalDate fechaDespacho;
    private LocalTime horaDespacho;
    private LocalDate fechaEntrega;
    private String numeroPedido;
    private String numeroTransporte;
    private List<ProductoRemisionDTO> productos = new ArrayList<>();

    public RemisionDataDTO() {}

    // Getters y Setters
    public String getNumeroRemision() { return numeroRemision; }
    public void setNumeroRemision(String numeroRemision) { this.numeroRemision = numeroRemision; }

    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) { this.placaVehiculo = placaVehiculo; }

    public String getTransportador() { return transportador; }
    public void setTransportador(String transportador) { this.transportador = transportador; }

    public String getConductor() { return conductor; }
    public void setConductor(String conductor) { this.conductor = conductor; }

    public String getCedulaConductor() { return cedulaConductor; }
    public void setCedulaConductor(String cedulaConductor) { this.cedulaConductor = cedulaConductor; }

    public LocalDate getFechaDespacho() { return fechaDespacho; }
    public void setFechaDespacho(LocalDate fechaDespacho) { this.fechaDespacho = fechaDespacho; }

    public LocalTime getHoraDespacho() { return horaDespacho; }
    public void setHoraDespacho(LocalTime horaDespacho) { this.horaDespacho = horaDespacho; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(String numeroPedido) { this.numeroPedido = numeroPedido; }

    public String getNumeroTransporte() { return numeroTransporte; }
    public void setNumeroTransporte(String numeroTransporte) { this.numeroTransporte = numeroTransporte; }

    public List<ProductoRemisionDTO> getProductos() { return productos; }
    public void setProductos(List<ProductoRemisionDTO> productos) { this.productos = productos; }

    public void addProducto(ProductoRemisionDTO producto) {
        this.productos.add(producto);
    }
}