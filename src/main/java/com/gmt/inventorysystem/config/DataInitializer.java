package com.gmt.inventorysystem.config;

import com.gmt.inventorysystem.model.Usuario;
import com.gmt.inventorysystem.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear usuario ADMIN si no existe
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(Usuario.Rol.ADMIN);
            usuarioRepository.save(admin);
            System.out.println("Usuario ADMIN creado: admin / admin123");
        }

        // Crear usuario CLIENTE si no existe
        if (usuarioRepository.findByUsername("cliente").isEmpty()) {
            Usuario cliente = new Usuario();
            cliente.setUsername("cliente");
            cliente.setPassword(passwordEncoder.encode("cliente123"));
            cliente.setRol(Usuario.Rol.CLIENTE);
            usuarioRepository.save(cliente);
            System.out.println("Usuario CLIENTE creado: cliente / cliente123");
        }
    }
}