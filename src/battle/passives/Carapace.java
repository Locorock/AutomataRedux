package battle.passives;

import base.Critter;
import battle.Passive;

public class Carapace extends Passive {
    public Carapace(Critter receiving, String name, double pow) {
        super (receiving, name, pow);
        this.resistances.put("Slashing", 1-0.005*pow);
        this.resistances.put("Piercing", 1-0.001*pow);
    }
}
