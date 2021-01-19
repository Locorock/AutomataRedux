package battle.actives;

import battle.Active;
import base.Critter;

public class Jaws extends Active {
    public static double multiplier = 1.6;
    public Jaws(Critter acting, String name, double pow) {
        super (acting, name, pow, multiplier);
        this.types.add("Piercing");
    }
}
