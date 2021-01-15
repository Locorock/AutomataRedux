package sources;

import base.Enviro;
import base.Resource;
import sources.Leaves;

import java.util.ArrayList;

public class Cactus extends Resource {
    public static String name = "Cactus";
    public Cactus(Enviro e, double amount) {
        super (name, e, amount);
    }

    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = new ArrayList<> ();
        double growthRate = (e.getFertility()/100)*(e.getHumidity()/20)*amount/4;
        double fert = growthRate/2000;
        if(bound == null){
            Resource res = new Leaves (e, growthRate);
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
