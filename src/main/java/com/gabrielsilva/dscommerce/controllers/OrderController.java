package com.gabrielsilva.dscommerce.controllers;


import com.gabrielsilva.dscommerce.dto.OrderDTO;
import com.gabrielsilva.dscommerce.dto.ProductDTO;
import com.gabrielsilva.dscommerce.dto.ProductMinDTO;
import com.gabrielsilva.dscommerce.services.OrderServices;
import com.gabrielsilva.dscommerce.services.ProductServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    private OrderServices service;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
        OrderDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

}
