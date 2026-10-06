package com.gabrielsilva.dscommerce.dto;

import com.gabrielsilva.dscommerce.entities.OrderItem;

public class OrderItemDTO {

    private Long productId;
    private String name;
    private Double price;
    private Integer quantity;

    public OrderItemDTO(Integer quantity, Double price, String name, Long productId) {
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.productId = productId;
    }

    public OrderItemDTO(OrderItem entity) {
        quantity = entity.getQuantity();
        price = entity.getPrice();
        name = entity.getProduct().getName();
        productId = entity.getProduct().getId();
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getSubTotal() {
        return price * quantity;
    }
}
