package battle.actives;

import battle.Active;
import base.Critter;

public class AcidSpit extends Active {
    public static double multiplier = 0.5;
    public AcidSpit(Critter acting, String name, double pow) {
        super (acting, name, pow, multiplier);
        this.types.add("Acid");
        this.types.add("Ranged");
    }
}
