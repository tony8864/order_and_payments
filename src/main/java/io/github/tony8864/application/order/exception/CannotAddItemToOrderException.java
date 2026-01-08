package io.github.tony8864.application.order.exception;

public class CannotAddItemToOrderException extends RuntimeException {
    public CannotAddItemToOrderException(String message) {
        super(message);
    }
}
