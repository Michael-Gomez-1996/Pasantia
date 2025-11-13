package com.gmt.inventorysystem.controller;

import com.gmt.inventorysystem.model.Cliente;
import com.gmt.inventorysystem.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController extends BaseController<Cliente, String> {

    @Autowired
    private ClienteService clienteService;

    // ✅ MÉTODOS FALTANTES - AGREGAR:

    // Obtener todos los clientes
    @GetMapping
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteService.obtenerTodosLosClientes();
    }

    // Obtener cliente por NIT
    @GetMapping("/{nit}")
    public ResponseEntity<?> obtenerClientePorNit(@PathVariable String nit) {
        Optional<Cliente> cliente = clienteService.obtenerClientePorNit(nit);
        return handleFindById(cliente, "Cliente");
    }

    // Crear cliente
    @PostMapping
    public ResponseEntity<?> crearCliente(@RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.crearCliente(cliente);
            return ResponseEntity.ok(nuevoCliente);
        } catch (RuntimeException e) {
            return handleCreate(e);
        }
    }

    // Actualizar cliente
    @PutMapping("/{nit}")
    public ResponseEntity<?> actualizarCliente(@PathVariable String nit, @RequestBody Cliente cliente) {
        try {
            Cliente clienteActualizado = clienteService.actualizarCliente(nit, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            return handleUpdate(e);
        }
    }

    // Eliminar cliente
    @DeleteMapping("/{nit}")
    public ResponseEntity<?> eliminarCliente(@PathVariable String nit) {
        try {
            clienteService.eliminarCliente(nit);
            return handleSuccess("Cliente eliminado correctamente");
        } catch (RuntimeException e) {
            return handleDelete(e);
        }
    }

    // Buscar clientes por nombre
    @GetMapping("/buscar")
    public List<Cliente> buscarClientesPorNombre(@RequestParam String nombre) {
        return clienteService.buscarClientesPorNombre(nombre);
    }

    // ✅ YA EXISTE - mantener:
    @PostMapping("/crear-rapido")
    public ResponseEntity<?> crearClienteRapido(@RequestBody ClienteRapidoRequest request) {
        try {
            Cliente cliente = new Cliente();
            cliente.setNit(request.getNit());
            cliente.setNombre(request.getNombre());

            Cliente nuevoCliente = clienteService.crearCliente(cliente);
            return ResponseEntity.ok(nuevoCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public static class ClienteRapidoRequest {
        private String nit;
        private String nombre;

        public String getNit() { return nit; }
        public void setNit(String nit) { this.nit = nit; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }
}