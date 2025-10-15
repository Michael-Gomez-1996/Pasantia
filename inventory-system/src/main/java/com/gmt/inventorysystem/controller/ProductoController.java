package com.gmt.inventorysystem.controller;

import com.gmt.inventorysystem.model.Producto;
import com.gmt.inventorysystem.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Crear nuevo producto
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.crearProducto(producto);
            return ResponseEntity.ok(nuevoProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Obtener todos los productos
    @GetMapping
    public List<Producto> obtenerTodosLosProductos() {
        return productoService.obtenerTodosLosProductos();
    }

    // Obtener producto por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable Long id) {
        Optional<Producto> producto = productoService.obtenerProductoPorId(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener producto por referencia
    @GetMapping("/referencia/{referencia}")
    public ResponseEntity<?> obtenerProductoPorReferencia(@PathVariable String referencia) {
        Optional<Producto> producto = productoService.obtenerProductoPorReferencia(referencia);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar producto por ID
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliminar producto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok().body("Producto eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Obtener productos por categoría
    @GetMapping("/categoria/{categoria}")
    public List<Producto> obtenerProductosPorCategoria(@PathVariable String categoria) {
        return productoService.obtenerProductosPorCategoria(categoria);
    }

    // Obtener productos por proveedor
    @GetMapping("/proveedor/{proveedor}")
    public List<Producto> obtenerProductosPorProveedor(@PathVariable String proveedor) {
        return productoService.obtenerProductosPorProveedor(proveedor);
    }

    // Obtener productos con bajo stock
    @GetMapping("/bajo-stock")
    public List<Producto> obtenerProductosBajoStock() {
        return productoService.obtenerProductosBajoStock();
    }
}