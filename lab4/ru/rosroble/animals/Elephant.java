package ru.rosroble.animals;

import ru.rosroble.entities.food.*;
import ru.rosroble.exceptions.UneatableFoodException;

public class Elephant extends Animal {

    public Elephant(int age, String name) {
        super(age, name);
    }

    public void eat(Food food) throws UneatableFoodException {
        if (food.isEaten()) {
            throw new UneatableFoodException(this.getName());
        }
        System.out.printf("Слонопотам %s бросается на %s %s%n", this.getName(), food.getFoodType().getTranslation(), food.getName());
        switch(food.getFoodType()) {
            case SOUR:
                System.out.println("Слишком кисло, слону не нравится " + food.getName());
                break;
            case SPICY:
                System.out.println("Очень остро, слон недоволен " + food.getName());
                break;
            case SWEET:
                System.out.println("МММммм, как сладко... Слону нравится " + food.getName());
                break;
            case BITTER:
                System.out.println("Очень горько. Слонам такое не нравится ");
                break;
            default:
                System.out.println("Что вы мне принесли вообще?");
                break;
        }
        this.setEatsFood(food);
        food.setEaten();
    }

}
