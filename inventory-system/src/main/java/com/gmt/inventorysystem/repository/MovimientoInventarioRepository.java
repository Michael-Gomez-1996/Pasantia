package com.gmt.inventorysystem.repository;

import com.gmt.inventorysystem.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    // CORREGIDO: Buscar por referencia del producto, no por ID
    List<MovimientoInventario> findByProductoReferenciaOrderByFechaMovimientoDesc(String productoReferencia);

    List<MovimientoInventario> findByTipoMovimientoOrderByFechaMovimientoDesc(String tipoMovimiento);

    List<MovimientoInventario> findAllByOrderByFechaMovimientoDesc();

    // NUEVOS MÉTODOS PARA TRABAJAR CON DOCUMENTOCOMPRA

    // Buscar movimientos por documento compra
    List<MovimientoInventario> findByDocumentoCompraIdOrderByFechaMovimientoDesc(Long documentoCompraId);

    // Buscar movimientos que tengan documento compra (salidas por PDF)
    List<MovimientoInventario> findByDocumentoCompraIsNotNullOrderByFechaMovimientoDesc();

    // Buscar movimientos sin documento compra (movimientos manuales)
    List<MovimientoInventario> findByDocumentoCompraIsNullOrderByFechaMovimientoDesc();

    // NUEVO MÉTODO MEJORADO: Buscar todos los movimientos con documento compra y producto cargados
    @Query("SELECT m FROM MovimientoInventario m LEFT JOIN FETCH m.documentoCompra LEFT JOIN FETCH m.producto ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findAllWithDocumentoCompraAndProducto();

    // Buscar movimientos por documento compra con relaciones cargadas
    @Query("SELECT m FROM MovimientoInventario m LEFT JOIN FETCH m.documentoCompra LEFT JOIN FETCH m.producto WHERE m.documentoCompra.id = :documentoCompraId ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findByDocumentoCompraIdWithDocumentoCompra(@Param("documentoCompraId") Long documentoCompraId);

    // Buscar movimiento específico con documento compra cargado
    @Query("SELECT m FROM MovimientoInventario m LEFT JOIN FETCH m.documentoCompra LEFT JOIN FETCH m.producto WHERE m.id = :id")
    Optional<MovimientoInventario> findByIdWithDocumentoCompra(@Param("id") Long id);
}