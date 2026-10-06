package com.gabrielsilva.dscommerce.controllers;


import com.gabrielsilva.dscommerce.dto.CategoryDTO;
import com.gabrielsilva.dscommerce.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryServices service;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> dto = service.findAll();
        return ResponseEntity.ok(dto);
    }

}
