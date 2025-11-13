package com.gmt.inventorysystem.dto;

public class ProcesamientoEntradaResponse {
    private boolean success;
    private String message;
    private String numeroRemision;
    private int productosProcesados;
    private int movimientosGenerados;

    // Constructores
    public ProcesamientoEntradaResponse() {}

    public ProcesamientoEntradaResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ProcesamientoEntradaResponse(boolean success, String message, String numeroRemision,
                                        int productosProcesados, int movimientosGenerados) {
        this.success = success;
        this.message = message;
        this.numeroRemision = numeroRemision;
        this.productosProcesados = productosProcesados;
        this.movimientosGenerados = movimientosGenerados;
    }

    // Getters y Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getNumeroRemision() { return numeroRemision; }
    public void setNumeroRemision(String numeroRemision) { this.numeroRemision = numeroRemision; }

    public int getProductosProcesados() { return productosProcesados; }
    public void setProductosProcesados(int productosProcesados) { this.productosProcesados = productosProcesados; }

    public int getMovimientosGenerados() { return movimientosGenerados; }
    public void setMovimientosGenerados(int movimientosGenerados) { this.movimientosGenerados = movimientosGenerados; }
}