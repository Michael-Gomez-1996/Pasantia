package com.gmt.inventorysystem.service;

import com.gmt.inventorysystem.dto.AveriaDTO;
import com.gmt.inventorysystem.model.Averia;
import com.gmt.inventorysystem.model.Producto;
import com.gmt.inventorysystem.repository.AveriaRepository;
import com.gmt.inventorysystem.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AveriaService {

    @Autowired
    private AveriaRepository averiaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public Averia registrarAveria(Averia averia) {
        // ✅ CORRECCIÓN: Obtener el producto COMPLETO de la base de datos usando la referencia
        Optional<Producto> productoOpt = productoRepository.findByReferencia(averia.getProducto().getReferencia());
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("Producto con referencia " + averia.getProducto().getReferencia() + " no encontrado");
        }

        Producto producto = productoOpt.get();
        String tipoAveria = averia.getTipoAveria();

        switch (tipoAveria) {
            case "ENTRADA":
                if (producto.getCantidadStock() < averia.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para registrar avería de entrada. Stock actual: " +
                            producto.getCantidadStock() + ", avería: " + averia.getCantidad());
                }
                producto.setCantidadStock(producto.getCantidadStock() - averia.getCantidad());
                producto.setCantidadAveriada(producto.getCantidadAveriada() + averia.getCantidad());
                break;

            case "OPERACION":
                if (producto.getCantidadStock() < averia.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para registrar avería de operación. Stock actual: " +
                            producto.getCantidadStock() + ", avería: " + averia.getCantidad());
                }
                producto.setCantidadStock(producto.getCantidadStock() - averia.getCantidad());
                producto.setCantidadAveriada(producto.getCantidadAveriada() + averia.getCantidad());
                break;

            case "DEVOLUCION":
                // ✅ SOLO aumenta stock averiado (no afecta stock bueno)
                producto.setCantidadAveriada(producto.getCantidadAveriada() + averia.getCantidad());
                break;

            default:
                throw new RuntimeException("Tipo de avería no válido: " + tipoAveria);
        }

        productoRepository.save(producto);

        // ✅ CORRECCIÓN: Asignar el producto COMPLETO de la base de datos
        averia.setProducto(producto);

        if (averia.getFechaDeteccion() == null) {
            averia.setFechaDeteccion(LocalDateTime.now());
        }

        return averiaRepository.save(averia);
    }

    public void eliminarAveria(Long id) {
        Averia averia = averiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avería no encontrada con ID: " + id));

        Producto producto = averia.getProducto();
        String tipoAveria = averia.getTipoAveria();

        switch (tipoAveria) {
            case "ENTRADA":
            case "OPERACION":
                producto.setCantidadStock(producto.getCantidadStock() + averia.getCantidad());
                producto.setCantidadAveriada(producto.getCantidadAveriada() - averia.getCantidad());
                break;

            case "DEVOLUCION":
                producto.setCantidadAveriada(producto.getCantidadAveriada() - averia.getCantidad());
                break;
        }

        productoRepository.save(producto);
        averiaRepository.deleteById(id);
    }

    public Integer obtenerTotalUnidadesAveriadas() {
        List<Producto> productos = productoRepository.findAll();
        return productos.stream()
                .mapToInt(Producto::getCantidadAveriada)
                .sum();
    }

    public List<AveriaDTO> obtenerTodasLasAverias() {
        List<Averia> averias = averiaRepository.findAllWithProducto();
        return averias.stream()
                .map(AveriaDTO::new)
                .collect(Collectors.toList());
    }

    public List<AveriaDTO> obtenerAveriasPorProducto(String productoReferencia) {
        List<Averia> averias = averiaRepository.findByProductoReferenciaOrderByFechaRegistroDesc(productoReferencia);
        return averias.stream()
                .map(AveriaDTO::new)
                .collect(Collectors.toList());
    }

    public List<AveriaDTO> obtenerAveriasPorTipo(String tipoAveria) {
        List<Averia> averias = averiaRepository.findByTipoAveriaOrderByFechaRegistroDesc(tipoAveria);
        return averias.stream()
                .map(AveriaDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<Averia> obtenerAveriaPorId(Long id) {
        return averiaRepository.findById(id);
    }

    public long contarAverias() {
        return averiaRepository.count();
    }

    public List<AveriaDTO> obtenerAveriasPorRemision(String numeroRemision) {
        List<Averia> averias = averiaRepository.findByNumeroRemision(numeroRemision);
        return averias.stream()
                .map(AveriaDTO::new)
                .collect(Collectors.toList());
    }
}