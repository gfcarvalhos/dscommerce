package com.gabrielsilva.dscommerce.tests;

import com.gabrielsilva.dscommerce.dto.ProductDTO;
import com.gabrielsilva.dscommerce.entities.Category;
import com.gabrielsilva.dscommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct() {
        Category category = CategoryFactory.createCategory();
        Product product = new Product("http://www.google.com", 3999.0, "description description", "Google", 1L);
        product.getCategories().add(category);

        return product;
    }

    public static Product createProduct(String name){
        Product product = createProduct();
        product.setName(name);

        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();

        return new ProductDTO(product);
    }

}
