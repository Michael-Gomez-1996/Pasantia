package com.gmt.inventorysystem.service;

import com.gmt.inventorysystem.dto.ReporteMovimientoDTO;
import com.gmt.inventorysystem.dto.ReporteInventarioDTO;
import com.gmt.inventorysystem.model.DocumentoCompra;
import com.gmt.inventorysystem.model.MovimientoInventario;
import com.gmt.inventorysystem.model.Producto;
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

    public byte[] exportarReporteMovimientos(List<ReporteMovimientoDTO> movimientos) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte Movimientos");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle numberStyle = createNumberStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

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

            int rowNum = 1;
            for (ReporteMovimientoDTO movimiento : movimientos) {
                Row row = sheet.createRow(rowNum++);

                Cell fechaCell = row.createCell(0);
                if (movimiento.getFecha() != null) {
                    fechaCell.setCellValue(movimiento.getFecha().format(DATE_FORMATTER));
                }
                fechaCell.setCellStyle(dateStyle);

                row.createCell(1).setCellValue(movimiento.getNumeroRemision() != null ? movimiento.getNumeroRemision() : "");
                row.createCell(2).setCellValue(movimiento.getNumeroFactura() != null ? movimiento.getNumeroFactura() : "");

                Cell tipoCell = row.createCell(3);
                tipoCell.setCellValue(movimiento.getTipoMovimiento());
                if ("ENTRADA".equals(movimiento.getTipoMovimiento())) {
                    tipoCell.setCellStyle(createGreenStyle(workbook));
                } else if ("SALIDA".equals(movimiento.getTipoMovimiento())) {
                    tipoCell.setCellStyle(createRedStyle(workbook));
                }

                row.createCell(4).setCellValue(movimiento.getOrigen() != null ? movimiento.getOrigen() : "");
                row.createCell(5).setCellValue(movimiento.getDestino() != null ? movimiento.getDestino() : "");

                Cell unidadesCell = row.createCell(6);
                if (movimiento.getTotalUnidades() != null) {
                    unidadesCell.setCellValue(movimiento.getTotalUnidades());
                }
                unidadesCell.setCellStyle(numberStyle);

                Cell valorCell = row.createCell(7);
                if (movimiento.getValorTotal() != null) {
                    valorCell.setCellValue(movimiento.getValorTotal());
                }
                valorCell.setCellStyle(currencyStyle);

                Cell pesoCell = row.createCell(8);
                if (movimiento.getPesoTotalKilos() != null) {
                    pesoCell.setCellValue(movimiento.getPesoTotalKilos());
                }
                pesoCell.setCellStyle(numberStyle);

                row.createCell(9).setCellValue(movimiento.getConductorNombre() != null ? movimiento.getConductorNombre() : "");
                row.createCell(10).setCellValue(movimiento.getConductorCedula() != null ? movimiento.getConductorCedula() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            sheet.createFreezePane(0, 1);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportarReporteMovimientosCompleto(List<ReporteMovimientoDTO> movimientos, List<DocumentoCompra> documentosConIngresos) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            crearHojaMovimientosGeneral(workbook, movimientos);
            crearHojaIngresosDesglosados(workbook, documentosConIngresos);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void crearHojaMovimientosGeneral(Workbook workbook, List<ReporteMovimientoDTO> movimientos) {
        Sheet sheet = workbook.createSheet("Reporte Movimientos");

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dateStyle = createDateStyle(workbook);
        CellStyle numberStyle = createNumberStyle(workbook);
        CellStyle currencyStyle = createCurrencyStyle(workbook);

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

        int rowNum = 1;
        for (ReporteMovimientoDTO movimiento : movimientos) {
            Row row = sheet.createRow(rowNum++);

            Cell fechaCell = row.createCell(0);
            if (movimiento.getFecha() != null) {
                fechaCell.setCellValue(movimiento.getFecha().format(DATE_FORMATTER));
            }
            fechaCell.setCellStyle(dateStyle);

            row.createCell(1).setCellValue(movimiento.getNumeroRemision() != null ? movimiento.getNumeroRemision() : "");
            row.createCell(2).setCellValue(movimiento.getNumeroFactura() != null ? movimiento.getNumeroFactura() : "");

            Cell tipoCell = row.createCell(3);
            tipoCell.setCellValue(movimiento.getTipoMovimiento());
            if ("ENTRADA".equals(movimiento.getTipoMovimiento())) {
                tipoCell.setCellStyle(createGreenStyle(workbook));
            } else if ("SALIDA".equals(movimiento.getTipoMovimiento())) {
                tipoCell.setCellStyle(createRedStyle(workbook));
            }

            row.createCell(4).setCellValue(movimiento.getOrigen() != null ? movimiento.getOrigen() : "");
            row.createCell(5).setCellValue(movimiento.getDestino() != null ? movimiento.getDestino() : "");

            Cell unidadesCell = row.createCell(6);
            if (movimiento.getTotalUnidades() != null) {
                unidadesCell.setCellValue(movimiento.getTotalUnidades());
            }
            unidadesCell.setCellStyle(numberStyle);

            Cell valorCell = row.createCell(7);
            if (movimiento.getValorTotal() != null) {
                valorCell.setCellValue(movimiento.getValorTotal());
            }
            valorCell.setCellStyle(currencyStyle);

            Cell pesoCell = row.createCell(8);
            if (movimiento.getPesoTotalKilos() != null) {
                pesoCell.setCellValue(movimiento.getPesoTotalKilos());
            }
            pesoCell.setCellStyle(numberStyle);

            row.createCell(9).setCellValue(movimiento.getConductorNombre() != null ? movimiento.getConductorNombre() : "");
            row.createCell(10).setCellValue(movimiento.getConductorCedula() != null ? movimiento.getConductorCedula() : "");
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        sheet.createFreezePane(0, 1);
    }

    private void crearHojaIngresosDesglosados(Workbook workbook, List<DocumentoCompra> documentosConIngresos) {
        Sheet sheet = workbook.createSheet("Reporte de Ingresos");

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle numberStyle = createNumberStyle(workbook);
        CellStyle dateStyle = createDateStyle(workbook);

        String[] headers = {
                "FECHA INGRESO", "REMISIÓN", "MOVIMIENTO", "ORIGEN", "DESTINO",
                "CÓDIGO", "DESCRIPCIÓN", "CANTIDAD", "PESO EN KILOS"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (DocumentoCompra documento : documentosConIngresos) {
            if (documento.getMovimientos() != null && !documento.getMovimientos().isEmpty()) {
                for (MovimientoInventario movimiento : documento.getMovimientos()) {
                    if ("ENTRADA".equals(movimiento.getTipoMovimiento())) {
                        Row row = sheet.createRow(rowNum++);

                        // FECHA INGRESO (NUEVA COLUMNA)
                        Cell fechaCell = row.createCell(0);
                        if (movimiento.getFechaMovimiento() != null) {
                            fechaCell.setCellValue(movimiento.getFechaMovimiento().format(DATE_FORMATTER));
                        }
                        fechaCell.setCellStyle(dateStyle);

                        row.createCell(1).setCellValue(documento.getNumeroRemision() != null ? documento.getNumeroRemision() : "");
                        row.createCell(2).setCellValue("ENTRADA");

                        String origen = documento.getOrigenIngenio() != null ?
                                documento.getOrigenIngenio().replace("ING_", "") : "";
                        row.createCell(3).setCellValue(origen);

                        String destino = "BODEGA GMT CALLE 13";
                        row.createCell(4).setCellValue(destino);

                        Producto producto = movimiento.getProducto();
                        if (producto != null) {
                            row.createCell(5).setCellValue(producto.getReferencia());
                            row.createCell(6).setCellValue(producto.getNombre() != null ? producto.getNombre() : "");

                            Cell cantidadCell = row.createCell(7);
                            cantidadCell.setCellValue(movimiento.getCantidad());
                            cantidadCell.setCellStyle(numberStyle);

                            Cell pesoCell = row.createCell(8);
                            double pesoKilos = 0.0;
                            if (producto.getPesoPorPaca() != null && movimiento.getCantidad() != null) {
                                pesoKilos = producto.getPesoPorPaca() * movimiento.getCantidad();
                            }
                            pesoCell.setCellValue(pesoKilos);
                            pesoCell.setCellStyle(numberStyle);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        sheet.createFreezePane(0, 1);
    }

    public byte[] exportarReporteInventario(List<ReporteInventarioDTO> inventario) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte Inventario");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle numberStyle = createNumberStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle warningStyle = createWarningStyle(workbook);
            CellStyle averiaStyle = createAveriaStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);
            CellStyle criticalStyle = createCriticalStyle(workbook);

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

            int rowNum = 1;
            for (ReporteInventarioDTO producto : inventario) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(producto.getReferencia());
                row.createCell(1).setCellValue(producto.getNombre());
                row.createCell(2).setCellValue(producto.getLote() != null ? producto.getLote() : "");
                row.createCell(3).setCellValue(producto.getDescripcion() != null ? producto.getDescripcion() : "");
                row.createCell(4).setCellValue(producto.getCategoria());

                Cell stockBuenoCell = row.createCell(5);
                stockBuenoCell.setCellValue(producto.getCantidadStock());
                stockBuenoCell.setCellStyle(numberStyle);

                Cell stockAveriadoCell = row.createCell(6);
                stockAveriadoCell.setCellValue(producto.getCantidadAveriada());
                stockAveriadoCell.setCellStyle(averiaStyle);

                Cell inventarioTotalCell = row.createCell(7);
                inventarioTotalCell.setCellValue(producto.getInventarioTotal());
                inventarioTotalCell.setCellStyle(numberStyle);

                Cell minCell = row.createCell(8);
                minCell.setCellValue(producto.getStockMinimo());
                minCell.setCellStyle(numberStyle);

                Cell pesoCell = row.createCell(9);
                if (producto.getPesoPorPaca() != null) {
                    pesoCell.setCellValue(producto.getPesoPorPaca());
                }
                pesoCell.setCellStyle(numberStyle);

                Cell unidadesCell = row.createCell(10);
                unidadesCell.setCellValue(producto.getUnidadesPorPaca());
                unidadesCell.setCellStyle(numberStyle);

                Cell pacasEstibaCell = row.createCell(11);
                pacasEstibaCell.setCellValue(producto.getPacasPorEstiba());
                pacasEstibaCell.setCellStyle(numberStyle);

                Cell totalEstibasCell = row.createCell(12);
                totalEstibasCell.setCellValue(producto.getTotalEstibas());
                totalEstibasCell.setCellStyle(numberStyle);

                Cell pacasSueltasCell = row.createCell(13);
                pacasSueltasCell.setCellValue(producto.getPacasSueltas());
                pacasSueltasCell.setCellStyle(numberStyle);

                Cell pesoEstibaCell = row.createCell(14);
                pesoEstibaCell.setCellValue(producto.getPesoPorEstiba());
                pesoEstibaCell.setCellStyle(numberStyle);

                Cell pesoTotalEstibasCell = row.createCell(15);
                pesoTotalEstibasCell.setCellValue(producto.getPesoTotalEstibas());
                pesoTotalEstibasCell.setCellStyle(numberStyle);

                row.createCell(16).setCellValue(producto.getProveedor());
                row.createCell(17).setCellValue(producto.getUbicacion());

                Cell fechaCell = row.createCell(18);
                if (producto.getFechaCreacion() != null) {
                    fechaCell.setCellValue(producto.getFechaCreacion().format(DATE_ONLY_FORMATTER));
                }
                fechaCell.setCellStyle(dateStyle);

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

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            sheet.createFreezePane(0, 1);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private CellStyle createAveriaStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setDataFormat((short) 2);
        return style;
    }

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
        style.setDataFormat((short) 2);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat((short) 7);
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