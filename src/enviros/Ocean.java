package enviros;

import base.Enviro;
import base.World;
import sources.*;

public class Ocean extends Enviro {
    public static final String name = "Ocean";

    public Ocean(double temp, double height, double hum, World w, int x, int y, boolean river){
        super(temp, height, hum, name, w, x, y, river);
        this.setFertility (0, null);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Water (this, 1000000, 0.1));
    }
}
