package io.github.tony8864.domain;

import io.github.tony8864.domain.vo.ConfirmedPayment;
import io.github.tony8864.domain.vo.OrderId;
import io.github.tony8864.domain.vo.OrderItem;
import io.github.tony8864.domain.vo.ProductId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order createOrder() {
        return Order.create(OrderId.newId());
    }

    private OrderItem findByProductId(ProductId id, List<OrderItem> items) {
        return items.stream()
                .filter(item -> item.productId().equals(id))
                .findAny()
                .orElse(null);
    }

    @Test
    void given_empty_order_when_item_is_product_is_added_new_item_is_created() {
        // arrange
        Order order = createOrder();
        Product product = Product.create(ProductId.newId(), "prod-1", BigDecimal.TEN, true);

        // act
        order.addItem(product.id(), product.name(), product.price());

        // assert
        OrderItem item = findByProductId(product.id(), order.getItems());
        assertNotNull(item);
        assertEquals("prod-1", product.name());
        assertEquals(BigDecimal.TEN, item.priceAtPurchase());
        assertEquals(BigDecimal.TEN, order.total());
        assertEquals(1, item.quantity());
        assertTrue(product.isAvailable());
    }

    @Test
    void given_order_with_item_when_same_product_is_added_then_quantity_is_increased() {
        // arrange
        Order order = createOrder();
        Product product = Product.create(ProductId.newId(), "prod-1", BigDecimal.TEN, true);
        order.addItem(product.id(), product.name(), product.price());

        // act
        order.addItem(product.id(), product.name(), product.price());

        // assert
        OrderItem item = findByProductId(product.id(), order.getItems());
        assertNotNull(item);
        assertEquals("prod-1", product.name());
        assertEquals(BigDecimal.TEN, item.priceAtPurchase());
        assertEquals(BigDecimal.TEN.add(BigDecimal.TEN), order.total());
        assertEquals(2, item.quantity());
        assertTrue(product.isAvailable());
    }

    @Test
    void given_order_with_item_when_item_is_removed_then_quantity_is_decreased() {
        // arrange
        Order order = createOrder();
        Product product = Product.create(ProductId.newId(), "prod-1", BigDecimal.TEN, true);
        order.addItem(product.id(), product.name(), product.price());
        order.addItem(product.id(), product.name(), product.price());

        // act
        order.removeItem(product.id());

        // assert
        OrderItem item = findByProductId(product.id(), order.getItems());
        assertNotNull(item);
        assertEquals(1, item.quantity());
        assertEquals(BigDecimal.TEN, order.total());
    }

    @Test
    void given_order_with_item_when_item_is_removed_then_its_deleted_from_list() {
        // arrange
        Order order = createOrder();
        Product product = Product.create(ProductId.newId(), "prod-1", BigDecimal.TEN, true);
        order.addItem(product.id(), product.name(), product.price());

        // act
        order.removeItem(product.id());

        // assert
        OrderItem item = findByProductId(product.id(), order.getItems());
        assertNull(item);
        assertEquals(BigDecimal.ZERO, order.total());
    }

    @Test
    void given_created_order_when_payment_initiated_enter_payment_pending() {
        // arrange
        Order order = createOrder();
        Product product = Product.create(ProductId.newId(), "prod-1", BigDecimal.TEN, true);
        order.addItem(product.id(), product.name(), product.price());

        // act
        order.enterPaymentPending();

        // assert
        assertTrue(order.isPaymentPending());
    }

    @Test
    void cannot_enter_payment_pending_when_already_in_payment_flow() {
        // arrange
        Order order = createOrder();
        Product product = Product.create(ProductId.newId(), "prod-1", BigDecimal.TEN, true);
        order.addItem(product.id(), product.name(), product.price());
        order.enterPaymentPending();

        // assert
        assertThrows(IllegalStateException.class, order::enterPaymentPending);
    }

    @Test
    void cannot_enter_payment_pending_when_order_has_no_items() {
        // arrange
        Order order = createOrder();

        // assert
        assertThrows(IllegalStateException.class, order::enterPaymentPending);
    }


    @Test
    void given_order_is_created_when_payment_is_accepted_then_status_is_paid() {
        // arrange
        Order order = createOrder();
        Product product = Product.create(ProductId.newId(), "prod-1", BigDecimal.TEN, true);
        order.addItem(product.id(), product.name(), product.price());
        order.enterPaymentPending();
        ConfirmedPayment payment = ConfirmedPayment.create(order.id(), BigDecimal.TEN);

        // act
        order.acceptPayment(payment);

        // assert
        assertTrue(order.isPaid());
    }

    @Test
    void cannot_accept_payment_when_already_paid() {
        // arrange
        Order order = createOrder();
        Product product = Product.create(ProductId.newId(), "prod-1", BigDecimal.TEN, true);
        order.addItem(product.id(), product.name(), product.price());
        order.enterPaymentPending();
        ConfirmedPayment payment = ConfirmedPayment.create(order.id(), BigDecimal.TEN);
        order.acceptPayment(payment);

        // assert
        assertThrows(IllegalStateException.class, () -> order.acceptPayment(payment));
    }

    @Test
    void cannot_accept_payment_when_not_in_pending_state() {
        // arrange
        Order order = createOrder();
        Product product = Product.create(ProductId.newId(), "prod-1", BigDecimal.TEN, true);
        order.addItem(product.id(), product.name(), product.price());
        ConfirmedPayment payment = ConfirmedPayment.create(order.id(), BigDecimal.TEN);

        // assert
        assertThrows(IllegalStateException.class, () -> order.acceptPayment(payment));
    }

    @Test
    void cannot_accept_payment_when_the_amount_is_insufficient() {
        // arrange
        Order order = createOrder();
        Product product = Product.create(ProductId.newId(), "prod-1", BigDecimal.TEN, true);
        order.addItem(product.id(), product.name(), product.price());
        order.enterPaymentPending();
        ConfirmedPayment payment = ConfirmedPayment.create(order.id(), BigDecimal.valueOf(6));

        // assert
        assertThrows(IllegalArgumentException.class, () -> order.acceptPayment(payment));
    }
}