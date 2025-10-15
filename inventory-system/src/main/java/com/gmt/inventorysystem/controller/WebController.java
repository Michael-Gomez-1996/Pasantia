package com.gmt.inventorysystem.controller;

import com.gmt.inventorysystem.model.Producto;
import com.gmt.inventorysystem.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ProductoService productoService;

    // Página de inicio - Dashboard
    @GetMapping("/")
    public String index(Model model) {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        List<Producto> productosBajoStock = productoService.obtenerProductosBajoStock();

        model.addAttribute("productos", productos);
        model.addAttribute("productosBajoStock", productosBajoStock);
        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("productosBajoStockCount", productosBajoStock.size());

        return "index";
    }

    // Página de login
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("message", "Sesión cerrada correctamente");
        }
        return "login";
    }

    // Página de gestión de productos
    @GetMapping("/productos")
    public String gestionProductos(Model model) {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        model.addAttribute("productos", productos);
        model.addAttribute("nuevoProducto", new Producto());
        model.addAttribute("categorias", new String[]{"AZUCAR_BLANCA", "AZUCAR_NATURAL"});
        model.addAttribute("proveedores", new String[]{"ING_MAYAGUEZ", "ING_SAN_CARLOS"});

        return "productos";
    }

    // Crear nuevo producto desde el formulario
    @PostMapping("/productos/crear")
    public String crearProducto(@ModelAttribute Producto producto) {
        try {
            productoService.crearProducto(producto);
            return "redirect:/productos?exito=Producto creado correctamente";
        } catch (Exception e) {
            return "redirect:/productos?error=" + e.getMessage();
        }
    }

    // Página de movimientos de inventario
    @GetMapping("/movimientos")
    public String movimientosInventario(Model model) {
        return "movimientos";
    }

    // Página de reportes
    @GetMapping("/reportes")
    public String reportes(Model model) {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        List<Producto> productosBajoStock = productoService.obtenerProductosBajoStock();

        model.addAttribute("productos", productos);
        model.addAttribute("productosBajoStock", productosBajoStock);
        model.addAttribute("totalProductos", productos.size());

        return "reportes";
    }
    // Endpoint de prueba simple
    @GetMapping("/test")
    public String testPage(Model model) {
        model.addAttribute("mensaje", "✅ Thymeleaf funcionando correctamente");
        return "test";
    }
}