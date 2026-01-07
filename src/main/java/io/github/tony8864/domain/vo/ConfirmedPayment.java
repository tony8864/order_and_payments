package io.github.tony8864.domain.vo;

import java.math.BigDecimal;

public record ConfirmedPayment(OrderId orderId, BigDecimal amount) {

    public ConfirmedPayment {
        if (orderId == null) {
            throw new IllegalArgumentException("Order Id cannot be null");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount cannot be null or non positive");
        }
    }

    public static ConfirmedPayment create(OrderId id, BigDecimal amount) {
        return new ConfirmedPayment(id, amount);
    }
}
