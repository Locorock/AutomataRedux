package enviros;

import base.Enviro;
import base.World;
import sources.*;

public class Wetland extends Enviro {
    public static final String name = "Wetland";
    public Wetland(double temp, double height, double hum, World w, int x, int y, boolean river, boolean seabound){
        super(temp, height, hum, name, w, x, y, river, seabound);
        this.setFertility (120, null);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Water (this, 1000000000, 0.04+ (isSeabound () ? 0.03 : 0)));
        this.getResources ().add(new Tree (this, 7*this.getHumidity (), false, 2));
        this.getResources ().add(new Grass (this, 10*this.getHumidity (), 1));
    }
}
