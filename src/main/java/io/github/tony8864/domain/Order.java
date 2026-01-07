package io.github.tony8864.domain;

import io.github.tony8864.domain.enums.OrderStatus;
import io.github.tony8864.domain.vo.ConfirmedPayment;
import io.github.tony8864.domain.vo.OrderId;
import io.github.tony8864.domain.vo.OrderItem;
import io.github.tony8864.domain.vo.ProductId;

import java.math.BigDecimal;
import java.util.*;

public class Order {
    private final OrderId id;
    private final List<OrderItem> items;
    private OrderStatus status;
    private BigDecimal total;

    private Order(OrderId id) {
        this.id = Objects.requireNonNull(id);
        this.items = new ArrayList<>();
        this.status = OrderStatus.CREATED;
        this.total = BigDecimal.ZERO;
    }

    public static Order create(OrderId id) {
        return new Order(id);
    }

    /**
     * Adds a new item in the Order, or increases the item's quantity
     *
     * <p>
     *     The Order {@code status} must be CREATED.
     * </p>
     *
     * <p>
     *     If {@code items} don't contain the product specified by the {@code productId}, then
     *     a new OrderItem is created and added in the list. If the list contains the specified product,
     *     we increase its quantity by 1.
     * </p>
     *
     * @param productId
     * @param name
     * @param price
     */
    public void addItem(
            ProductId productId,
            String name,
            BigDecimal price
    ) {

        if (!OrderStatus.CREATED.equals(status)) {
            throw new IllegalStateException("To add a product, the Order must be in CREATED state");
        }

        Optional<OrderItem> orderItemOpt = findByProductId(productId);
        if (orderItemOpt.isEmpty()) {
            OrderItem item = OrderItem.create(productId, name, price, 1);
            items.add(item);
        } else {
            OrderItem item = orderItemOpt.get();
            items.remove(item);
            items.add(item.withQuantity(item.quantity() + 1));
        }
        calculateTotal();
    }

    /**
     * Removes a product from the Order
     *
     * <p>
     *     The Order {@code status} must be CREATED.
     * </p>
     *
     * <p>
     *     The product specified by the {@code productId} must be in the {@code items}, otherwise it's an error.
     *     The item's quantity is decreases by one, until it reaches zero.
     *     At that point the item is removed from the list.
     * </p>
     *
     * @param productId
     */
    public void removeItem(
            ProductId productId
    ) {
        if (!OrderStatus.CREATED.equals(status)) {
            throw new IllegalStateException("To remove a product, Order must be in CREATED state");
        }

        Optional<OrderItem> itemOpt = findByProductId(productId);
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("The product to be removed, has not been added in the Order");
        }

        OrderItem item = itemOpt.get();
        items.remove(item);
        if (item.quantity() > 1) {
            items.add(item.withQuantity(item.quantity() - 1));
        }
        calculateTotal();
    }

    public void enterPaymentPending() {
        if (!OrderStatus.CREATED.equals(status)) {
            throw new IllegalStateException("Order must be in CREATED state to initiate a payment");
        }

        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot enter the payment flow with no items added in the order");
        }

        status = OrderStatus.PAYMENT_PENDING;
    }

    public void acceptPayment(ConfirmedPayment confirmedPayment) {
        if (OrderStatus.PAID.equals(status)) {
            throw new IllegalStateException("Order cannot be paid twice");
        }

        if (!OrderStatus.PAYMENT_PENDING.equals(status)) {
            throw new IllegalStateException("Order must be in PAYMENT_PENDING state to accept a payment");
        }

        if (confirmedPayment.amount().compareTo(total) < 0) {
            throw new IllegalArgumentException("The given amount is not sufficient to pay the order");
        }

        status = OrderStatus.PAID;
    }

    public OrderId id() {
        return id;
    }

    public BigDecimal total() {
        return total;
    }

    public boolean isPaymentPending() {
        return status == OrderStatus.PAYMENT_PENDING;
    }

    public boolean isPaid() {
        return status == OrderStatus.PAID;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    private void calculateTotal() {
        total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.subtotal());
        }
    }

    private Optional<OrderItem> findByProductId(ProductId productId) {
        return items.stream()
                .filter(item -> item.productId().equals(productId))
                .findAny();
    }
}
