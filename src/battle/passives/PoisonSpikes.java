package battle.passives;

import base.Critter;
import battle.Passive;

import java.util.ArrayList;

public class PoisonSpikes extends Passive {
    public PoisonSpikes(Critter receiving, String name, double pow) {
        super (receiving, name, pow);
    }

    @Override
    public double modify(double value, ArrayList<String> types, Critter acting) {
        if(!types.contains ("Ranged") && !types.contains ("Self")){
            ArrayList<String> t = new ArrayList<> ();
            t.add("Piercing");
            acting.setPoison (acting.getPoison ()+(int)pow/5);
        }
        return super.modify (value, types, acting);
    }
}
