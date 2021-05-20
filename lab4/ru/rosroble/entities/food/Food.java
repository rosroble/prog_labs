package ru.rosroble.entities.food;

import ru.rosroble.entities.Entity;
import ru.rosroble.animals.*;

import java.util.Objects;

public class Food extends Entity {
    private final FoodType foodType;
    private final Animal owner;
    private boolean isEaten = false;


    public Food(String name, FoodType foodType) {
        super(name);
        this.foodType = foodType;
        this.owner = new Bear();
    }

    public Food(String name, FoodType foodType, Animal owner) {
        super(name);
        this.foodType = foodType;
        this.owner = owner;
        owner.setOwnsFood(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Food food = (Food) o;
        return foodType == food.foodType &&
                Objects.equals(owner, food.owner);
    }

    @Override
    public int hashCode() {
       int result = foodType.hashCode();
       result = 31 * result + owner.hashCode();
       result = 31 * result + this.getName().hashCode();
       return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + this.getName() + " " + this.foodType;
    }

    public FoodType getFoodType() {
        return this.foodType;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten() {
        isEaten = true;
        this.setSmell(false);
    }
}
