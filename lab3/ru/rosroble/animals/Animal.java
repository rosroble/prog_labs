package ru.rosroble.animals;

import ru.rosroble.entities.food.*;
import ru.rosroble.locations.Location;

public abstract class Animal {
    private final int age;
    private final String name;
    private boolean isSleeping = false;
    private Food eatsFood;
    private Food ownsFood;
    private Location currentLocation = new Location();
    private final String className = getClass().getSimpleName();

    public Animal() {
        this.age = 0;
        this.name = "Unnamed";
    }

    public Animal(int age, String name) {
        this.age = age;
        this.name = name;
        System.out.printf("Создан %s по имени %s, которому %d лет%n", this.className, this.name, this.age);
    }

    @Override
    public String toString() {
        return className + " " + name + " " + age;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(age);
        result = 31 * result + name.hashCode();
        result = 31 * result + className.hashCode();
        return  result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        } else {
            Animal objToAnimal = (Animal) obj;
            return this.age == objToAnimal.age && this.name.equals(objToAnimal.name);
        }

    }

    public String getClassName() {
        return className;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public Food getEatsFood() {
        return this.eatsFood;
    }

    public Food getOwnsFood() {
        return ownsFood;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setOwnsFood(Food ownsFood) {
        this.ownsFood = ownsFood;
    }

    public void setSleepStatus(boolean isSleeping) {
        this.isSleeping = isSleeping;
        if (isSleeping) {
            System.out.println(this.name + " засыпает");
        } else {
            System.out.println(this.name + " просыпается");
        }
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setEatsFood(Food food) {
        this.eatsFood = food;
    }

    public boolean isSleeping() {
        return this.isSleeping;
    }

    public abstract void eat(Food food);
}
