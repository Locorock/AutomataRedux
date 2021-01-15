package enviros;

import base.Enviro;
import base.World;
import sources.*;

public class Jungle extends Enviro {
    public static final String name = "Jungle";
    public Jungle(double temp, double height, double hum, World w, int x, int y, boolean river){
        super(temp, height, hum, name, w, x, y, river);
        this.setFertility (140, null);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Bush (this, 5*this.getHumidity ()));
        this.getResources ().add(new Tree (this, 15*this.getHumidity (), false, 10));
        this.getResources ().add(new FruitTree (this, 10*this.getHumidity ()));
        this.getResources ().add(new Grass (this, 10*this.getHumidity (), 3));
    }
}
