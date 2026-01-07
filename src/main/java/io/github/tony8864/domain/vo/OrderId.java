package io.github.tony8864.domain.vo;

import java.util.UUID;

public record OrderId(UUID id) {

    public OrderId {
        if (id == null) {
            throw new IllegalArgumentException("Order Id cannot be null");
        }
    }

    public static OrderId newId() {
        return new OrderId(UUID.randomUUID());
    }
}
