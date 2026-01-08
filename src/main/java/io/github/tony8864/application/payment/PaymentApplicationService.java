package io.github.tony8864.application.payment;

import io.github.tony8864.application.order.exception.OrderNotFoundException;
import io.github.tony8864.application.order.repository.OrderRepository;
import io.github.tony8864.application.payment.exception.CannotConfirmPaymentException;
import io.github.tony8864.application.payment.exception.CannotFailPaymentException;
import io.github.tony8864.application.payment.exception.PaymentNotFound;
import io.github.tony8864.application.payment.repository.PaymentRepository;
import io.github.tony8864.domain.Order;
import io.github.tony8864.domain.Payment;
import io.github.tony8864.domain.enums.PaymentMethod;
import io.github.tony8864.domain.vo.OrderId;
import io.github.tony8864.domain.vo.PaymentId;

import java.util.Objects;

public class PaymentApplicationService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentApplicationService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository
    ) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository);
        this.orderRepository = Objects.requireNonNull(orderRepository);
    }

    public PaymentId createPayment(OrderId orderId, PaymentMethod method) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId.toString()));

        Payment payment = Payment.create(
                PaymentId.newId(),
                orderId,
                method,
                order.total()
        );

        paymentRepository.save(payment);

        return payment.id();
    }

    public void confirmPayment(PaymentId paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFound(paymentId.toString()));

        try {
            payment.confirm();
        } catch (IllegalStateException e) {
            throw new CannotConfirmPaymentException(e.getMessage());
        }

        paymentRepository.save(payment);
    }

    public void failPayment(PaymentId paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFound(paymentId.toString()));

        try {
            payment.fail();
        } catch (IllegalStateException e) {
            throw new CannotFailPaymentException(e.getMessage());
        }

        paymentRepository.save(payment);
    }
}
