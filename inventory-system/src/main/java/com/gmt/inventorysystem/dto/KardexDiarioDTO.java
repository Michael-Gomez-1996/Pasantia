    package com.gmt.inventorysystem.dto;

    import java.time.LocalDate;

    public class KardexDiarioDTO {
        private String referencia;
        private String nombre;
        private Integer pacasPorEstiba;

        // Datos por día
        private LocalDate fecha;
        private Integer devolucion;
        private Integer entradas;
        private Integer salida;
        private Integer existencias;
        private Integer estibas; // ✅ Este campo debe existir

        // Constructores
        public KardexDiarioDTO() {}

        public KardexDiarioDTO(String referencia, String nombre, Integer pacasPorEstiba) {
            this.referencia = referencia;
            this.nombre = nombre;
            this.pacasPorEstiba = pacasPorEstiba;
        }

        // Getters y Setters
        public String getReferencia() { return referencia; }
        public void setReferencia(String referencia) { this.referencia = referencia; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public Integer getPacasPorEstiba() { return pacasPorEstiba; }
        public void setPacasPorEstiba(Integer pacasPorEstiba) { this.pacasPorEstiba = pacasPorEstiba; }

        public LocalDate getFecha() { return fecha; }
        public void setFecha(LocalDate fecha) { this.fecha = fecha; }

        public Integer getDevolucion() { return devolucion; }
        public void setDevolucion(Integer devolucion) { this.devolucion = devolucion; }

        public Integer getEntradas() { return entradas; }
        public void setEntradas(Integer entradas) { this.entradas = entradas; }

        public Integer getSalida() { return salida; }
        public void setSalida(Integer salida) { this.salida = salida; }

        public Integer getExistencias() { return existencias; }
        public void setExistencias(Integer existencias) { this.existencias = existencias; }

        public Integer getEstibas() { return estibas; }
        public void setEstibas(Integer estibas) { this.estibas = estibas; }
    }