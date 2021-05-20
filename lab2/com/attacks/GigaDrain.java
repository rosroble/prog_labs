package com.attacks;

import ru.ifmo.se.pokemon.*;

public class GigaDrain extends SpecialMove {
    public GigaDrain() {
        super(Type.GRASS, 75, 100);
    }
    @Override
    protected void applySelfEffects(Pokemon p) {
        int drainedHP = (int) (p.getStat(Stat.HP) - p.getHP() / 2);
        p.setMod(Stat.HP, drainedHP);
    }
    @Override
    protected java.lang.String describe() {
        return "casted Giga Drain!";
    }
}
