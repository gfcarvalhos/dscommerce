package com.gabrielsilva.dscommerce.dto;

import com.gabrielsilva.dscommerce.entities.OrderItem;

public class OrderItemDTO {

    private Long productId;
    private String name;
    private Double price;
    private Integer quantity;
    private String imgUrl;

    public OrderItemDTO(){}

    public OrderItemDTO(Integer quantity, Double price, String name, Long productId, String imgUrl) {
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.productId = productId;
        this.imgUrl = imgUrl;
    }

    public OrderItemDTO(OrderItem entity) {
        quantity = entity.getQuantity();
        price = entity.getPrice();
        name = entity.getProduct().getName();
        productId = entity.getProduct().getId();
        imgUrl = entity.getProduct().getImgUrl();
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

    public String getImgUrl() {
        return imgUrl;
    }
}
