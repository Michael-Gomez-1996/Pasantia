package com.gmt.inventorysystem.service;

import com.gmt.inventorysystem.model.Cliente;
import com.gmt.inventorysystem.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService extends BaseService<Cliente, String> {

    @Autowired
    private ClienteRepository clienteRepository;

    // Crear un nuevo cliente
    public Cliente crearCliente(Cliente cliente) {
        // Validar que el NIT no exista
        if (clienteRepository.existsByNit(cliente.getNit())) {
            throw new RuntimeException("Ya existe un cliente con el NIT: " + cliente.getNit());
        }

        // Validar datos requeridos
        validateRequiredField(cliente.getNit(), "El NIT del cliente");
        validateRequiredField(cliente.getNombre(), "El nombre del cliente");

        return clienteRepository.save(cliente);
    }

    // Obtener cliente por NIT
    public Optional<Cliente> obtenerClientePorNit(String nit) {
        return clienteRepository.findByNit(nit);
    }

    // Verificar si existe un cliente
    public boolean existeCliente(String nit) {
        return clienteRepository.existsByNit(nit);
    }

    // Obtener todos los clientes
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAllByOrderByNombreAsc();
    }

    // Buscar clientes por nombre
    public List<Cliente> buscarClientesPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Actualizar cliente
    public Cliente actualizarCliente(String nit, Cliente clienteActualizado) {
        return clienteRepository.findByNit(nit)
                .map(clienteExistente -> {
                    // Actualizar nombre si se proporciona
                    if (clienteActualizado.getNombre() != null &&
                            !clienteActualizado.getNombre().trim().isEmpty()) {
                        clienteExistente.setNombre(clienteActualizado.getNombre());
                    }
                    return clienteRepository.save(clienteExistente);
                })
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con NIT: " + nit));
    }

    // Eliminar cliente
    public void eliminarCliente(String nit) {
        validateEntityExists(clienteRepository, nit, "Cliente");
        clienteRepository.deleteById(nit);
    }

    // Contar total de clientes
    public long contarClientes() {
        return clienteRepository.count();
    }
}