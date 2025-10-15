package com.gmt.inventorysystem.controller;

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
public class DocumentoCompraController {

    @Autowired
    private DocumentoCompraService documentoCompraService;

    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    // Obtener todos los documentos compra
    @GetMapping
    public List<DocumentoCompra> obtenerTodosDocumentosCompra() {
        return documentoCompraService.obtenerTodosDocumentosCompra();
    }

    // Obtener documento compra por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDocumentoCompraPorId(@PathVariable Long id) {
        Optional<DocumentoCompra> documento = documentoCompraService.obtenerDocumentoCompraConMovimientos(id);
        if (documento.isPresent()) {
            return ResponseEntity.ok(documento.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener movimientos de un documento compra específico
    @GetMapping("/{id}/movimientos")
    public List<MovimientoInventario> obtenerMovimientosPorDocumento(@PathVariable Long id) {
        return movimientoInventarioService.obtenerMovimientosPorDocumentoCompra(id);
    }

    // Obtener documentos compra por rango de fechas
    @GetMapping("/por-fecha")
    public List<DocumentoCompra> obtenerDocumentosPorFecha(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        return documentoCompraService.obtenerDocumentosPorFecha(fechaInicio, fechaFin);
    }

    // Eliminar documento compra (solo si es necesario)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDocumentoCompra(@PathVariable Long id) {
        try {
            documentoCompraService.eliminarDocumentoCompra(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}