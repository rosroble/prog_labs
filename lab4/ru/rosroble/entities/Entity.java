package ru.rosroble.entities;

import ru.rosroble.locations.Location;

import java.util.Objects;

public class Entity { // represents Non-Animal object in the world of Winnie
    private final String name;
    private Location location;
    private final String shortName = getClass().getSimpleName();
    private boolean smell = true;

    public Entity() {
        this.name = "indeterminate";
    }
    public Entity(String name) {
        this.name = name;
        this.location = new Location();
        System.out.printf("Создан объект %s по имени %s%n", shortName, name);
    }
    public Entity(String name, Location location) {
        this.name = name;
        this.location = location;
        System.out.printf("Создан объект %s по имени %s, расположенный в %s%n", shortName, name, location.getName());
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(name, entity.name) &&
                Objects.equals(location, entity.location) &&
                Objects.equals(shortName, entity.shortName);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + location.hashCode();
        result = 31 * result + shortName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + name + " " + location;
    }

    public boolean isSmell() {
        return smell;
    }

    public void setSmell(boolean smell) {
        this.smell = smell;
    }
}


