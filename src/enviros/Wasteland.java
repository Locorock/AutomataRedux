package enviros;

import base.Enviro;
import base.World;
import sources.Bush;
import sources.Cactus;

public class Wasteland extends Enviro {
    public static final String name = "Wasteland";
    public Wasteland(double temp, double height, double hum, World w, int x, int y, boolean river){
        super(temp, height, hum, name, w, x, y, river);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Bush (this, 20*this.getHumidity ()));
    }
}
