package sources;

import base.Enviro;
import base.Resource;

import java.util.ArrayList;

public class Corpse extends Resource {
    private static final String name = "Corpse";
    public Corpse(Enviro e, double amount, double fert){
        super (name, e, amount);
        this.setAmassedFertility (fert);
    }

    @Override
    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = new ArrayList<> ();
        double decayRate = (e.getTemperature ()/60)+(e.getHumidity ()/60)*amount*0.1;
        if(decayRate>0 && this.amount>0){
            double total = amount;
            double decay = this.request (decayRate);
            double fert = (decay/total)*amassedFertility;
            if(Double.isNaN (e.getFertility ())){
                System.out.println ("ayaya");
            }
            if(Double.isNaN (fert))
            e.setFertility (e.getFertility ()+fert, this);
            this.amassedFertility-= fert;
        }
        return resources;
    }
}
