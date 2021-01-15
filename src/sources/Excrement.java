package sources;

import base.Enviro;
import base.Resource;

import java.util.ArrayList;

public class Excrement extends Resource {
    private static final String name = "Excrement";
    public double delta = 0;
    public Excrement(Enviro e, double amount, double fertilityDividend){
        super (name, e, amount);
        delta+=amount;
    }

    @Override
    public double provide(double provided){
        delta += provided;
        return super.provide (provided);
    }

    @Override
    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = new ArrayList<> ();
        double decayRate = (e.getTemperature ()/40)+(e.getHumidity ()/40)*amount*0.15;
        if(decayRate>0){
            double total = amount;
            double decay = this.request (decayRate);
            double fert = (decay/total)*amassedFertility;
            e.setFertility (e.getFertility ()+fert, this);
            delta-=fert;
            this.amassedFertility-= fert;
        }
        return resources;
    }
}
