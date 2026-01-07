package io.github.tony8864.domain.vo;

import java.util.UUID;

public record CustomerId(UUID id) {

    public CustomerId {
        if (id == null) {
            throw new IllegalArgumentException("Customer Id cannot be null");
        }
    }

    public static CustomerId newId() {
        return new CustomerId(UUID.randomUUID());
    }

    public static CustomerId from(UUID id) {
        return new CustomerId(id);
    }
}
