package com.gmt.inventorysystem.repository;

import com.gmt.inventorysystem.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar producto por referencia (código único)
    Optional<Producto> findByReferencia(String referencia); // ← AGREGAR ESTE MÉTODO

    // Buscar productos por categoría
    List<Producto> findByCategoria(String categoria);

    // Buscar productos por proveedor
    List<Producto> findByProveedor(String proveedor);

    // Buscar productos que estén bajo stock mínimo
    @Query("SELECT p FROM Producto p WHERE p.cantidadStock <= p.stockMinimo")
    List<Producto> findProductosBajoStockMinimo();

    // Buscar productos por nombre (búsqueda parcial)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Verificar si existe un producto con una referencia
    boolean existsByReferencia(String referencia);

    // Buscar productos por lote
    List<Producto> findByLote(String lote);
}