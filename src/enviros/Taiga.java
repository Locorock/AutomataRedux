package enviros;

import base.Enviro;
import base.World;
import sources.*;

public class Taiga extends Enviro {
    public static final String name = "Taiga";
    public Taiga(double temp, double height, double hum, World w, int x, int y, boolean river, boolean seabound){
        super(temp, height, hum, name, w, x, y, river, seabound);
        this.setFertility (60, null);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Snow (this, 100));
        this.getResources ().add(new Bush (this, 10*this.getHumidity ()));
        this.getResources ().add(new Tree (this, 5*this.getHumidity (), false, 4));
        this.getResources ().add(new FruitTree (this, 10*this.getHumidity ()));
        this.getResources ().add(new Grass (this, 20*this.getHumidity (), 1));
    }
}
