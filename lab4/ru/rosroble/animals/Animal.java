package ru.rosroble.animals;

import ru.rosroble.animals.interfaces.ISingMode;
import ru.rosroble.animals.interfaces.Singable;
import ru.rosroble.songs.Song;
import ru.rosroble.entities.food.*;
import ru.rosroble.exceptions.InvalidParameterException;
import ru.rosroble.exceptions.UneatableFoodException;
import ru.rosroble.locations.Location;

public abstract class Animal implements Singable {
    private final int age;
    private final String name;
    private boolean isSleeping = false;
    private boolean isAlive = true;
    private Food eatsFood;
    private Food ownsFood;
    private Location currentLocation = new Location();
    private final String className = getClass().getSimpleName();
    private Animal.Heart heart = this.new Heart();

    public Animal() {
        this.age = 0;
        this.name = "Unnamed";
        heart.start();
    }

    public class Heart {
        private int pulse;

        public int getPulse() {
            return pulse;
        }

        public void setPulse(int pulse) {
            this.pulse = pulse;
            System.out.println(name + ". Пульс изменен: " + pulse + " ударов в минуту");
        }

        public void stop() {
            this.pulse = 0;
            isAlive = false;
        }

        public void start() {
            this.pulse = 90;
            System.out.println(name + ". Сердце заведено: " + pulse + " ударов в минуту");
            isAlive = true;
        }
    }

    public Animal(int age, String name) {
        if (age < 0) {
            throw new InvalidParameterException("Возраст не может быть отрицательным.");
        }
        if (name == null) {
            throw new InvalidParameterException("Попытка передать null в качестве имени.");
        }
        this.age = age;
        this.name = name;
        System.out.printf("Создан %s по имени %s, которому %d лет%n", this.className, this.name, this.age);
        heart.start();
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
    @Override
    public void sing(Song song, ISingMode singMode) {
        System.out.printf("%s поёт песню %s длительностью %d секунд%n", this.getName(), song.getName(), song.getDuration());
        singMode.setSingMode(this); //sing mode depends on which animal is singing
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

    public Heart getHeart() {
        return heart;
    }

    public abstract void eat(Food food) throws UneatableFoodException;
}
