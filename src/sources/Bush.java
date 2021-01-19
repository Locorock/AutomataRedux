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
        double growthRate = Math.round((e.getFertility()/100)*(e.getHumidity()/50)*amount);
        double fert = growthRate/1000;
        Resource res = new Leaves(e, growthRate);
        res.setAmassedFertility (fert);
        if(Double.isNaN (fert)){
            System.out.println ("N");
        }
        resources.add(res);
        e.setFertility(e.getFertility ()-fert, this);
        return resources;
    }
}
