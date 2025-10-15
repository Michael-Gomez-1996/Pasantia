package com.gmt.inventorysystem.service;

import com.gmt.inventorysystem.model.DocumentoCompra;
import com.gmt.inventorysystem.model.MovimientoInventario;
import com.gmt.inventorysystem.model.Producto;
import com.gmt.inventorysystem.repository.DocumentoCompraRepository;
import com.gmt.inventorysystem.repository.MovimientoInventarioRepository;
import com.gmt.inventorysystem.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MovimientoInventarioService {

    @Autowired
    private MovimientoInventarioRepository movimientoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private DocumentoCompraRepository documentoCompraRepository;

    // MÉTODOS EXISTENTES (manuales) - CORREGIDOS
    @Transactional
    public MovimientoInventario registrarEntrada(String productoReferencia, Integer cantidad, String motivo, String usuario) {
        Producto producto = productoRepository.findByReferencia(productoReferencia)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con referencia: " + productoReferencia));

        int stockAnterior = producto.getCantidadStock();
        int stockNuevo = stockAnterior + cantidad;

        producto.setCantidadStock(stockNuevo);
        productoRepository.save(producto);

        MovimientoInventario movimiento = new MovimientoInventario(
                producto, "ENTRADA", cantidad, motivo, usuario, stockAnterior, stockNuevo
        );

        return movimientoRepository.save(movimiento);
    }

    @Transactional
    public MovimientoInventario registrarSalida(String productoReferencia, Integer cantidad, String motivo, String usuario) {
        Producto producto = productoRepository.findByReferencia(productoReferencia)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con referencia: " + productoReferencia));

        int stockAnterior = producto.getCantidadStock();

        if (stockAnterior < cantidad) {
            throw new RuntimeException("Stock insuficiente. Stock actual: " + stockAnterior + ", solicitado: " + cantidad);
        }

        int stockNuevo = stockAnterior - cantidad;

        producto.setCantidadStock(stockNuevo);
        productoRepository.save(producto);

        MovimientoInventario movimiento = new MovimientoInventario(
                producto, "SALIDA", cantidad, motivo, usuario, stockAnterior, stockNuevo
        );

        return movimientoRepository.save(movimiento);
    }

    @Transactional
    public MovimientoInventario registrarAjuste(String productoReferencia, Integer nuevoStock, String motivo, String usuario) {
        Producto producto = productoRepository.findByReferencia(productoReferencia)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con referencia: " + productoReferencia));

        int stockAnterior = producto.getCantidadStock();
        int cantidadAjuste = nuevoStock - stockAnterior;

        producto.setCantidadStock(nuevoStock);
        productoRepository.save(producto);

        MovimientoInventario movimiento = new MovimientoInventario(
                producto, "AJUSTE", cantidadAjuste, motivo, usuario, stockAnterior, nuevoStock
        );

        return movimientoRepository.save(movimiento);
    }

    // MÉTODOS DE CONSULTA EXISTENTES
    public List<MovimientoInventario> obtenerTodosLosMovimientos() {
        return movimientoRepository.findAllWithDocumentoCompra();
    }

    public List<MovimientoInventario> obtenerMovimientosPorProducto(String productoReferencia) {
        return movimientoRepository.findByProductoReferenciaOrderByFechaMovimientoDesc(productoReferencia);
    }

    public List<MovimientoInventario> obtenerMovimientosPorTipo(String tipoMovimiento) {
        if (!tipoMovimiento.equals("ENTRADA") && !tipoMovimiento.equals("SALIDA") && !tipoMovimiento.equals("AJUSTE")) {
            throw new RuntimeException("Tipo de movimiento inválido. Debe ser: ENTRADA, SALIDA o AJUSTE");
        }
        return movimientoRepository.findByTipoMovimientoOrderByFechaMovimientoDesc(tipoMovimiento);
    }

    public Optional<MovimientoInventario> obtenerMovimientoPorId(Long id) {
        return movimientoRepository.findByIdWithDocumentoCompra(id);
    }

    // NUEVOS MÉTODOS PARA TRABAJAR CON DOCUMENTOS COMPRA

    // Obtener movimientos agrupados por documento compra
    public List<MovimientoInventario> obtenerMovimientosConDocumentoCompra() {
        return movimientoRepository.findByDocumentoCompraIsNotNullOrderByFechaMovimientoDesc();
    }

    // Obtener movimientos manuales (sin documento compra)
    public List<MovimientoInventario> obtenerMovimientosManuales() {
        return movimientoRepository.findByDocumentoCompraIsNullOrderByFechaMovimientoDesc();
    }

    // Obtener movimientos de un documento compra específico
    public List<MovimientoInventario> obtenerMovimientosPorDocumentoCompra(Long documentoCompraId) {
        return movimientoRepository.findByDocumentoCompraIdWithDocumentoCompra(documentoCompraId);
    }

    // Obtener documento compra con sus movimientos
    public Optional<DocumentoCompra> obtenerDocumentoCompraConMovimientos(Long documentoCompraId) {
        return documentoCompraRepository.findByIdWithMovimientos(documentoCompraId);
    }

    // Obtener todos los documentos compra para reportes
    public List<DocumentoCompra> obtenerTodosDocumentosCompra() {
        return documentoCompraRepository.findAllWithMovimientos();
    }
}