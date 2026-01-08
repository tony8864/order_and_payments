package io.github.tony8864;

import io.github.tony8864.application.order.OrderApplicationService;
import io.github.tony8864.application.order.repository.OrderRepository;
import io.github.tony8864.application.order.repository.ProductRepository;
import io.github.tony8864.application.payment.PaymentApplicationService;
import io.github.tony8864.application.payment.repository.PaymentRepository;
import io.github.tony8864.domain.enums.PaymentMethod;
import io.github.tony8864.domain.vo.CustomerId;
import io.github.tony8864.domain.vo.OrderId;
import io.github.tony8864.domain.vo.PaymentId;
import io.github.tony8864.domain.vo.ProductId;
import io.github.tony8864.infrastructure.inmemory.InMemoryOrderRepository;
import io.github.tony8864.infrastructure.inmemory.InMemoryPaymentRepository;
import io.github.tony8864.infrastructure.inmemory.InMemoryProductRepository;
import io.github.tony8864.infrastructure.inmemory.ProductCatalogInitializer;

public class Main {
    public static void main(String[] args) {
        ProductRepository productRepository = new InMemoryProductRepository();
        PaymentRepository paymentRepository = new InMemoryPaymentRepository();
        OrderRepository orderRepository = new InMemoryOrderRepository();

        ProductCatalogInitializer productCatalogInitializer = new ProductCatalogInitializer(productRepository);
        productCatalogInitializer.initialize();

        OrderApplicationService orderApplicationService = new OrderApplicationService(
                orderRepository,
                productRepository,
                paymentRepository
        );

        PaymentApplicationService paymentApplicationService = new PaymentApplicationService(
                paymentRepository,
                orderRepository
        );

        OrderId orderId = orderApplicationService.createOrder(CustomerId.newId());

        orderApplicationService.addItemToOrder(orderId, ProductId.of("00000000-0000-0000-0000-000000000001"));
        orderApplicationService.addItemToOrder(orderId, ProductId.of("00000000-0000-0000-0000-000000000002"));

        orderApplicationService.enterPaymentPending(orderId);

        PaymentId paymentId = paymentApplicationService.createPayment(orderId, PaymentMethod.CARD);

        paymentApplicationService.confirmPayment(paymentId);
        orderApplicationService.confirmPayment(orderId, paymentId);

        System.out.println("Order paid successfully!");
    }
}