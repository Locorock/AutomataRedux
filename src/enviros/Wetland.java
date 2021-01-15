package enviros;

import base.Enviro;
import base.World;
import sources.*;

public class Wetland extends Enviro {
    public static final String name = "Wetland";
    public Wetland(double temp, double height, double hum, World w, int x, int y, boolean river){
        super(temp, height, hum, name, w, x, y, river);
        this.setFertility (120, null);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Water (this, 10000, 0.01));
        this.getResources ().add(new Tree (this, 10*this.getHumidity (), false, 2));
        this.getResources ().add(new Grass (this, 20*this.getHumidity (), 1));
    }
}
