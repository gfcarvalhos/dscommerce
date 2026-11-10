package com.gabrielsilva.dscommerce.services;

import com.gabrielsilva.dscommerce.dto.CategoryDTO;
import com.gabrielsilva.dscommerce.entities.Category;
import com.gabrielsilva.dscommerce.repositories.CategoryRepository;
import com.gabrielsilva.dscommerce.tests.CategoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {

    @InjectMocks
    private CategoryServices service;

    @Mock
    private CategoryRepository repository;

    private Category category;

    @BeforeEach
    void setUp() throws Exception {
        category = CategoryFactory.createCategory();

        List<Category> list = new ArrayList<>();
        list.add(category);

        Mockito.when(repository.findAll()).thenReturn(list);
    }

    @Test
    public void findAllShouldReturnListCategoryDTO() {
        List<CategoryDTO> result = service.findAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(result.getFirst().getId(), category.getId());
        Assertions.assertEquals(result.getFirst().getName(), category.getName());
    }
}
