package io.github.tony8864.domain.vo;

import java.util.UUID;

public record PaymentId(UUID value) {

    public PaymentId {
        if (value == null) {
            throw new IllegalArgumentException("Payment Id cannot be null");
        }
    }

    public static PaymentId newId() {
        return new PaymentId(UUID.randomUUID());
    }
}
