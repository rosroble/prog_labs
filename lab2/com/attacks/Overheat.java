package com.attacks;

import ru.ifmo.se.pokemon.*;

public class Overheat extends SpecialMove {
    public Overheat() {
        super(Type.FIRE, 130, 90);
    }
    @Override
    protected void applySelfEffects(Pokemon p) {
        p.setMod(Stat.SPECIAL_ATTACK, -2);
    }
    @Override
    protected java.lang.String describe() {
        String s = "has overheated his enemy!";
        return s;
    }
}