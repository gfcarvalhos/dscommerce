package com.gabrielsilva.dscommerce.services;

import com.gabrielsilva.dscommerce.dto.CategoryDTO;
import com.gabrielsilva.dscommerce.entities.Category;
import com.gabrielsilva.dscommerce.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServices {
    @Autowired
    CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> result = repository.findAll();
        return result.stream().map(CategoryDTO::new).toList();
    }
}
