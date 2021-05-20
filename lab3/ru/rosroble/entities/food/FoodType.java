package ru.rosroble.entities.food;

public enum FoodType {
    SWEET("сладкий"), BITTER("горький"), SPICY("острый"), SOUR("кислый");

    private final String translation;

    FoodType(String translation) {
        this.translation = translation;
    }
    public String getTranslation() {
        return this.translation;
    }
}
