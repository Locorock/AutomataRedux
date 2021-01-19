package battle.actives;

import battle.Active;
import base.Critter;

public class Struggle extends Active {
    public static double multiplier = 1;
    public Struggle(Critter acting, String name) {
        super (acting, name, 16, multiplier);
        this.types.add("Blunt");
    }

    @Override
    public void run(Critter acting, Critter receiving) {
        super.run (acting, receiving);
        acting.setHP (acting.getHP ()-acting.getMaxHP ()/10);
    }
}
