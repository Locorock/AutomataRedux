package sources;

import base.Enviro;
import base.Resource;

import java.util.ArrayList;

public class Fruits extends Resource{
    private static final String name = "Fruits";
    public Fruits(Enviro e, double amount){
        super (name, e, amount);
    }

    @Override
    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = new ArrayList<> ();
        double decayRate = ((e.getTemperature ()/60)+(e.getHumidity ()/60))*amount*0.1;
        if(decayRate>0 && amount>0){
            double total = amount;
            double decay = this.request (decayRate);
            double fert = (decay/total)*amassedFertility;
            e.setFertility (e.getFertility ()+fert, this);
            this.amassedFertility-= fert;
        }
        return resources;
    }
}
