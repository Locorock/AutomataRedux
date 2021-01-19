package sources;

import base.Enviro;
import base.Resource;

import java.util.ArrayList;

public class Micro extends Resource{
    private static final String name = "Micro"; //WATCH OUT FOR NaNs / NO FERTILITY VALUE (0)
    private boolean tick = true;
    public Micro(Enviro e, double amount){
        super (name, e, amount);
    }

    public ArrayList<Resource> tick() {
        ArrayList<Resource> resources = new ArrayList<> ();
        if(amount<=1){
            amount = 1;
        }
        double growthRate = Math.abs((e.getTemperature ()+60)/amount);
        amount += growthRate;
        return resources;
    }
}