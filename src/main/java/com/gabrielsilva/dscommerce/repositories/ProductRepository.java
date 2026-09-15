package com.gabrielsilva.dscommerce.repositories;

import com.gabrielsilva.dscommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
