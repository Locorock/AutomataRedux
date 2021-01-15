package sources;

import base.Enviro;
import base.Resource;

import java.util.ArrayList;

public class Water extends Resource {
    private static final String name = "Water";
    protected double salination;
    public Water(Enviro e, double amount, double salination){
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
        double target = (A*(-e.getTemperature ()/100))/Math.pow(salination, 1/20);
        double freezeRate = A/(-e.getTemperature ())/5;
        if(freezeRate+B>target){
            freezeRate = target-B;
        }
        if(freezeRate>0){
            double freeze = this.request (freezeRate);
            if(bound == null){
                Resource ice = new Ice(e, freeze, salination);
                ice.setBound(this);
                resources.add(ice);
                this.bound = ice;
            }else{
                this.bound.provide (freeze);
            }
        }
        return resources;
    }

    public double getSalination() {
        return salination;
    }

    public void setSalination(double salination) {
        this.salination = salination;
    }
}