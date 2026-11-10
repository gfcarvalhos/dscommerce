package com.gabrielsilva.dscommerce.services;

import com.gabrielsilva.dscommerce.dto.ProductDTO;
import com.gabrielsilva.dscommerce.dto.ProductMinDTO;
import com.gabrielsilva.dscommerce.entities.Product;
import com.gabrielsilva.dscommerce.repositories.ProductRepository;
import com.gabrielsilva.dscommerce.services.exceptions.DatabaseException;
import com.gabrielsilva.dscommerce.services.exceptions.ResourceNotFoundException;
import com.gabrielsilva.dscommerce.tests.ProductFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductServices service;

    @Mock
    private ProductRepository repository;

    private long existingId, nonExistingId, dependentId;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        String productName = "PlayStation 5";
        product = ProductFactory.createProduct(productName);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists(){
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));

        ProductDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getName(), product.getName());
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist(){
        Mockito.when(repository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void findAllShouldReturnPagedProductMinDTO() {
        Pageable pageable = PageRequest.of(0, 12);
        String name = "PlayStation 5";
        Page<Product> page = new PageImpl<>(List.of(product));

        Mockito.when(repository.searchByName(any(), (Pageable) any())).thenReturn(page);

        Page<ProductMinDTO> result = service.findAll(name, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getSize());
        Assertions.assertEquals(name, result.iterator().next().getName());
    }

    @Test
    public void insertShouldReturnProductDTO() {
        Mockito.when(repository.save(any())).thenReturn(product);
        ProductDTO productDTO = new ProductDTO(product);

        ProductDTO result = service.insert(productDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(product.getId(), result.getId());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {
        Mockito.when(repository.save(any())).thenReturn(product);
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
        String newName = "PlayStation 10";

        ProductDTO productDTO = new ProductDTO(ProductFactory.createProduct(newName));

        ProductDTO result = service.update(existingId, productDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(product.getId(), result.getId());
        Assertions.assertEquals(newName, product.getName());
    }

    @Test
    public void updateShouldReturnEntityNotFoundExceptionWhenIdDoesNotExist() {
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
        String newName = "PlayStation 10";
        ProductDTO productDTO = new ProductDTO(ProductFactory.createProduct(newName));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, productDTO);
        });
    }
    
    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Mockito.when(repository.existsById(any())).thenReturn(true);
        Mockito.doNothing().when(repository).deleteById((existingId));


        Assertions.assertDoesNotThrow((()-> {
            service.delete(existingId);
        }));

    }

    @Test
    public void deleteShouldReturnDDatabaseExceptionWhenIdIsDependent() {
        Mockito.when(repository.existsById(any())).thenReturn(true);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
    }

    @Test
    public void deleteShouldReturnResourceNotFoundExceptionWhenIdIDoesNotExist() {
        Mockito.when(repository.existsById(any())).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }
}
