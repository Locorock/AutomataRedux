package battle.passives;

import base.Critter;
import battle.Passive;

import java.util.ArrayList;

public class Spikes extends Passive {
    public Spikes(Critter receiving, String name, double pow) {
        super (receiving, name, pow);
    }

    @Override
    public double modify(double value, ArrayList<String> types, Critter acting) {
        if(!types.contains ("Ranged") && !types.contains ("Self")){
            ArrayList<String> t = new ArrayList<> ();
            t.add("Piercing");
            t.add("Ranged");
            acting.dealDamage ((value/(256/pow)), t, receiving);
        }
        return super.modify (value, types, acting);
    }
}
