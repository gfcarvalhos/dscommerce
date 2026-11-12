package com.gabrielsilva.dscommerce.tests;

import com.gabrielsilva.dscommerce.dto.OrderDTO;
import com.gabrielsilva.dscommerce.entities.*;

import java.time.Instant;

public class OrderFactory {

    public static Order createOrder (User client) {
        Order order = new Order(client.getId(), Instant.now(), new Payment(), client, OrderStatus.WAITING_PAYMENT);
        Product product = ProductFactory.createProduct();
        OrderItem orderItem = new OrderItem(order, product, 10.0, 2);
        order.getItems().add(orderItem);

        return order;
    }

    public static OrderDTO createOrderDTO (User client) {
        Order order = createOrder(client);

        return new OrderDTO(order);
    }
}
