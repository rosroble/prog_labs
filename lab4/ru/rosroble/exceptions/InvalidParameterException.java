package ru.rosroble.exceptions;

public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException(String message) {
        super(message);
    }
}
