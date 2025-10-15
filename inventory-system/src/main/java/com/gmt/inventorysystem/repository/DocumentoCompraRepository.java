package com.gmt.inventorysystem.repository;

import com.gmt.inventorysystem.model.DocumentoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentoCompraRepository extends JpaRepository<DocumentoCompra, Long> {

    // Buscar por número de factura
    Optional<DocumentoCompra> findByNumeroFactura(String numeroFactura);

    // Buscar por número de remisión
    Optional<DocumentoCompra> findByNumeroRemision(String numeroRemision);

    // Verificar si existe por número de factura
    boolean existsByNumeroFactura(String numeroFactura);

    // Verificar si existe por número de remisión
    boolean existsByNumeroRemision(String numeroRemision);

    // Buscar documentos por rango de fechas de facturación
    @Query("SELECT dc FROM DocumentoCompra dc WHERE dc.fechaFacturacion BETWEEN :fechaInicio AND :fechaFin ORDER BY dc.fechaFacturacion DESC")
    List<DocumentoCompra> findByFechaFacturacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio,
                                                        @Param("fechaFin") LocalDateTime fechaFin);

    // Buscar todos los documentos con sus movimientos cargados (para reportes)
    @Query("SELECT dc FROM DocumentoCompra dc LEFT JOIN FETCH dc.movimientos ORDER BY dc.fechaFacturacion DESC")
    List<DocumentoCompra> findAllWithMovimientos();

    // Buscar documento específico con sus movimientos cargados
    @Query("SELECT dc FROM DocumentoCompra dc LEFT JOIN FETCH dc.movimientos WHERE dc.id = :id")
    Optional<DocumentoCompra> findByIdWithMovimientos(@Param("id") Long id);
}