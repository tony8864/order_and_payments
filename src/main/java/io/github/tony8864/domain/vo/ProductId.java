package io.github.tony8864.domain.vo;

import java.util.UUID;

public record ProductId(UUID id) {

    public ProductId {
        if (id == null) {
            throw new IllegalArgumentException("Product Id cannot be null");
        }
    }

    public static ProductId newId() {
        return new ProductId(UUID.randomUUID());
    }

    public static ProductId of(String id) {
        return new ProductId(UUID.fromString(id));
    }
}
