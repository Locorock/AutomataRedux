package sources;

import base.Enviro;
import base.Resource;

import java.util.ArrayList;

public class Corpse extends Resource {
    private static final String name = "Corpse";
    public double delta = 0;
    public Corpse(Enviro e, double amount, double fert){
        super (name, e, amount);
        this.setAmassedFertility (fert);
        delta+=fert;
    }

    @Override
    public void setAmassedFertility(double fertility){
        super.setAmassedFertility (fertility);
        delta+=fertility;
    }

    @Override
    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = new ArrayList<> ();
        double decayRate = ((e.getTemperature ()/60)+(e.getHumidity ()/60))*amount*0.01;
        double decayRateB = ((e.getTemperature ()/60)+(e.getHumidity ()/60))*amassedFertility*0.01;
        if(decayRate>0 && this.amount>0){
            double decay = this.request (decayRate);
        }
        if(decayRateB>0){
            e.setFertility (e.getFertility ()+decayRateB, this);
            this.amassedFertility-= decayRateB;
            delta-=decayRateB;
        }
        return resources;
    }
}
