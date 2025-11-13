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
public class ProductoController extends BaseController<Producto, String> {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.crearProducto(producto);
            return ResponseEntity.ok(nuevoProducto);
        } catch (RuntimeException e) {
            return handleCreate(e);
        }
    }

    @GetMapping
    public List<Producto> obtenerTodosLosProductos() {
        return productoService.obtenerTodosLosProductos();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable String id) {
        Optional<Producto> producto = productoService.obtenerProductoPorId(id);
        return handleFindById(producto, "Producto");
    }

    @GetMapping("/referencia/{referencia}")
    public ResponseEntity<?> obtenerProductoPorReferencia(@PathVariable String referencia) {
        Optional<Producto> producto = productoService.obtenerProductoPorReferencia(referencia);
        return handleFindById(producto, "Producto");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable String id, @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return handleUpdate(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable String id) {
        try {
            productoService.eliminarProducto(id);
            return handleSuccess("Producto eliminado correctamente");
        } catch (RuntimeException e) {
            return handleDelete(e);
        }
    }

    @GetMapping("/categoria/{categoria}")
    public List<Producto> obtenerProductosPorCategoria(@PathVariable String categoria) {
        return productoService.obtenerProductosPorCategoria(categoria);
    }

    @GetMapping("/proveedor/{proveedor}")
    public List<Producto> obtenerProductosPorProveedor(@PathVariable String proveedor) {
        return productoService.obtenerProductosPorProveedor(proveedor);
    }

    @GetMapping("/bajo-stock")
    public List<Producto> obtenerProductosBajoStock() {
        return productoService.obtenerProductosBajoStock();
    }
}