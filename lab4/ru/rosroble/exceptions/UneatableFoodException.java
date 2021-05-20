package ru.rosroble.exceptions;

public class UneatableFoodException extends Exception {

    public final String foodName;

    public UneatableFoodException(String foodName) {
        super("Еда " + foodName + " не может быть съедена.");
        this.foodName = foodName;
    }

    public UneatableFoodException(String foodName, String message) {
        super(foodName + ": " + message);
        this.foodName = foodName;
    }
}
