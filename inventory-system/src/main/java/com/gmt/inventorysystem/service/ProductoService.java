package com.gmt.inventorysystem.service;

import com.gmt.inventorysystem.model.Producto;
import com.gmt.inventorysystem.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Crear un nuevo producto
    public Producto crearProducto(Producto producto) {
        // Validar que la referencia no exista
        if (productoRepository.existsByReferencia(producto.getReferencia())) {
            throw new RuntimeException("Ya existe un producto con la referencia: " + producto.getReferencia());
        }

        // Validar categoría
        validarCategoria(producto.getCategoria());

        // Validar proveedor
        validarProveedor(producto.getProveedor());

        return productoRepository.save(producto);
    }

    // Obtener todos los productos
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    // Obtener producto por ID
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    // Obtener producto por referencia
    public Optional<Producto> obtenerProductoPorReferencia(String referencia) {
        return productoRepository.findByReferencia(referencia);
    }

    // Actualizar producto por ID
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(productoExistente -> {
                    // Validar que la referencia no esté duplicada (si se cambia)
                    if (productoActualizado.getReferencia() != null &&
                            !productoActualizado.getReferencia().equals(productoExistente.getReferencia()) &&
                            productoRepository.existsByReferencia(productoActualizado.getReferencia())) {
                        throw new RuntimeException("Ya existe un producto con la referencia: " + productoActualizado.getReferencia());
                    }

                    // Validar categoría si se está actualizando
                    if (productoActualizado.getCategoria() != null) {
                        validarCategoria(productoActualizado.getCategoria());
                        productoExistente.setCategoria(productoActualizado.getCategoria());
                    }

                    // Validar proveedor si se está actualizando
                    if (productoActualizado.getProveedor() != null) {
                        validarProveedor(productoActualizado.getProveedor());
                        productoExistente.setProveedor(productoActualizado.getProveedor());
                    }

                    // Actualizar otros campos
                    if (productoActualizado.getReferencia() != null) {
                        productoExistente.setReferencia(productoActualizado.getReferencia());
                    }
                    if (productoActualizado.getNombre() != null) {
                        productoExistente.setNombre(productoActualizado.getNombre());
                    }
                    if (productoActualizado.getLote() != null) {
                        productoExistente.setLote(productoActualizado.getLote());
                    }
                    if (productoActualizado.getDescripcion() != null) {
                        productoExistente.setDescripcion(productoActualizado.getDescripcion());
                    }
                    if (productoActualizado.getCantidadStock() != null) {
                        productoExistente.setCantidadStock(productoActualizado.getCantidadStock());
                    }

                    return productoRepository.save(productoExistente);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    // Eliminar producto por ID
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    // Obtener productos por categoría
    public List<Producto> obtenerProductosPorCategoria(String categoria) {
        validarCategoria(categoria);
        return productoRepository.findByCategoria(categoria);
    }

    // Obtener productos por proveedor
    public List<Producto> obtenerProductosPorProveedor(String proveedor) {
        validarProveedor(proveedor);
        return productoRepository.findByProveedor(proveedor);
    }

    // Obtener productos con bajo stock
    public List<Producto> obtenerProductosBajoStock() {
        return productoRepository.findProductosBajoStockMinimo();
    }

    // Métodos de validación
    private void validarCategoria(String categoria) {
        if (categoria != null &&
                !categoria.equals("AZUCAR_BLANCA") &&
                !categoria.equals("AZUCAR_NATURAL")) {
            throw new RuntimeException("Categoría inválida. Debe ser: AZUCAR_BLANCA o AZUCAR_NATURAL");
        }
    }

    private void validarProveedor(String proveedor) {
        if (proveedor != null &&
                !proveedor.equals("ING_MAYAGUEZ") &&
                !proveedor.equals("ING_SAN_CARLOS")) {
            throw new RuntimeException("Proveedor inválido. Debe ser: ING_MAYAGUEZ o ING_SAN_CARLOS");
        }
    }
}