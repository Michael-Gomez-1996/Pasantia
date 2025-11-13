package com.gmt.inventorysystem.service;

import com.gmt.inventorysystem.model.Producto;
import com.gmt.inventorysystem.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService extends BaseService<Producto, String> {

    @Autowired
    private ProductoRepository productoRepository;

    public Producto crearProducto(Producto producto) {
        if (productoRepository.existsByReferencia(producto.getReferencia())) {
            throw new RuntimeException("Ya existe un producto con la referencia: " + producto.getReferencia());
        }

        validarCategoria(producto.getCategoria());
        validarProveedor(producto.getProveedor());

        if (producto.getPacasPorEstiba() == null || producto.getPacasPorEstiba() <= 0) {
            producto.setPacasPorEstiba(100);
        }

        return productoRepository.save(producto);
    }

    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerProductoPorId(String id) {
        return productoRepository.findById(id);
    }

    public Optional<Producto> obtenerProductoPorReferencia(String referencia) {
        return productoRepository.findByReferencia(referencia);
    }

    public Producto actualizarProducto(String id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(productoExistente -> {
                    if (productoActualizado.getReferencia() != null &&
                            !productoActualizado.getReferencia().equals(productoExistente.getReferencia()) &&
                            productoRepository.existsByReferencia(productoActualizado.getReferencia())) {
                        throw new RuntimeException("Ya existe un producto con la referencia: " + productoActualizado.getReferencia());
                    }

                    if (productoActualizado.getCategoria() != null) {
                        validarCategoria(productoActualizado.getCategoria());
                        productoExistente.setCategoria(productoActualizado.getCategoria());
                    }

                    if (productoActualizado.getProveedor() != null) {
                        validarProveedor(productoActualizado.getProveedor());
                        productoExistente.setProveedor(productoActualizado.getProveedor());
                    }

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
                    if (productoActualizado.getPesoPorPaca() != null) {
                        productoExistente.setPesoPorPaca(productoActualizado.getPesoPorPaca());
                    }
                    if (productoActualizado.getUnidadesPorPaca() != null) {
                        productoExistente.setUnidadesPorPaca(productoActualizado.getUnidadesPorPaca());
                    }
                    if (productoActualizado.getPacasPorEstiba() != null) {
                        productoExistente.setPacasPorEstiba(productoActualizado.getPacasPorEstiba());
                    }
                    if (productoActualizado.getStockMinimo() != null) {
                        productoExistente.setStockMinimo(productoActualizado.getStockMinimo());
                    }

                    return productoRepository.save(productoExistente);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    public void eliminarProducto(String id) {
        validateEntityExists(productoRepository, id, "Producto");
        productoRepository.deleteById(id);
    }

    public List<Producto> obtenerProductosPorCategoria(String categoria) {
        validarCategoria(categoria);
        return productoRepository.findByCategoria(categoria);
    }

    public List<Producto> obtenerProductosPorProveedor(String proveedor) {
        validarProveedor(proveedor);
        return productoRepository.findByProveedor(proveedor);
    }

    public List<Producto> obtenerProductosBajoStock() {
        return productoRepository.findProductosBajoStockMinimo();
    }

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