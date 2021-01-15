package enviros;

import base.Enviro;
import base.World;
import sources.Ice;
import sources.Water;

public class Glacial extends Enviro {
    public static final String name = "Glacial";
    public Glacial(double temp, double height, double hum, World w, int x, int y, boolean river){
        super(temp, height, hum, name, w, x, y, river);
        this.setFertility (40, null);
        initResources();
    }

    public void initResources(){
        Ice ice = new Ice (this, 100000, 0.1);
        Water water = new Water (this, 1000000, 0.1);
        ice.setBound (water);
        water.setBound (ice);
        this.getResources ().add(ice);
        this.getResources ().add(water);

    }
}
