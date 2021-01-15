package battle.actives;

import battle.Active;
import base.Critter;

public class Tail extends Active {
    public static double multiplier = 0.8;
    public Tail(Critter acting, String name, double pow) {
        super (acting, name, pow, multiplier);
        this.types.add("Blunt");
    }

    @Override
    public void run(Critter acting, Critter receiving) {
        super.run (acting, receiving);
        if(acting.getEnviro ().getR ().nextInt (10)==0)
            receiving.setStun(receiving.getStun ()+1);
    }
}
