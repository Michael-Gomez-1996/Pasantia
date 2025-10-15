package com.gmt.inventorysystem.controller;

import com.gmt.inventorysystem.dto.ProcesamientoResponseDTO;
import com.gmt.inventorysystem.service.PdfProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/documentos")
public class DocumentoController {

    @Autowired
    private PdfProcessingService pdfProcessingService;

    // Mostrar formulario para subir documentos
    @GetMapping("/subir")
    public String mostrarFormularioSubida(Model model) {
        return "subir-documentos";
    }

    // Procesar documentos subidos
    @PostMapping("/procesar")
    public String procesarDocumentos(
            @RequestParam("archivoRemision") MultipartFile archivoRemision,
            @RequestParam("archivoFactura") MultipartFile archivoFactura,
            @RequestParam(value = "usuario", defaultValue = "Sistema") String usuario,
            RedirectAttributes redirectAttributes) {

        // Validar que se hayan subido archivos
        if (archivoRemision.isEmpty() || archivoFactura.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Debe subir ambos archivos (remisión y factura)");
            return "redirect:/documentos/subir";
        }

        // Validar que sean PDFs
        if (!archivoRemision.getContentType().equals("application/pdf") ||
                !archivoFactura.getContentType().equals("application/pdf")) {
            redirectAttributes.addFlashAttribute("error", "Ambos archivos deben ser PDF");
            return "redirect:/documentos/subir";
        }

        try {
            // Procesar documentos
            ProcesamientoResponseDTO resultado = pdfProcessingService.procesarDocumentos(
                    archivoRemision, archivoFactura, usuario);

            if (resultado.isSuccess()) {
                redirectAttributes.addFlashAttribute("success", resultado.getMessage());
                redirectAttributes.addFlashAttribute("detalles", resultado);
            } else {
                redirectAttributes.addFlashAttribute("error", resultado.getMessage());
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error procesando documentos: " + e.getMessage());
        }

        return "redirect:/documentos/subir";
    }
}