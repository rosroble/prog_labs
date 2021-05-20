package com.attacks;

import ru.ifmo.se.pokemon.*;

public class Swagger extends StatusMove {
    public Swagger(){
        super(Type.NORMAL, 0, 85);
    }
    @Override
    protected void applyOppEffects(Pokemon p) {
        Effect.confuse(p);
    }
    @Override
    protected void applySelfEffects(Pokemon p) {
        p.setMod(Stat.ATTACK, 2);
    }
    @Override
    protected java.lang.String describe() {
        String s = "has Swaggered his enemy!";
        return s;
    }
}