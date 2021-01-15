package enviros;

import base.Enviro;
import base.World;
import sources.Cactus;

public class Desert extends Enviro {
    public static final String name = "Desert";
    public Desert(double temp, double height, double hum, World w, int x, int y, boolean river){
        super(temp, height, hum, name, w, x, y, river);
        this.setFertility (20, null);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Cactus (this, 5*this.getHumidity ()));
    }
}
