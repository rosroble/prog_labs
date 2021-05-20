package ru.rosroble.animals;

import ru.rosroble.entities.food.Food;

public class Sheep extends Animal  {

    public Sheep(int age, String name) {
        super(age, name);
    }
    public Sheep() {
        super();
    }

    @Override
    public void eat(Food food) {
        System.out.println("Овечка " + this.getName() + " поедает " + food.getName());
        this.setEatsFood(food);
        food.setEaten();
    }

}
