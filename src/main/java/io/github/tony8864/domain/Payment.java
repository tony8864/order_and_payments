package io.github.tony8864.domain;

import io.github.tony8864.domain.enums.PaymentMethod;
import io.github.tony8864.domain.enums.PaymentStatus;
import io.github.tony8864.domain.vo.OrderId;
import io.github.tony8864.domain.vo.PaymentId;

import java.math.BigDecimal;
import java.util.Objects;

public class Payment {
    private final PaymentId id;
    private final OrderId orderId;
    private final PaymentMethod method;
    private final BigDecimal amount;
    private PaymentStatus status;

    private Payment(
            PaymentId id,
            OrderId orderId,
            PaymentMethod method,
            BigDecimal amount
    ) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount cannot be negative or zero");
        }

        this.id = Objects.requireNonNull(id);
        this.orderId = Objects.requireNonNull(orderId);
        this.method = Objects.requireNonNull(method);
        this.amount = amount;
        this.status = PaymentStatus.CREATED;
    }

    public static Payment create(
            PaymentId id,
            OrderId orderId,
            PaymentMethod method,
            BigDecimal amount
    ) {
        return new Payment(id, orderId, method, amount);
    }

    public void confirm() {
        if (!PaymentStatus.CREATED.equals(status)) {
            throw new IllegalStateException("Payment must be in CREATED state");
        }
        status = PaymentStatus.CONFIRMED;
    }

    public void fail() {
        if (!PaymentStatus.CREATED.equals(status)) {
            throw new IllegalStateException("Payment must be in CREATED state");
        }
        status = PaymentStatus.FAILED;
    }

    public BigDecimal amount() {
        return amount;
    }

    public OrderId orderId() {
        return orderId;
    }

    public PaymentId id() {
        return id;
    }
}
