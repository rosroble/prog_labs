package ru.rosroble.animals.sleepStrategy;

import ru.rosroble.animals.Animal;
import ru.rosroble.animals.Bear;

public class SleepStrategyCount implements SleepStrategy { // one of the implementations of strategy: counting
    Animal countedAnimal;
    Bear sleepingBear;

    public SleepStrategyCount(Animal countedAnimal) {
        this.countedAnimal = countedAnimal;
    }

    public void setSleepingBear(Bear sleepingBear) {
        this.sleepingBear = sleepingBear;
    }

    public void sleep() {
        System.out.println(sleepingBear.getName() + " пытается уснуть");
        sleepingBear.count(countedAnimal);
        if (countedAnimal.getEatsFood().equals(sleepingBear.getOwnsFood()))
            System.out.printf("%s не может уснуть, т.к %s ест его еду :(%n", sleepingBear.getName(), countedAnimal.getName());
        else sleepingBear.setSleepStatus(true);
    }
}
