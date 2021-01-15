package sources;

import base.Enviro;
import base.Resource;

import java.util.ArrayList;

public class Bush extends Resource {
    public static String name = "Bush";
    public Bush(String name, Enviro enviro, double amount) {
        super (name, enviro, amount);
    }

    public Bush(Enviro enviro, double amount) {
        super (name, enviro, amount);
    }

    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = new ArrayList<> ();
        double growthRate = (e.getFertility()/100)*(e.getHumidity()/50)*amount;
        double fert = growthRate/1000;
        if(bound == null){
            Resource res = new Leaves(e, growthRate);
            res.setAmassedFertility (fert);
            if(Double.isNaN (fert)){
                System.out.println ("N");
            }
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
