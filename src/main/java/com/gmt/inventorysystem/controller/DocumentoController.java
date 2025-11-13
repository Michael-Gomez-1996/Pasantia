package com.gmt.inventorysystem.controller;

import com.gmt.inventorysystem.dto.ProcesamientoResponseDTO;
import com.gmt.inventorysystem.service.PdfProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public String mostrarFormularioSubida(Model model,
                                          @RequestParam(value = "exito", required = false) String exito,
                                          @RequestParam(value = "error", required = false) String error) {

        if (exito != null) {
            model.addAttribute("success", exito);
        }
        if (error != null) {
            model.addAttribute("error", error);
        }

        return "subir-documentos";
    }

    // Endpoint para previsualizar datos extraídos (AJAX)
    @PostMapping("/previsualizar")
    @ResponseBody
    public ResponseEntity<ProcesamientoResponseDTO> previsualizarDocumentos(
            @RequestParam("archivoRemision") MultipartFile archivoRemision,
            @RequestParam("archivoFactura") MultipartFile archivoFactura) {

        ProcesamientoResponseDTO resultado = pdfProcessingService.extraerDatosPrueba(archivoRemision, archivoFactura);
        return ResponseEntity.ok(resultado);
    }

    // Procesar documentos subidos - ACTUALIZADO PARA DATOS MANUALES
    @PostMapping("/procesar")
    @ResponseBody
    public ResponseEntity<ProcesamientoResponseDTO> procesarDocumentos(
            @RequestParam("archivoRemision") MultipartFile archivoRemision,
            @RequestParam("archivoFactura") MultipartFile archivoFactura,
            @RequestParam(value = "usuario", defaultValue = "Sistema") String usuario,
            @RequestParam(value = "pesoTotal", required = false) Double pesoTotal,
            @RequestParam(value = "valorTotal", required = false) Double valorTotal,
            @RequestParam(value = "placaVehiculo", required = false) String placaVehiculo) {

        // Validaciones básicas
        if (archivoRemision.isEmpty() || archivoFactura.isEmpty()) {
            return ResponseEntity.badRequest().body(new ProcesamientoResponseDTO(false, "Debe subir ambos archivos (remisión y factura)"));
        }

        if (!archivoRemision.getContentType().equals("application/pdf") ||
                !archivoFactura.getContentType().equals("application/pdf")) {
            return ResponseEntity.badRequest().body(new ProcesamientoResponseDTO(false, "Ambos archivos deben ser PDF"));
        }

        try {
            // Procesar documentos con datos manuales
            ProcesamientoResponseDTO resultado = pdfProcessingService.procesarDocumentos(
                    archivoRemision, archivoFactura, usuario, pesoTotal, valorTotal, placaVehiculo);

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ProcesamientoResponseDTO(false, "Error procesando documentos: " + e.getMessage()));
        }
    }
}