package ru.rosroble.animals.sleepStrategy;

import ru.rosroble.animals.Animal;
import ru.rosroble.animals.Bear;

public interface SleepStrategy {    // main interface for implementing sleep strategy
    void sleep();
    void setSleepingBear(Bear sleepingBear);
}

