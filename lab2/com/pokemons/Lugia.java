package com.pokemons;

import com.attacks.*;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;


public class Lugia extends Pokemon {
    public Lugia(String name, int level) {
        super(name, level);
        setStats(106, 90, 130, 90, 154, 110);
        setType(Type.PSYCHIC, Type.FLYING);
        setMove(new ChargeBeam(), new DreamEater(), new Swagger(), new Roost());
    }
}
