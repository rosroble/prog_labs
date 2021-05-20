package com.pokemons;

import com.attacks.*;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;


public class Slugma extends Pokemon {
    public Slugma(String name, int level) {
        super(name, level);
        setStats(40, 40, 40, 70, 40, 20);
        setType(Type.FIRE);
        setMove(new Confide(), new Overheat(), new Facade());
    }
}