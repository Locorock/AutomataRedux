package battle.actives;

import battle.Active;
import base.Critter;

public class PoisonSpit extends Active {
    public static double multiplier = 0;
    public PoisonSpit(Critter acting, String name, double pow) {
        super (acting, name, pow, multiplier);
        this.types.add("Ranged");
    }

    @Override
    public void run(Critter acting, Critter receiving) {
        receiving.setPoison (receiving.getPoison ()+(int)pow/5);
    }
}
