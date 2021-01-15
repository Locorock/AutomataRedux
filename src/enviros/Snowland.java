package enviros;

import base.Enviro;
import base.World;
import sources.Bush;
import sources.Cactus;
import sources.Snow;
import sources.Tree;

public class Snowland extends Enviro {
    public static final String name = "Snowland";
    public Snowland(double temp, double height, double hum, World w, int x, int y, boolean river){
        super(temp, height, hum, name, w, x, y, river);
        this.setFertility (50, null);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Snow (this, 200));
        this.getResources ().add(new Bush (this, 5*this.getHumidity ()));
        this.getResources ().add(new Tree (this, 10*this.getHumidity (), false, 6));
    }
}
