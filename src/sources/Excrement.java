package sources;

import base.Enviro;
import base.Resource;

import java.util.ArrayList;

public class Excrement extends Resource {
    private static final String name = "Excrement";
    public double delta = 0;
    public static boolean first = true;
    public boolean track = false;
    public Excrement(Enviro e, double amount, double fert){
        super (name, e, amount);
        if(first){
            track = true;
            first = false;
        }
        this.amassedFertility = fert;
        delta+=fert;
        if(track){
            System.out.println ("amount "+fert);
        }
    }

    @Override
    public void setAmassedFertility(double fertility){
        super.setAmassedFertility (fertility);
        if(track){
        }
        delta+=fertility;
    }

    @Override
    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = new ArrayList<> ();
        double decayRate = ((e.getTemperature ()/40)+(e.getHumidity ()/40))*amount*0.15;
        double decayRateB = ((e.getTemperature ()/40)+(e.getHumidity ()/40))*amassedFertility*0.15;
        if(decayRate>0){
            double decay = this.request (decayRate);
        }
        if(decayRateB>0){
            e.setFertility (e.getFertility ()+decayRateB, this);
            delta-=decayRateB;
            this.amassedFertility-= decayRateB;
        }
        return resources;
    }
}
