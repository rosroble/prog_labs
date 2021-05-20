package com.pokemons;

import com.attacks.*;
import ru.ifmo.se.pokemon.Type;


public class Magcargo extends Slugma {
    public Magcargo(String name, int level) {
        super(name, level);
        setStats(60, 50, 120, 90, 80, 30);
        setType(Type.FIRE, Type.ROCK);
        setMove(new Confide(), new Overheat(), new Facade(), new StoneEdge());
    }
}