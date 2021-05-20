package ru.rosroble.locations;

import ru.rosroble.entities.*;

import java.util.Objects;

public class Location {
    private final String name;
    private Entity entity; //object situated in this location

    public Location() {
        this.name = "indeterminate";
    }
    public Location(String name) {
        this.name = name;
        this.entity = new Entity();
    }
    public Location(String name, Entity entity) {
        this.name = name;
        this.entity = entity;
    }
    public String getName() {
        return name;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(name, location.name) &&
                Objects.equals(entity, location.entity);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + entity.hashCode();
        return result;
    }
}
