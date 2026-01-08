package io.github.tony8864.infrastructure.inmemory;

import io.github.tony8864.application.order.repository.OrderRepository;
import io.github.tony8864.domain.Order;
import io.github.tony8864.domain.vo.OrderId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryOrderRepository implements OrderRepository {

    private final Map<OrderId, Order> orders;

    public InMemoryOrderRepository() {
        orders = new HashMap<>();
    }

    @Override
    public void save(Order order) {
        orders.put(order.id(), order);
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return Optional.of(orders.get(orderId));
    }
}
