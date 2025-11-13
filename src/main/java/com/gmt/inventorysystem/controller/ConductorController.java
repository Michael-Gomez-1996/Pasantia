package com.gmt.inventorysystem.controller;

import com.gmt.inventorysystem.model.Conductor;
import com.gmt.inventorysystem.service.ConductorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/conductores")
public class ConductorController extends BaseController<Conductor, String> {

    @Autowired
    private ConductorService conductorService;

    // ✅ MÉTODOS FALTANTES - AGREGAR:

    // Obtener todos los conductores
    @GetMapping
    public List<Conductor> obtenerTodosLosConductores() {
        return conductorService.obtenerTodosLosConductores();
    }

    // Obtener conductor por cédula
    @GetMapping("/{cedula}")
    public ResponseEntity<?> obtenerConductorPorCedula(@PathVariable String cedula) {
        Optional<Conductor> conductor = conductorService.obtenerConductorPorCedula(cedula);
        return handleFindById(conductor, "Conductor");
    }

    // Crear conductor
    @PostMapping
    public ResponseEntity<?> crearConductor(@RequestBody Conductor conductor) {
        try {
            Conductor nuevoConductor = conductorService.crearConductor(conductor);
            return ResponseEntity.ok(nuevoConductor);
        } catch (RuntimeException e) {
            return handleCreate(e);
        }
    }

    // Actualizar conductor
    @PutMapping("/{cedula}")
    public ResponseEntity<?> actualizarConductor(@PathVariable String cedula, @RequestBody Conductor conductor) {
        try {
            Conductor conductorActualizado = conductorService.actualizarConductor(cedula, conductor);
            return ResponseEntity.ok(conductorActualizado);
        } catch (RuntimeException e) {
            return handleUpdate(e);
        }
    }

    // Eliminar conductor
    @DeleteMapping("/{cedula}")
    public ResponseEntity<?> eliminarConductor(@PathVariable String cedula) {
        try {
            conductorService.eliminarConductor(cedula);
            return handleSuccess("Conductor eliminado correctamente");
        } catch (RuntimeException e) {
            return handleDelete(e);
        }
    }

    // Buscar conductores por nombre
    @GetMapping("/buscar")
    public List<Conductor> buscarConductoresPorNombre(@RequestParam String nombre) {
        return conductorService.buscarConductoresPorNombre(nombre);
    }

    // Buscar conductores por empresa de transporte
    @GetMapping("/empresa")
    public List<Conductor> buscarConductoresPorEmpresa(@RequestParam String empresa) {
        return conductorService.buscarConductoresPorEmpresa(empresa);
    }

    // ✅ YA EXISTE - mantener:
    @PostMapping("/crear-rapido")
    public ResponseEntity<?> crearConductorRapido(@RequestBody ConductorRapidoRequest request) {
        try {
            Conductor conductor = new Conductor();
            conductor.setCedula(request.getCedula());
            conductor.setNombre(request.getNombre());
            conductor.setEmpresaTransporte(request.getEmpresaTransporte());

            Conductor nuevoConductor = conductorService.crearConductor(conductor);
            return ResponseEntity.ok(nuevoConductor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public static class ConductorRapidoRequest {
        private String cedula;
        private String nombre;
        private String empresaTransporte;

        public String getCedula() { return cedula; }
        public void setCedula(String cedula) { this.cedula = cedula; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getEmpresaTransporte() { return empresaTransporte; }
        public void setEmpresaTransporte(String empresaTransporte) {
            this.empresaTransporte = empresaTransporte;
        }
    }
}