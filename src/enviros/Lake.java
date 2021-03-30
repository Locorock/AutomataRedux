package enviros;

import base.Enviro;
import base.World;
import sources.*;

public class Lake extends Enviro {
    public static final String name = "Lake";

    public Lake(double temp, double height, double hum, World w, int x, int y, boolean river, boolean seabound){
        super(temp, height, hum, name, w, x, y, river, seabound);
        this.setFertility (50, null);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Water (this, 100000000, 0.01));
        this.getResources ().add(new Grass (this, 50*this.getHumidity (), 2));
        this.getResources ().add(new Micro (this, 100));
    }
}
