package io.github.tony8864.domain;

import io.github.tony8864.domain.vo.ProductId;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private final ProductId id;
    private final String name;
    private final BigDecimal price;
    private final boolean availability;

    private Product(
            ProductId id,
            String name,
            BigDecimal price,
            boolean availability
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or blank");
        }

        this.id = Objects.requireNonNull(id);
        this.name = name;
        this.price = Objects.requireNonNull(price);
        this.availability = availability;
    }

    public static Product create(
            ProductId id,
            String name,
            BigDecimal price,
            boolean availability
    ) {
        return new Product(id, name, price, availability);
    }

    public ProductId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }

    public boolean isAvailable() {
        return availability;
    }
}
