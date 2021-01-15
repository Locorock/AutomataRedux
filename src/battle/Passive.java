package battle;

import base.Critter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Passive {
    protected HashMap<String, Double> resistances = new HashMap<> ();
    protected Critter receiving;
    protected double pow;
    protected String name;
    public Passive(Critter receiving, String name, double pow){
        this.name = name;
        this.receiving = receiving;
        this.pow = pow;
    }
    public double modify(double value, ArrayList<String> types, Critter acting){
        double actual = value;
        for(String type : types){
            if(resistances.containsKey (type)){
                actual = actual * resistances.get (type);
            }
        }
        return actual;
    }
}
