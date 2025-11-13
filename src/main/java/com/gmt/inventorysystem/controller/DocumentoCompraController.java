package com.gmt.inventorysystem.controller;

import com.gmt.inventorysystem.dto.DocumentoCompraDTO;
import com.gmt.inventorysystem.dto.MovimientoDTO;
import com.gmt.inventorysystem.model.DocumentoCompra;
import com.gmt.inventorysystem.model.MovimientoInventario;
import com.gmt.inventorysystem.service.DocumentoCompraService;
import com.gmt.inventorysystem.service.MovimientoInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documentos-compra")
public class DocumentoCompraController extends BaseController<DocumentoCompra, Long> {

    @Autowired
    private DocumentoCompraService documentoCompraService;

    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    // ✅ NUEVO MÉTODO: Crear documento compra
    @PostMapping
    public ResponseEntity<?> crearDocumentoCompra(@RequestBody DocumentoCompra documentoCompra) {
        try {
            DocumentoCompra nuevoDocumento = documentoCompraService.crearDocumentoCompra(documentoCompra);
            return ResponseEntity.ok(nuevoDocumento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Obtener todos los documentos compra como DTOs
    @GetMapping
    public List<DocumentoCompraDTO> obtenerTodosDocumentosCompra() {
        return documentoCompraService.obtenerTodosDocumentosCompraDTO();
    }

    // Obtener documento compra por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDocumentoCompraPorId(@PathVariable Long id) {
        Optional<DocumentoCompra> documento = documentoCompraService.obtenerDocumentoCompraConMovimientos(id);
        return handleFindById(documento, "DocumentoCompra");
    }

    // Obtener movimientos de un documento compra específico como DTOs
    @GetMapping("/{id}/movimientos")
    public List<MovimientoDTO> obtenerMovimientosPorDocumento(@PathVariable Long id) {
        return documentoCompraService.obtenerMovimientosPorDocumentoCompraDTO(id);
    }

    // Obtener documentos compra por rango de fechas
    @GetMapping("/por-fecha")
    public List<DocumentoCompra> obtenerDocumentosPorFecha(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        return documentoCompraService.obtenerDocumentosPorFecha(fechaInicio, fechaFin);
    }

    // Eliminar documento compra
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDocumentoCompra(@PathVariable Long id) {
        try {
            documentoCompraService.eliminarDocumentoCompra(id);
            return handleSuccess("Documento compra eliminado correctamente");
        } catch (RuntimeException e) {
            return handleDelete(e);
        }
    }

    // ✅ MÉTODOS ADICIONALES PARA BÚSQUEDA

    // Buscar documento por número de factura
    @GetMapping("/factura/{numeroFactura}")
    public ResponseEntity<?> obtenerDocumentoPorFactura(@PathVariable String numeroFactura) {
        Optional<DocumentoCompra> documento = documentoCompraService.obtenerDocumentoPorFactura(numeroFactura);
        return handleFindById(documento, "DocumentoCompra");
    }

    // Buscar documento por número de remisión
    @GetMapping("/remision/{numeroRemision}")
    public ResponseEntity<?> obtenerDocumentoPorRemision(@PathVariable String numeroRemision) {
        Optional<DocumentoCompra> documento = documentoCompraService.obtenerDocumentoPorRemision(numeroRemision);
        return handleFindById(documento, "DocumentoCompra");
    }
}