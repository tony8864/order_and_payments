package io.github.tony8864.application.order;

import io.github.tony8864.application.order.exception.*;
import io.github.tony8864.application.order.repository.OrderRepository;
import io.github.tony8864.application.order.repository.ProductRepository;
import io.github.tony8864.application.payment.exception.PaymentNotFound;
import io.github.tony8864.application.payment.repository.PaymentRepository;
import io.github.tony8864.domain.Order;
import io.github.tony8864.domain.Payment;
import io.github.tony8864.domain.Product;
import io.github.tony8864.domain.vo.*;

import java.util.Objects;

public class OrderApplicationService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    public OrderApplicationService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            PaymentRepository paymentRepository
    ) {
        this.orderRepository = Objects.requireNonNull(orderRepository);
        this.productRepository = Objects.requireNonNull(productRepository);
        this.paymentRepository = Objects.requireNonNull(paymentRepository);
    }

    public OrderId createOrder(CustomerId customerId) {
        OrderId orderId = OrderId.newId();
        Order order = Order.create(orderId, customerId);

        orderRepository.save(order);

        return orderId;
    }

    public void addItemToOrder(OrderId orderId, ProductId productId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ProductNotFoundException(productId.toString()));

        try {
            order.addItem(
                    product.id(),
                    product.name(),
                    product.price()
            );
        } catch (IllegalStateException e) {
            throw new CannotAddItemToOrderException(e.getMessage());
        }

        orderRepository.save(order);
    }

    public void removeItemFromOrder(OrderId orderId, ProductId productId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ProductNotFoundException(productId.toString()));

        try {
            order.removeItem(product.id());
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new CannotRemoveItemFromOrderException(e.getMessage());
        }

        orderRepository.save(order);
    }

    public void enterPaymentPending(OrderId orderId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new OrderNotFoundException(orderId.toString()));

        try {
            order.enterPaymentPending();
        } catch (IllegalStateException e) {
            throw new CannotEnterPaymentPendingException(e.getMessage());
        }

        orderRepository.save(order);
    }

    public void confirmPayment(OrderId orderId, PaymentId paymentId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFound(paymentId.toString()));

        try {
            assertPaymentReferencesOrder(payment, order);
            ConfirmedPayment confirmedPayment = ConfirmedPayment.create(orderId, payment.amount());
            order.acceptPayment(confirmedPayment);
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new CannotConfirmPaymentException(e.getMessage());
        }

        orderRepository.save(order);
    }

    private void assertPaymentReferencesOrder(Payment payment, Order order) {
        if (!payment.orderId().equals(order.id())) {
            throw new CannotConfirmPaymentException("The payment does not belong to the specified order");
        }
    }
}
