package com.gmt.inventorysystem.repository;

import com.gmt.inventorysystem.model.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConductorRepository extends JpaRepository<Conductor, String> {

    // Buscar conductor por cédula
    Optional<Conductor> findByCedula(String cedula);

    // Verificar si existe un conductor con la cédula
    boolean existsByCedula(String cedula);

    // Buscar conductores por nombre (búsqueda parcial)
    List<Conductor> findByNombreContainingIgnoreCase(String nombre);

    // Buscar conductores por empresa de transporte
    List<Conductor> findByEmpresaTransporteContainingIgnoreCase(String empresaTransporte);

    // Obtener todos los conductores ordenados por nombre
    List<Conductor> findAllByOrderByNombreAsc();

    // Buscar conductor con sus documentos de compra
    @Query("SELECT c FROM Conductor c LEFT JOIN FETCH c.documentosCompra WHERE c.cedula = :cedula")
    Optional<Conductor> findByCedulaWithDocumentos(@Param("cedula") String cedula);

    // Contar total de conductores
    long count();
}