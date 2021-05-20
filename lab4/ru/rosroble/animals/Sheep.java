package ru.rosroble.animals;

import ru.rosroble.entities.food.Food;
import ru.rosroble.exceptions.UneatableFoodException;

public class Sheep extends Animal  {

    public Sheep(int age, String name) {
        super(age, name);
    }
    public Sheep() {
        super();
    }

    @Override
    public void eat(Food food) throws UneatableFoodException {
        if (food.isEaten()) {
            throw new UneatableFoodException(this.getName(), "Еда уже съедена.");
        }
        System.out.println("Овечка " + this.getName() + " поедает " + food.getName());
        this.setEatsFood(food);
        food.setEaten();
    }

}
