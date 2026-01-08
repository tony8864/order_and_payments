package io.github.tony8864.application.order.exception;

public class CannotRemoveItemFromOrderException extends RuntimeException {
    public CannotRemoveItemFromOrderException(String message) {
        super(message);
    }
}
