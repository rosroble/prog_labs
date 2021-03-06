package ru.rosroble.animals;

import ru.rosroble.animals.sleepStrategy.SleepStrategy;
import ru.rosroble.entities.Entity;
import ru.rosroble.animals.interfaces.Countable;
import ru.rosroble.animals.interfaces.Runnable;
import ru.rosroble.animals.interfaces.Smellable;
import ru.rosroble.entities.food.*;
import ru.rosroble.locations.Location;

public class Bear extends Animal implements Countable, Runnable, Smellable {

    SleepStrategy sleepStrategy;
    public Bear() {
        super(0, "Unnamed");
    }

    public Bear(int age, String name) {
        super(age, name);
    }

    public void setSleepStrategy(SleepStrategy sleepStrategy) {
        this.sleepStrategy = sleepStrategy;
        sleepStrategy.setSleepingBear(this);
    }

    public void goToSleep() {
        sleepStrategy.sleep();
    }
    @Override
    public void eat(Food food) {
        if (food.isEaten()) {
            System.out.println("Нельзя съесть уже съеденное...");
        }
        else {
            System.out.printf("Мишка %s грозно поедает %s %s%n", this.getName(), food.getFoodType().getTranslation(), food.getName());
            this.setEatsFood(food);
            food.setEaten();
        }
    }

    @Override
    public void count(Animal animal) {
        System.out.println(this.getName() + " считает животных типа " + animal.getClassName());
    }

// following code is now implemented as a SleepStrategy:
//
//    public void tryToSleepByCounting(Animal animal) {
//        System.out.println(this.getName() + " пытается уснуть");
//        count(animal);
//        if (animal.getEatsFood().equals(this.getOwnsFood()))
//            System.out.printf("%s не может уснуть, т.к %s ест его еду :(%n", this.getName(), animal.getName());
//        else setSleepStatus(true);
//    }

    @Override
    public void runTo(Location location) {
        this.setCurrentLocation(location);
        System.out.printf("%s бежит к %s%n", this.getName(), location.getName());
    }

    @Override
    public void runTo(Entity entity) {
        this.setCurrentLocation(entity.getLocation());
        System.out.printf("%s бежит к %s%n", this.getName(), entity.getName());
    }

    @Override
    public Entity smell() {
        Entity entity = this.getCurrentLocation().getEntity();
        if (entity == null) {
            return null;
        }
        if (entity.isSmell()) {
            smellMessage(entity);
            return entity;
        }
        else {
            smellNotFoundMessage();
            return null;
        }
    }

    protected void smellMessage(Entity entity) {
        if (entity instanceof Food) {
            Food food = (Food) entity;
            System.out.printf("%s чувствует %s запах еды %s, доносящийся из %s%n",
                    this.getName(),
                    food.getFoodType().getTranslation(),
                    food.getName(),
                    getCurrentLocation().getName());
        }
        else {
            System.out.printf("%s чувствует запах %s%n", this.getName(), entity.getName());
        }

    }

    protected void smellNotFoundMessage() {
        System.out.println("Не чувствую запахов... интересно почему?");
    }
}
