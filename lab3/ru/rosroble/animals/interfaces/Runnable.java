package ru.rosroble.animals.interfaces;

import ru.rosroble.animals.Animal;
import ru.rosroble.entities.Entity;
import ru.rosroble.locations.Location;

public interface Runnable {
    void runTo(Location location);
    void runTo(Entity entity);
}
