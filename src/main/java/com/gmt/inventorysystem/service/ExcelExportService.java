package com.gmt.inventorysystem.service;

import com.gmt.inventorysystem.dto.ReporteMovimientoDTO;
import com.gmt.inventorysystem.dto.ReporteInventarioDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Exportar reporte de movimientos a Excel - ACTUALIZADO
    public byte[] exportarReporteMovimientos(List<ReporteMovimientoDTO> movimientos) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte Movimientos");

            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle numberStyle = createNumberStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            // Crear encabezados
            String[] headers = {
                    "Fecha", "Número Remisión", "Número Factura", "Tipo Movimiento",
                    "Origen", "Destino", "Total Unidades", "Valor Total",
                    "Peso Total (kg)", "Conductor", "Cédula Conductor"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar datos
            int rowNum = 1;
            for (ReporteMovimientoDTO movimiento : movimientos) {
                Row row = sheet.createRow(rowNum++);

                // Fecha
                Cell fechaCell = row.createCell(0);
                if (movimiento.getFecha() != null) {
                    fechaCell.setCellValue(movimiento.getFecha().format(DATE_FORMATTER));
                }
                fechaCell.setCellStyle(dateStyle);

                // Número Remisión
                row.createCell(1).setCellValue(movimiento.getNumeroRemision() != null ? movimiento.getNumeroRemision() : "");

                // Número Factura
                row.createCell(2).setCellValue(movimiento.getNumeroFactura() != null ? movimiento.getNumeroFactura() : "");

                // Tipo Movimiento
                Cell tipoCell = row.createCell(3);
                tipoCell.setCellValue(movimiento.getTipoMovimiento());
                // Colorear según tipo de movimiento
                if ("ENTRADA".equals(movimiento.getTipoMovimiento())) {
                    tipoCell.setCellStyle(createGreenStyle(workbook));
                } else if ("SALIDA".equals(movimiento.getTipoMovimiento())) {
                    tipoCell.setCellStyle(createRedStyle(workbook));
                }

                // Origen
                row.createCell(4).setCellValue(movimiento.getOrigen() != null ? movimiento.getOrigen() : "");

                // Destino
                row.createCell(5).setCellValue(movimiento.getDestino() != null ? movimiento.getDestino() : "");

                // Total Unidades
                Cell unidadesCell = row.createCell(6);
                if (movimiento.getTotalUnidades() != null) {
                    unidadesCell.setCellValue(movimiento.getTotalUnidades());
                }
                unidadesCell.setCellStyle(numberStyle);

                // Valor Total
                Cell valorCell = row.createCell(7);
                if (movimiento.getValorTotal() != null) {
                    valorCell.setCellValue(movimiento.getValorTotal());
                }
                valorCell.setCellStyle(currencyStyle);

                // Peso Total
                Cell pesoCell = row.createCell(8);
                if (movimiento.getPesoTotalKilos() != null) {
                    pesoCell.setCellValue(movimiento.getPesoTotalKilos());
                }
                pesoCell.setCellStyle(numberStyle);

                // Conductor
                row.createCell(9).setCellValue(movimiento.getConductorNombre() != null ? movimiento.getConductorNombre() : "");

                // Cédula Conductor
                row.createCell(10).setCellValue(movimiento.getConductorCedula() != null ? movimiento.getConductorCedula() : "");
            }

            // Autoajustar columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Congelar panel de encabezados
            sheet.createFreezePane(0, 1);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    // Exportar reporte de inventario a Excel - ✅ MODIFICADO PARA INCLUIR ESTIBAS
    public byte[] exportarReporteInventario(List<ReporteInventarioDTO> inventario) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte Inventario");

            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle numberStyle = createNumberStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle warningStyle = createWarningStyle(workbook);
            CellStyle averiaStyle = createAveriaStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);
            CellStyle criticalStyle = createCriticalStyle(workbook);

            // ✅ CREAR ENCABEZADOS MODIFICADOS CON ESTIBAS
            String[] headers = {
                    "Referencia", "Nombre", "Lote", "Descripción", "Categoría",
                    "Stock Bueno", "Stock Averiado", "Inventario Total",
                    "Stock Mínimo", "Peso por Paca (kg)",
                    "Unidades por Paca", "Pacas por Estiba",
                    "Total Estibas", "Pacas Sueltas", "Peso por Estiba (kg)", "Peso Total Estibas (kg)",
                    "Proveedor", "Ubicación", "Fecha Creación", "Estado"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar datos
            int rowNum = 1;
            for (ReporteInventarioDTO producto : inventario) {
                Row row = sheet.createRow(rowNum++);

                // Referencia
                row.createCell(0).setCellValue(producto.getReferencia());

                // Nombre
                row.createCell(1).setCellValue(producto.getNombre());

                // Lote
                row.createCell(2).setCellValue(producto.getLote() != null ? producto.getLote() : "");

                // Descripción
                row.createCell(3).setCellValue(producto.getDescripcion() != null ? producto.getDescripcion() : "");

                // Categoría
                row.createCell(4).setCellValue(producto.getCategoria());

                // Stock Bueno
                Cell stockBuenoCell = row.createCell(5);
                stockBuenoCell.setCellValue(producto.getCantidadStock());
                stockBuenoCell.setCellStyle(numberStyle);

                // Stock Averiado
                Cell stockAveriadoCell = row.createCell(6);
                stockAveriadoCell.setCellValue(producto.getCantidadAveriada());
                stockAveriadoCell.setCellStyle(averiaStyle);

                // Inventario Total
                Cell inventarioTotalCell = row.createCell(7);
                inventarioTotalCell.setCellValue(producto.getInventarioTotal());
                inventarioTotalCell.setCellStyle(numberStyle);

                // Stock Mínimo
                Cell minCell = row.createCell(8);
                minCell.setCellValue(producto.getStockMinimo());
                minCell.setCellStyle(numberStyle);

                // Peso por Paca
                Cell pesoCell = row.createCell(9);
                if (producto.getPesoPorPaca() != null) {
                    pesoCell.setCellValue(producto.getPesoPorPaca());
                }
                pesoCell.setCellStyle(numberStyle);

                // Unidades por Paca
                Cell unidadesCell = row.createCell(10);
                unidadesCell.setCellValue(producto.getUnidadesPorPaca());
                unidadesCell.setCellStyle(numberStyle);

                // ✅ Pacas por Estiba
                Cell pacasEstibaCell = row.createCell(11);
                pacasEstibaCell.setCellValue(producto.getPacasPorEstiba());
                pacasEstibaCell.setCellStyle(numberStyle);

                // ✅ Total Estibas
                Cell totalEstibasCell = row.createCell(12);
                totalEstibasCell.setCellValue(producto.getTotalEstibas());
                totalEstibasCell.setCellStyle(numberStyle);

                // ✅ Pacas Sueltas
                Cell pacasSueltasCell = row.createCell(13);
                pacasSueltasCell.setCellValue(producto.getPacasSueltas());
                pacasSueltasCell.setCellStyle(numberStyle);

                // ✅ Peso por Estiba
                Cell pesoEstibaCell = row.createCell(14);
                pesoEstibaCell.setCellValue(producto.getPesoPorEstiba());
                pesoEstibaCell.setCellStyle(numberStyle);

                // ✅ Peso Total Estibas
                Cell pesoTotalEstibasCell = row.createCell(15);
                pesoTotalEstibasCell.setCellValue(producto.getPesoTotalEstibas());
                pesoTotalEstibasCell.setCellStyle(numberStyle);

                // Proveedor
                row.createCell(16).setCellValue(producto.getProveedor());

                // Ubicación
                row.createCell(17).setCellValue(producto.getUbicacion());

                // Fecha Creación
                Cell fechaCell = row.createCell(18);
                if (producto.getFechaCreacion() != null) {
                    fechaCell.setCellValue(producto.getFechaCreacion().format(DATE_ONLY_FORMATTER));
                }
                fechaCell.setCellStyle(dateStyle);

                // Estado (ahora basado solo en stock bueno)
                Cell estadoCell = row.createCell(19);
                String estado = "NORMAL";
                if (producto.getCantidadStock() <= 20) {
                    estado = "CRÍTICO";
                    estadoCell.setCellStyle(criticalStyle);
                } else if (producto.getCantidadStock() <= producto.getStockMinimo()) {
                    estado = "BAJO STOCK";
                    estadoCell.setCellStyle(warningStyle);
                } else {
                    estado = "NORMAL";
                    estadoCell.setCellStyle(normalStyle);
                }
                estadoCell.setCellValue(estado);
            }

            // Autoajustar columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Congelar panel de encabezados
            sheet.createFreezePane(0, 1);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    // ✅ MÉTODO: Estilo para celdas de stock averiado
    private CellStyle createAveriaStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setDataFormat((short) 2); // Formato número
        return style;
    }

    // Métodos auxiliares para estilos
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy hh:mm"));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createNumberStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat((short) 2); // Formato número
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat((short) 7); // Formato moneda
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }

    private CellStyle createGreenStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createRedStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createWarningStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createCriticalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createNormalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}