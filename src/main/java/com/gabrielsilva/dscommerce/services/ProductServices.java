package com.gabrielsilva.dscommerce.services;

import com.gabrielsilva.dscommerce.dto.ProductDTO;
import com.gabrielsilva.dscommerce.entities.Product;
import com.gabrielsilva.dscommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServices {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Optional<Product> result = repository.findById(id);
        Product product = result.get();
        return new ProductDTO(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll(){
        List<Product> result = repository.findAll();
        return result.stream().map(ProductDTO::new).toList();
    }
}
