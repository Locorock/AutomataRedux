package sources;

import base.Enviro;
import base.Resource;
import sources.Leaves;

import java.util.ArrayList;

public class Tree extends Resource {
    public static String name = "Tree";
    protected double height;
    protected boolean deciduous;
    public Tree(String name, Enviro e, double amount, boolean deciduous, double baseH) {
        super (name, e, amount);
        this.deciduous = deciduous;
        this.height = Math.abs (baseH + (e.getR ().nextGaussian () * baseH / 4));
    }

    public Tree(Enviro e, double amount, boolean deciduous, double baseH) {
        super (name, e, amount);
        this.deciduous = deciduous;
        this.height = Math.abs (baseH + (e.getR ().nextGaussian () * baseH / 4));
    }

    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = new ArrayList<> ();
        double growthRate = Math.round((e.getFertility()/100)*(e.getHumidity()/50)*amount);
        double fert = growthRate/1000;
        Resource res = new Leaves (e, growthRate);
        res.setAmassedFertility (fert);
        resources.add(res);
        e.setFertility(e.getFertility ()-fert, this);
        return resources;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isDeciduous() {
        return deciduous;
    }

    public void setDeciduous(boolean deciduous) {
        this.deciduous = deciduous;
    }
}
