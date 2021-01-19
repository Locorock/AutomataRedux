package sources;

import base.Enviro;
import base.Resource;

import java.util.ArrayList;

public class Leaves extends Resource{
    private static final String name = "Leaves";
    private boolean tick = true;
    public Leaves(Enviro e, double amount){
        super (name, e, amount);
    }

    @Override
    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = new ArrayList<> ();
        double decayRate = ((e.getTemperature ()/80)+(e.getHumidity ()/80))*amount*0.05;
        if(decayRate>0 && amount>0){
            double total = amount;
            double decay = this.request (decayRate);
            double fert = (decay/total)*amassedFertility;
            tick = false;
            e.setFertility (e.getFertility ()+fert, this);
            amassedFertility -= fert;
        }
        return resources;
    }
}
