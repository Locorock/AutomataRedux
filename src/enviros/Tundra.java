package enviros;

import base.Enviro;
import base.World;
import sources.*;

public class Tundra extends Enviro {
    public static final String name = "Tundra";
    public Tundra(double temp, double height, double hum, World w, int x, int y, boolean river){
        super(temp, height, hum, name, w, x, y, river);
        this.setFertility (30, null);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Snow (this, 200));
        this.getResources ().add(new Bush (this, 5*this.getHumidity ()));
        this.getResources ().add(new Ice (this, 100, 0.01));
        this.getResources ().add(new Grass (this, 20*this.getHumidity (), 0.1));
    }
}
