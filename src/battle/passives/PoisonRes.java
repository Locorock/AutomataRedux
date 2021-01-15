package battle.passives;

import base.Critter;
import battle.Passive;

public class PoisonRes extends Passive {
    public PoisonRes(Critter receiving, String name, double pow) {
        super (receiving, name, pow);
        this.resistances.put("Poison", 1-0.031*pow);
    }
}
