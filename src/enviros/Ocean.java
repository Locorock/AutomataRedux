package enviros;

import base.Enviro;
import base.World;
import sources.*;

public class Ocean extends Enviro {
    public static final String name = "Ocean";

    public Ocean(double temp, double height, double hum, World w, int x, int y, boolean river, boolean seabound){
        super(temp, height, hum, name, w, x, y, river, seabound);
        this.setFertility (50, null);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Water (this, 100000000, 0.1));
        this.getResources ().add(new Grass (this, 30, 1));
        this.getResources ().add(new Micro (this, 10));

    }
}
