package com.gmt.inventorysystem.controller;

import com.gmt.inventorysystem.model.Producto;
import com.gmt.inventorysystem.service.AveriaService;
import com.gmt.inventorysystem.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private AveriaService averiaService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index(Model model) {
        // Obtener información de autenticación
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("rol", auth.getAuthorities().iterator().next().getAuthority());

        // Lógica existente del dashboard
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        List<Producto> productosBajoStock = productoService.obtenerProductosBajoStock();

        int inventarioTotal = productos.stream()
                .mapToInt(Producto::getInventarioTotal)
                .sum();

        int stockBuenEstado = productos.stream()
                .mapToInt(Producto::getCantidadStock)
                .sum();

        int stockAveriado = productos.stream()
                .mapToInt(Producto::getCantidadAveriada)
                .sum();

        model.addAttribute("productos", productos);
        model.addAttribute("productosBajoStock", productosBajoStock);
        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("productosBajoStockCount", productosBajoStock.size());
        model.addAttribute("inventarioTotal", inventarioTotal);
        model.addAttribute("stockBuenEstado", stockBuenEstado);
        model.addAttribute("stockAveriado", stockAveriado);

        return "index";
    }

    @GetMapping("/productos")
    public String gestionProductos(Model model) {
        // Obtener información de autenticación
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("rol", auth.getAuthorities().iterator().next().getAuthority());

        // Lógica existente de productos
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        model.addAttribute("productos", productos);
        model.addAttribute("nuevoProducto", new Producto());
        model.addAttribute("categorias", new String[]{"AZUCAR_BLANCA", "AZUCAR_NATURAL"});
        model.addAttribute("proveedores", new String[]{"ING_MAYAGUEZ", "ING_SAN_CARLOS"});

        return "productos";
    }

    @PostMapping("/productos/crear")
    public String crearProducto(@ModelAttribute Producto producto) {
        try {
            productoService.crearProducto(producto);
            return "redirect:/productos?exito=Producto creado correctamente";
        } catch (Exception e) {
            return "redirect:/productos?error=" + e.getMessage();
        }
    }

    @GetMapping("/movimientos")
    public String movimientosInventario(Model model) {
        // Obtener información de autenticación
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("rol", auth.getAuthorities().iterator().next().getAuthority());

        return "movimientos";
    }

    @GetMapping("/reportes")
    public String reportes(Model model) {
        // Obtener información de autenticación
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("rol", auth.getAuthorities().iterator().next().getAuthority());

        try {
            List<Producto> productos = productoService.obtenerTodosLosProductos();
            List<Producto> productosBajoStock = productoService.obtenerProductosBajoStock();

            model.addAttribute("productos", productos);
            model.addAttribute("productosBajoStock", productosBajoStock);
            model.addAttribute("totalProductos", productos.size());

            return "reportes";
        } catch (Exception e) {
            return "redirect:/?error=Error cargando página de reportes: " + e.getMessage();
        }
    }
}