package sources;

import base.Enviro;
import base.Resource;
import sources.Bush;

import java.util.ArrayList;

public class BerryBush extends Bush {
    private static final String name = "BerryBush";
    public BerryBush(Enviro enviro, double amount) {
        super (name, enviro, amount);
    }

    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = super.tick ();
        double growthRate = (e.getFertility()/100)*(e.getHumidity()/50)*amount;
        double fert = growthRate/700;
        if(bound == null){
            Resource res = new Fruits(e, growthRate);
            res.setAmassedFertility (fert);
            res.setBound(this);
            resources.add(res);
            this.bound = res;
        }else{
            this.bound.provide (growthRate);
            this.bound.setAmassedFertility (this.bound.getAmassedFertility ()+fert);
        }
        e.setFertility(e.getFertility ()-fert, this);
        return resources;
    }
}
