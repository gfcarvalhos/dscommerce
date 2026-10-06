package com.gabrielsilva.dscommerce.services;

import com.gabrielsilva.dscommerce.dto.OrderDTO;
import com.gabrielsilva.dscommerce.entities.Order;
import com.gabrielsilva.dscommerce.repositories.OrderRepository;
import com.gabrielsilva.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServices {

    @Autowired
    private OrderRepository repository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id){
        Order order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
        return new OrderDTO(order);
    }
}
