package ru.rosroble.entities.tree;

import ru.rosroble.entities.Entity;
import ru.rosroble.locations.Location;

public class Tree extends Entity {
    private final int age;

    public Tree(String name, int age) {
        super(name);
        this.age = age;
        System.out.printf("Создано дерево с именем %s и возрастом %d%n", name, age);
    }

    public Tree(String name, int age, Location location) {
        super(name, location);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

}
