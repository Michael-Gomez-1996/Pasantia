package com.gmt.inventorysystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

public class BaseController<T, ID> {

    protected ResponseEntity<?> handleFindById(Optional<T> entity, String entityName) {
        if (entity.isPresent()) {
            return ResponseEntity.ok(entity.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    protected ResponseEntity<?> handleCreate(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    protected ResponseEntity<?> handleUpdate(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    protected ResponseEntity<?> handleDelete(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    protected ResponseEntity<?> handleSuccess(String message) {
        return ResponseEntity.ok().body(message);
    }
}