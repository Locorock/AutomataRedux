package sources;

import base.Enviro;
import base.Resource;
import sources.Fruits;
import sources.Tree;

import java.util.ArrayList;

public class FruitTree extends Tree {
    private static final String name = "FruitTree";
    private static final double baseH = 4;
    public FruitTree(Enviro enviro, double amount) {
        super (name, enviro, amount, false, baseH);
    }

    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = super.tick ();
        double growthRate = Math.round((e.getFertility()/100)*(e.getHumidity()/50)*amount*8);
        double fert = growthRate/700;
        Resource res = new Fruits(e, growthRate);
        res.setAmassedFertility (fert);
        res.setBound(this);
        resources.add(res);
        e.setFertility(e.getFertility ()-fert, this);
        return resources;
    }
}
