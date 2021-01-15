package sources;

import base.Enviro;
import base.Resource;

import java.util.ArrayList;

public class Ice extends Resource {
    private static final String name = "Ice";
    protected double salination;
    public Ice(Enviro e, double amount, double salination) {
        super (name, e, amount);
        this.salination = salination;
    }


    @Override
    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = new ArrayList<> ();
        double A = this.amount;
        double B;
        if(bound == null){
            B = 0;
        }else{
            B = bound.getAmount();
        }
        double target = (A*Math.pow(salination, 1/20))/(-e.getTemperature ());
        double meltRate = A/(-e.getTemperature ())/5;
        if(meltRate+B>target){
            meltRate = target-B;
        }
        if(meltRate>0){
            double melt = this.request (meltRate);
            if(bound == null){
                Resource water = new Water(e, melt, salination);
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
