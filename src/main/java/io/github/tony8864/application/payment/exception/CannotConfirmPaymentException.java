package io.github.tony8864.application.payment.exception;

public class CannotConfirmPaymentException extends RuntimeException {
    public CannotConfirmPaymentException(String message) {
        super(message);
    }
}
