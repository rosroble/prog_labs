package com.attacks;

import ru.ifmo.se.pokemon.*;

public class Facade extends PhysicalMove {
    public Facade() {
        super(Type.NORMAL, 70, 100);
    }
    @Override
    protected void applyOppDamage(Pokemon def, double damage) {
        Status cond = def.getCondition();
        if (cond.equals(Status.BURN) || cond.equals(Status.POISON) || cond.equals(Status.PARALYZE)) {
            def.setMod(Stat.HP, (int) Math.round(damage) * 2);
        }
        else {
            def.setMod(Stat.HP, (int) Math.round(damage));
        }    
    }
    @Override
    protected java.lang.String describe() {
        return "used Facade on his enemy!";
    }
}
