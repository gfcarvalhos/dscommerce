package com.gabrielsilva.dscommerce.repositories;

import com.gabrielsilva.dscommerce.entities.OrderItem;
import com.gabrielsilva.dscommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
}
