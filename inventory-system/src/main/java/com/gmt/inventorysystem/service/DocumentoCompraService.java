package com.gmt.inventorysystem.service;

import com.gmt.inventorysystem.dto.DocumentoCompraDTO;
import com.gmt.inventorysystem.dto.MovimientoDTO;
import com.gmt.inventorysystem.model.DocumentoCompra;
import com.gmt.inventorysystem.model.MovimientoInventario;
import com.gmt.inventorysystem.model.Producto;
import com.gmt.inventorysystem.repository.DocumentoCompraRepository;
import com.gmt.inventorysystem.repository.MovimientoInventarioRepository;
import com.gmt.inventorysystem.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentoCompraService {

    @Autowired
    private DocumentoCompraRepository documentoCompraRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Crear un nuevo documento compra
    @Transactional
    public DocumentoCompra crearDocumentoCompra(DocumentoCompra documentoCompra) {
        // Validar que no existan documentos con los mismos números
        if (documentoCompraRepository.existsByNumeroFactura(documentoCompra.getNumeroFactura())) {
            throw new RuntimeException("Ya existe un documento con el número de factura: " + documentoCompra.getNumeroFactura());
        }

        if (documentoCompraRepository.existsByNumeroRemision(documentoCompra.getNumeroRemision())) {
            throw new RuntimeException("Ya existe un documento con el número de remisión: " + documentoCompra.getNumeroRemision());
        }

        return documentoCompraRepository.save(documentoCompra);
    }

    // Obtener todos los documentos compra
    public List<DocumentoCompra> obtenerTodosDocumentosCompra() {
        return documentoCompraRepository.findAllWithMovimientos();
    }

    // NUEVO MÉTODO: Obtener todos los documentos compra como DTOs
    public List<DocumentoCompraDTO> obtenerTodosDocumentosCompraDTO() {
        List<DocumentoCompra> documentos = documentoCompraRepository.findAllWithMovimientos();
        return documentos.stream()
                .map(DocumentoCompraDTO::new)
                .collect(Collectors.toList());
    }

    // NUEVO MÉTODO: Obtener movimientos por documento compra como DTOs
    public List<MovimientoDTO> obtenerMovimientosPorDocumentoCompraDTO(Long documentoCompraId) {
        List<MovimientoInventario> movimientos = movimientoInventarioRepository.findByDocumentoCompraIdWithDocumentoCompra(documentoCompraId);
        return movimientos.stream()
                .map(MovimientoDTO::new)
                .collect(Collectors.toList());
    }

    // Obtener documento compra por ID con movimientos
    public Optional<DocumentoCompra> obtenerDocumentoCompraConMovimientos(Long id) {
        return documentoCompraRepository.findByIdWithMovimientos(id);
    }

    // Obtener documentos por rango de fechas
    public List<DocumentoCompra> obtenerDocumentosPorFecha(String fechaInicioStr, String fechaFinStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr + " 00:00:00", formatter);
        LocalDateTime fechaFin = LocalDateTime.parse(fechaFinStr + " 23:59:59", formatter);

        return documentoCompraRepository.findByFechaFacturacionBetween(fechaInicio, fechaFin);
    }

    // Eliminar documento compra
    @Transactional
    public void eliminarDocumentoCompra(Long id) {
        Optional<DocumentoCompra> documento = documentoCompraRepository.findById(id);
        if (documento.isPresent()) {
            // Primero eliminar los movimientos asociados
            List<MovimientoInventario> movimientos = movimientoInventarioRepository.findByDocumentoCompraIdOrderByFechaMovimientoDesc(id);
            for (MovimientoInventario movimiento : movimientos) {
                // Revertir el stock de los productos
                Producto producto = movimiento.getProducto();
                producto.setCantidadStock(producto.getCantidadStock() + movimiento.getCantidad());
                productoRepository.save(producto);

                // Eliminar el movimiento
                movimientoInventarioRepository.delete(movimiento);
            }

            // Luego eliminar el documento
            documentoCompraRepository.deleteById(id);
        } else {
            throw new RuntimeException("Documento compra no encontrado con ID: " + id);
        }
    }

    // Buscar documento por número de factura
    public Optional<DocumentoCompra> obtenerDocumentoPorFactura(String numeroFactura) {
        return documentoCompraRepository.findByNumeroFactura(numeroFactura);
    }

    // Buscar documento por número de remisión
    public Optional<DocumentoCompra> obtenerDocumentoPorRemision(String numeroRemision) {
        return documentoCompraRepository.findByNumeroRemision(numeroRemision);
    }
}