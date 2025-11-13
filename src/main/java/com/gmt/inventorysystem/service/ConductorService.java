package com.gmt.inventorysystem.service;

import com.gmt.inventorysystem.model.Conductor;
import com.gmt.inventorysystem.repository.ConductorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConductorService extends BaseService<Conductor, String> {

    @Autowired
    private ConductorRepository conductorRepository;

    // Crear un nuevo conductor
    public Conductor crearConductor(Conductor conductor) {
        // Validar que la cédula no exista
        if (conductorRepository.existsByCedula(conductor.getCedula())) {
            throw new RuntimeException("Ya existe un conductor con la cédula: " + conductor.getCedula());
        }

        // Validar datos requeridos
        validateRequiredField(conductor.getCedula(), "La cédula del conductor");
        validateRequiredField(conductor.getNombre(), "El nombre del conductor");

        return conductorRepository.save(conductor);
    }

    // Obtener conductor por cédula
    public Optional<Conductor> obtenerConductorPorCedula(String cedula) {
        return conductorRepository.findByCedula(cedula);
    }

    // Verificar si existe un conductor
    public boolean existeConductor(String cedula) {
        return conductorRepository.existsByCedula(cedula);
    }

    // Obtener todos los conductores
    public List<Conductor> obtenerTodosLosConductores() {
        return conductorRepository.findAllByOrderByNombreAsc();
    }

    // Buscar conductores por nombre
    public List<Conductor> buscarConductoresPorNombre(String nombre) {
        return conductorRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Buscar conductores por empresa de transporte
    public List<Conductor> buscarConductoresPorEmpresa(String empresaTransporte) {
        return conductorRepository.findByEmpresaTransporteContainingIgnoreCase(empresaTransporte);
    }

    // Actualizar conductor
    public Conductor actualizarConductor(String cedula, Conductor conductorActualizado) {
        return conductorRepository.findByCedula(cedula)
                .map(conductorExistente -> {
                    // Actualizar nombre si se proporciona
                    if (conductorActualizado.getNombre() != null &&
                            !conductorActualizado.getNombre().trim().isEmpty()) {
                        conductorExistente.setNombre(conductorActualizado.getNombre());
                    }

                    // Actualizar empresa de transporte si se proporciona
                    if (conductorActualizado.getEmpresaTransporte() != null) {
                        conductorExistente.setEmpresaTransporte(conductorActualizado.getEmpresaTransporte());
                    }

                    return conductorRepository.save(conductorExistente);
                })
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado con cédula: " + cedula));
    }

    // Eliminar conductor
    public void eliminarConductor(String cedula) {
        validateEntityExists(conductorRepository, cedula, "Conductor");
        conductorRepository.deleteById(cedula);
    }

    // Contar total de conductores
    public long contarConductores() {
        return conductorRepository.count();
    }
}