package com.attacks;

import ru.ifmo.se.pokemon.*;

public class StunSpore extends StatusMove {
    public StunSpore() {
        super(Type.GRASS, 0, 75);
    }
    @Override
    protected void applyOppEffects(Pokemon p) {
        Effect.paralyze(p);
    }
    @Override
    protected java.lang.String describe() {
        String s = "has used Stun Spore ability!";
        return s;
    }
}