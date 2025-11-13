package com.gmt.inventorysystem.controller;

import com.gmt.inventorysystem.service.KardexExcelService;
import com.gmt.inventorysystem.service.KardexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.gmt.inventorysystem.dto.KardexDiarioDTO;
import java.util.List;

@RestController
@RequestMapping("/api/kardex")
public class KardexController {

    @Autowired
    private KardexService kardexService;

    @Autowired
    private KardexExcelService kardexExcelService;

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportarKardexExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        try {
            // Generar datos del kardex
            List<List<KardexDiarioDTO>> kardexData = kardexService.generarKardexDiario(fechaInicio, fechaFin);
            List<LocalDate> fechas = kardexService.obtenerFechasRango(fechaInicio, fechaFin);

            // Generar Excel con el servicio especializado
            byte[] excelBytes = kardexExcelService.exportarKardexDiario(kardexData, fechas);

            // Configurar respuesta
            String filename = String.format("kardex_%s_a_%s.xlsx",
                    fechaInicio.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                    fechaFin.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelBytes);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}