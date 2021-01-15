package sources;

import base.Enviro;
import base.Resource;

import java.util.ArrayList;

public class Snow extends Resource{
    private static final String name = "Snow";
    public Snow(Enviro e, double amount) {
        super (name, e, amount);
    }

    @Override
    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = new ArrayList<> ();
        double meltRate = (e.getTemperature ()/100)*amount*2;
        if(meltRate>0){
            double melt = this.request (meltRate);
            if(bound == null){
                Resource water = new Water(e, melt, 0.01);
                water.setBound(this);
                resources.add(water);
                this.bound = water;
            }else{
                this.bound.provide (melt);
            }
        }
        return resources;
    }
}
