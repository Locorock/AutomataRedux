package sources;

import base.Enviro;
import base.Resource;

import java.util.ArrayList;

public class Grass extends Resource {
    private final double height;
    private static final String name = "Grass";
    public Grass(Enviro enviro, double amount, double height) {
        super (name, enviro, amount);
        this.height = height;
    }

    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = new ArrayList<> ();
        double growthRate = Math.round((e.getFertility()/100)*(e.getHumidity()/50)*amount*height/4);
        double fert = growthRate/1000;
        Resource res = new Leaves (e, growthRate);
        res.setAmassedFertility (fert);
        res.setBound(this);
        resources.add(res);
        e.setFertility(e.getFertility ()-fert, this);
        return resources;
    }
}
