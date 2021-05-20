package com.attacks;

import ru.ifmo.se.pokemon.*;

public class DreamEater extends SpecialMove {
    public DreamEater() {
        super(Type.PSYCHIC, 100, 100);
    }

    @Override
    protected void applyOppDamage(Pokemon def, double damage) {
        Status cond = def.getCondition();
        if (cond.equals(Status.SLEEP)) {
            def.setMod(Stat.HP, (int) Math.round(damage));
        }
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        int drainedHP = (int) (p.getStat(Stat.HP) - p.getHP() / 2);
        p.setMod(Stat.HP, drainedHP);
    }
    @Override
    protected java.lang.String describe() {
        return "ate his enemy Dream!";
    }
}
