package io.github.tony8864.infrastructure.inmemory;

import io.github.tony8864.application.payment.repository.PaymentRepository;
import io.github.tony8864.domain.Payment;
import io.github.tony8864.domain.vo.PaymentId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryPaymentRepository implements PaymentRepository {

    private final Map<PaymentId, Payment> payments;

    public InMemoryPaymentRepository() {
        payments = new HashMap<>();
    }

    @Override
    public void save(Payment payment) {
        payments.put(payment.id(), payment);
    }

    @Override
    public Optional<Payment> findById(PaymentId paymentId) {
        return Optional.of(payments.get(paymentId));
    }
}
