package enviros;

import base.Enviro;
import base.World;
import sources.*;

public class Forest extends Enviro {
    public static final String name = "Forest";
    public Forest(double temp, double height, double hum, World w, int x, int y, boolean river){
        super(temp, height, hum, name, w, x, y, river);
        this.setFertility (100, null);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Bush (this, 10*this.getHumidity ()));
        this.getResources ().add(new Tree (this, 5*this.getHumidity (), false, 4));
        this.getResources ().add(new FruitTree (this, 10*this.getHumidity ()));
        this.getResources ().add(new BerryBush (this, 5*this.getHumidity ()));
        this.getResources ().add(new Grass (this, 20*this.getHumidity (), 1));
    }
}
