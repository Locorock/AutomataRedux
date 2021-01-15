package battle.actives;

import battle.Active;
import base.Critter;

public class PoisonedJaws extends Active {
    public static double multiplier = 0.6;
    public PoisonedJaws(Critter acting, String name, double pow) {
        super (acting, name, pow, multiplier);
        this.types.add("Piercing");
    }

    @Override
    public void run(Critter acting, Critter receiving) {
        receiving.setPoison (receiving.getPoison ()+2);
    }
}
