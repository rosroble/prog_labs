package ru.rosroble.common.exceptions;

public class InvalidAuthTypeException extends Exception{
    public InvalidAuthTypeException(String message) {
        super (message);
    }

    public void printMessage() {
        System.out.println(this.getMessage());
    }
}
