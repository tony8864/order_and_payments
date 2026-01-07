package io.github.tony8864.domain.vo;

import java.math.BigDecimal;
import java.util.Objects;

public record OrderItem(
        ProductId productId,
        String name,
        BigDecimal priceAtPurchase,
        int quantity
) {

    public OrderItem(
            ProductId productId,
            String name,
            BigDecimal priceAtPurchase,
            int quantity
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item's name cannot be null or blank");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Item's quantity cannot be zero or negative");
        }

        this.productId = Objects.requireNonNull(productId);
        this.name = name;
        this.priceAtPurchase = Objects.requireNonNull(priceAtPurchase);
        this.quantity = quantity;
    }

    public static OrderItem create(
        ProductId productId,
        String name,
        BigDecimal priceAtPurchase,
        int quantity
    ) {
        return new OrderItem(productId, name, priceAtPurchase, quantity);
    }

    public BigDecimal subtotal() {
        return priceAtPurchase.multiply(BigDecimal.valueOf(quantity));
    }

    public OrderItem withQuantity(int quantity) {
        return new OrderItem(productId, name, priceAtPurchase, quantity);
    }
}
