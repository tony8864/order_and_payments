package io.github.tony8864.application.payment.exception;

public class CannotFailPaymentException extends RuntimeException {
    public CannotFailPaymentException(String message) {
        super(message);
    }
}
