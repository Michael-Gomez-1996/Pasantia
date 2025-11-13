package com.gmt.inventorysystem.repository;

import com.gmt.inventorysystem.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    // Buscar cliente por NIT
    Optional<Cliente> findByNit(String nit);

    // Verificar si existe un cliente con el NIT
    boolean existsByNit(String nit);

    // Buscar clientes por nombre (búsqueda parcial)
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    // Obtener todos los clientes ordenados por nombre
    List<Cliente> findAllByOrderByNombreAsc();

    // Buscar cliente con sus documentos de compra
    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.documentosCompra WHERE c.nit = :nit")
    Optional<Cliente> findByNitWithDocumentos(@Param("nit") String nit);

    // Contar total de clientes
    long count();
}