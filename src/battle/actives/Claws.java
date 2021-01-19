package battle.actives;

import battle.Active;
import base.Critter;

public class Claws extends Active {
    public static double multiplier = 2;
    public Claws(Critter acting, String name, double pow) {
        super (acting, name, pow, multiplier);
        this.types.add("Slashing");
    }
}
