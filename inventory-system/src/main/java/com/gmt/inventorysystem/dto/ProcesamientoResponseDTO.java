package com.gmt.inventorysystem.dto;

public class ProcesamientoResponseDTO {
    private boolean success;
    private String message;
    private String numeroRemision;
    private String numeroFactura;
    private String cliente;
    private int cantidadProductosProcesados;
    private int movimientosGenerados;

    public ProcesamientoResponseDTO() {}

    public ProcesamientoResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters y Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getNumeroRemision() { return numeroRemision; }
    public void setNumeroRemision(String numeroRemision) { this.numeroRemision = numeroRemision; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public int getCantidadProductosProcesados() { return cantidadProductosProcesados; }
    public void setCantidadProductosProcesados(int cantidadProductosProcesados) { this.cantidadProductosProcesados = cantidadProductosProcesados; }

    public int getMovimientosGenerados() { return movimientosGenerados; }
    public void setMovimientosGenerados(int movimientosGenerados) { this.movimientosGenerados = movimientosGenerados; }
}