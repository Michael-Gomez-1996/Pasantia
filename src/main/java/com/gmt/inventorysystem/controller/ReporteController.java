package com.gmt.inventorysystem.controller;

import com.gmt.inventorysystem.dto.ReporteMovimientoDTO;
import com.gmt.inventorysystem.dto.ReporteInventarioDTO;
import com.gmt.inventorysystem.service.ExcelExportService;
import com.gmt.inventorysystem.service.ReporteService;
import com.gmt.inventorysystem.model.DocumentoCompra;
import com.gmt.inventorysystem.service.DocumentoCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private DocumentoCompraService documentoCompraService;

    @GetMapping("/movimientos-excel")
    public ResponseEntity<List<ReporteMovimientoDTO>> generarReporteMovimientosExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        try {
            List<ReporteMovimientoDTO> reporte = reporteService.generarReporteMovimientosExcel(fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/inventario-excel")
    public ResponseEntity<List<ReporteInventarioDTO>> generarReporteInventarioExcel() {
        try {
            List<ReporteInventarioDTO> reporte = reporteService.generarReporteInventarioExcel();
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/descargar-movimientos")
    public ResponseEntity<byte[]> descargarReporteMovimientosExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        try {
            List<ReporteMovimientoDTO> reporte = reporteService.generarReporteMovimientosExcel(fechaInicio, fechaFin);
            List<DocumentoCompra> documentosConIngresos = documentoCompraService.obtenerTodosDocumentosCompra();

            byte[] excelBytes = excelExportService.exportarReporteMovimientosCompleto(reporte, documentosConIngresos);

            String filename = "reporte_movimientos_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelBytes);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/descargar-inventario")
    public ResponseEntity<byte[]> descargarReporteInventarioExcel() {
        try {
            List<ReporteInventarioDTO> reporte = reporteService.generarReporteInventarioExcel();
            byte[] excelBytes = excelExportService.exportarReporteInventario(reporte);

            String filename = "reporte_inventario_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelBytes);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}