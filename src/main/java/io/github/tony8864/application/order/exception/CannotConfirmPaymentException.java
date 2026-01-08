package io.github.tony8864.application.order.exception;

public class CannotConfirmPaymentException extends RuntimeException {
    public CannotConfirmPaymentException(String message) {
        super(message);
    }
}
