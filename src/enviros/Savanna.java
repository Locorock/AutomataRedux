package enviros;

import base.Enviro;
import base.World;
import sources.Bush;
import sources.Cactus;
import sources.Grass;
import sources.Tree;

public class Savanna extends Enviro {
    public static final String name = "Savanna";
    public Savanna(double temp, double height, double hum, World w, int x, int y, boolean river, boolean seabound){
        super(temp, height, hum, name, w, x, y, river, seabound);
        this.setFertility (60, null);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Bush (this, 5*this.getHumidity ()));
        this.getResources ().add(new Tree (this, 5*this.getHumidity (), false, 6));
        this.getResources ().add(new Grass (this, 10*this.getHumidity (), 2));
    }
}
