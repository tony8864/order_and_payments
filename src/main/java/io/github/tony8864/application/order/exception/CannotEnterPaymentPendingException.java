package io.github.tony8864.application.order.exception;

public class CannotEnterPaymentPendingException extends RuntimeException {
    public CannotEnterPaymentPendingException(String message) {
        super(message);
    }
}
