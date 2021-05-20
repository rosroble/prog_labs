package com.attacks;

import ru.ifmo.se.pokemon.*;

public class SwordsDance extends StatusMove {
    public SwordsDance() {
        super(Type.NORMAL, 0, 0);
    }
    @Override
    protected void applySelfEffects(Pokemon p) {
        p.setMod(Stat.ATTACK, 2);
    }
    @Override
    protected java.lang.String describe() {
        String s = "made the swords dance!";
        return s;
    }
}