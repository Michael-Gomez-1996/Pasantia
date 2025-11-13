package com.gmt.inventorysystem.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public class BaseService<T, ID> {

    protected void validateRequiredField(Object field, String fieldName) {
        if (field == null || field.toString().trim().isEmpty()) {
            throw new RuntimeException(fieldName + " es requerido");
        }
    }

    protected void validateEntityExists(JpaRepository<T, ID> repository, ID id, String entityName) {
        if (!repository.existsById(id)) {
            throw new RuntimeException(entityName + " no encontrado con ID: " + id);
        }
    }

    protected T getEntityOrThrow(JpaRepository<T, ID> repository, ID id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(entityName + " no encontrado con ID: " + id));
    }
}