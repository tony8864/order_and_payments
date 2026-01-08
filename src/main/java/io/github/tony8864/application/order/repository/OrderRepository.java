package io.github.tony8864.application.order.repository;

import io.github.tony8864.domain.Order;
import io.github.tony8864.domain.vo.OrderId;

import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(OrderId orderId);
}
