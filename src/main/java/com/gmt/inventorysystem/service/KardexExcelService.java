package com.gmt.inventorysystem.service;

import com.gmt.inventorysystem.dto.KardexDiarioDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class KardexExcelService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] exportarKardexDiario(List<List<KardexDiarioDTO>> kardexData, List<LocalDate> fechas) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Kardex Diario");

            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle numberStyle = createNumberStyle(workbook);
            CellStyle boldStyle = createBoldStyle(workbook);
            CellStyle totalStyle = createTotalStyle(workbook);

            int currentRow = 0;
            int currentCol = 0;

            // 1. CREAR CABECERAS DE FECHAS
            Row headerRow1 = sheet.createRow(currentRow++);
            Row headerRow2 = sheet.createRow(currentRow++);

            // Columnas fijas A y B
            createMergedHeader(headerRow1, headerRow2, 0, "Material", headerStyle, sheet);
            createMergedHeader(headerRow1, headerRow2, 1, "Texto breve de material", headerStyle, sheet);

            // Cabeceras dinámicas por fecha
            currentCol = 2;
            for (LocalDate fecha : fechas) {
                // Fusionar 5 columnas para cada fecha
                sheet.addMergedRegion(new CellRangeAddress(0, 0, currentCol, currentCol + 4));

                Cell fechaCell = headerRow1.createCell(currentCol);
                fechaCell.setCellValue(fecha.format(DATE_FORMATTER));
                fechaCell.setCellStyle(headerStyle);

                // Subcabeceras para cada fecha
                String[] subHeaders = {"DEVOLUCION", "ENTRADAS", "SALIDA", "EXISTENCIAS", "ESTIBAS"};
                for (int i = 0; i < 5; i++) {
                    Cell subHeaderCell = headerRow2.createCell(currentCol + i);
                    subHeaderCell.setCellValue(subHeaders[i]);
                    subHeaderCell.setCellStyle(headerStyle);
                }
                currentCol += 5;
            }

            // 2. LLENAR DATOS DE PRODUCTOS
            for (List<KardexDiarioDTO> productoKardex : kardexData) {
                if (productoKardex.isEmpty()) continue;

                Row dataRow = sheet.createRow(currentRow++);

                // Columnas fijas A y B
                KardexDiarioDTO primerDia = productoKardex.get(0);
                dataRow.createCell(0).setCellValue(primerDia.getReferencia());
                dataRow.createCell(1).setCellValue(primerDia.getNombre());

                // Aplicar estilo de número a las celdas de datos
                for (int i = 0; i < 2; i++) {
                    dataRow.getCell(i).setCellStyle(boldStyle);
                }

                // Datos por día
                currentCol = 2;
                for (KardexDiarioDTO dia : productoKardex) {
                    createNumberCell(dataRow, currentCol++, dia.getDevolucion(), numberStyle);
                    createNumberCell(dataRow, currentCol++, dia.getEntradas(), numberStyle);
                    createNumberCell(dataRow, currentCol++, dia.getSalida(), numberStyle);
                    createNumberCell(dataRow, currentCol++, dia.getExistencias(), numberStyle);
                    createNumberCell(dataRow, currentCol++, dia.getEstibas(), numberStyle);
                }
            }

            // 3. FILA "TOTAL GENERAL"
            if (!kardexData.isEmpty()) {
                Row totalRow = sheet.createRow(currentRow++);
                totalRow.createCell(0).setCellValue("Total general");
                totalRow.getCell(0).setCellStyle(totalStyle);

                // Calcular totales por día
                currentCol = 2;
                for (int dia = 0; dia < fechas.size(); dia++) {
                    Integer totalDevolucion = 0;
                    Integer totalEntradas = 0;
                    Integer totalSalida = 0;
                    Integer totalExistencias = 0;
                    Integer totalEstibas = 0;

                    for (List<KardexDiarioDTO> productoKardex : kardexData) {
                        if (dia < productoKardex.size()) {
                            KardexDiarioDTO diaProducto = productoKardex.get(dia);
                            totalDevolucion += diaProducto.getDevolucion() != null ? diaProducto.getDevolucion() : 0;
                            totalEntradas += diaProducto.getEntradas() != null ? diaProducto.getEntradas() : 0;
                            totalSalida += diaProducto.getSalida() != null ? diaProducto.getSalida() : 0;
                            totalExistencias += diaProducto.getExistencias() != null ? diaProducto.getExistencias() : 0;
                            totalEstibas += diaProducto.getEstibas() != null ? diaProducto.getEstibas() : 0;
                        }
                    }

                    createNumberCell(totalRow, currentCol++, totalDevolucion, totalStyle);
                    createNumberCell(totalRow, currentCol++, totalEntradas, totalStyle);
                    createNumberCell(totalRow, currentCol++, totalSalida, totalStyle);
                    createNumberCell(totalRow, currentCol++, totalExistencias, totalStyle);
                    createNumberCell(totalRow, currentCol++, totalEstibas, totalStyle);
                }
            }

            // Autoajustar columnas
            for (int i = 0; i < currentCol; i++) {
                sheet.autoSizeColumn(i);
            }

            // Congelar paneles (columnas A-B fijas)
            sheet.createFreezePane(2, 2);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    // MÉTODOS AUXILIARES
    private void createMergedHeader(Row headerRow1, Row headerRow2, int col, String text, CellStyle style, Sheet sheet) {
        sheet.addMergedRegion(new CellRangeAddress(0, 1, col, col));
        Cell cell = headerRow1.createCell(col);
        cell.setCellValue(text);
        cell.setCellStyle(style);
    }

    private void createNumberCell(Row row, int col, Integer value, CellStyle style) {
        Cell cell = row.createCell(col);
        if (value != null) {
            cell.setCellValue(value);
        } else {
            cell.setCellValue(0);
        }
        cell.setCellStyle(style);
    }

    // ESTILOS
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
        style.setVerticalAlignment(VerticalAlignment.CENTER);
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

    private CellStyle createBoldStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTotalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setDataFormat((short) 2); // Formato número
        return style;
    }
}