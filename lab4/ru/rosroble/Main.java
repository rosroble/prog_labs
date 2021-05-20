package ru.rosroble;

import ru.rosroble.animals.*;
import ru.rosroble.animals.sleepStrategy.SleepStrategyCount;
import ru.rosroble.entities.Entity;
import ru.rosroble.songs.Song;
import ru.rosroble.entities.food.*;
import ru.rosroble.exceptions.SmellNotFoundException;
import ru.rosroble.exceptions.UneatableFoodException;
import ru.rosroble.locations.Location;
import ru.rosroble.locations.Pit;
import ru.rosroble.entities.tree.Tree;
import ru.rosroble.songs.Volume;

public class Main {
    public static void main(String[] args) throws SmellNotFoundException, UneatableFoodException {
        Bear winnie = new Bear(15, "Winnie");
        Sheep sheep = new Sheep(10, "Dolly");
        Elephant elephant = new Elephant(10, "John");
        Food dreamHoney1 = new Food("Воображаемый мёд 1", FoodType.SWEET, winnie);
        Song song = new Song.Builder()
                .artist(winnie)
                .duration(120)
                .name("Спят усталые игрушки")
                .build();

        winnie.sing(song, animal -> {
            if (animal instanceof Bear) {
                song.setVolume(Volume.QUIET);
                System.out.println("Поём тихо...(ворчим)");
            }
        });
        sheep.eat(dreamHoney1);
        winnie.setSleepStrategy(new SleepStrategyCount(sheep));
        winnie.goToSleep();
        Food dreamHoney2 = new Food("Воображаемый мёд 2", FoodType.SWEET, winnie);
        elephant.eat(dreamHoney2);
        winnie.setSleepStrategy(new SleepStrategyCount(elephant));
        winnie.goToSleep();

        Location forest = new Location("Dense Forest");
        Tree tree = new Tree("Six Pines", 666, forest);
        Food realHoney = new Food("Мёд", FoodType.SWEET, winnie);
        Pit pit = new Pit("Хитрая Западня", 100, realHoney);
        winnie.runTo(tree);
        winnie.runTo(pit);
        Entity smelledFood = winnie.smell(); //smell  around winnie's location and return smelled food
        if (smelledFood instanceof Food)
            winnie.eat((Food) smelledFood);
        winnie.eat((Food) smelledFood);    //TESTING ABILITY TO EAT EATEN FOOD
        winnie.smell();                    //TESTING ABILITY TO SMELL EATEN FOOD



    }

}
