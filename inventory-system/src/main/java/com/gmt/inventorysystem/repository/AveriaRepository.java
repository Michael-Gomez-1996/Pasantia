package com.gmt.inventorysystem.repository;

import com.gmt.inventorysystem.model.Averia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AveriaRepository extends JpaRepository<Averia, Long> {

    List<Averia> findByProductoReferenciaOrderByFechaRegistroDesc(String productoReferencia);

    List<Averia> findByTipoAveriaOrderByFechaRegistroDesc(String tipoAveria);

    List<Averia> findByNumeroRemision(String numeroRemision);

    List<Averia> findAllByOrderByFechaRegistroDesc();

    @Query("SELECT a FROM Averia a LEFT JOIN FETCH a.producto ORDER BY a.fechaRegistro DESC")
    List<Averia> findAllWithProducto();
}