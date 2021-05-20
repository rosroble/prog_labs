package com.attacks;

import ru.ifmo.se.pokemon.*;

public class Roost extends StatusMove {
    public Roost() {
        super(Type.FLYING, 0, 0);
    }
    @Override
    protected void applySelfEffects(Pokemon p) {
        int halfHP = (int) p.getStat(Stat.HP) / 2;
        p.setMod(Stat.HP, halfHP);
    }
    @Override
    protected java.lang.String describe() {
        String s = "is Roosting";
        return s;
    }
}