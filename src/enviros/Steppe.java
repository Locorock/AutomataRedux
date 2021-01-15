package enviros;

import base.Enviro;
import base.World;
import sources.Bush;
import sources.Cactus;
import sources.Grass;
import sources.Tree;

public class Steppe extends Enviro {
    public static final String name = "Steppe";
    public Steppe(double temp, double height, double hum, World w, int x, int y, boolean river){
        super(temp, height, hum, name, w, x, y, river);
        this.setFertility (40, null);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Grass (this, 20*this.getHumidity (), 0.5));
    }
}
