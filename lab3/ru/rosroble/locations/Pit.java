package ru.rosroble.locations;

import ru.rosroble.entities.Entity;
import ru.rosroble.locations.Location;

public class Pit extends Location {
    private final int depth;

    public Pit(String name, int depth, Entity entity) {
        super(name, entity);
        this.depth = depth;

        System.out.printf("Создана яма глубины %d, содержащая %s%n", depth, entity.getName());
    }
    public int getDepth() {
        return depth;
    }
}
