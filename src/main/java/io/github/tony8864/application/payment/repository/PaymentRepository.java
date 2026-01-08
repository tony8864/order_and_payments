package io.github.tony8864.application.payment.repository;

import io.github.tony8864.domain.Payment;
import io.github.tony8864.domain.vo.PaymentId;

import java.util.Optional;

public interface PaymentRepository {
    void save(Payment payment);
    Optional<Payment> findById(PaymentId paymentId);
}
