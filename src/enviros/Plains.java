package enviros;

import base.Enviro;
import base.World;
import sources.*;

public class Plains extends Enviro {
    public static final String name = "Plains";
    public Plains(double temp, double height, double hum, World w, int x, int y, boolean river){
        super(temp, height, hum, name, w, x, y, river);
        this.setFertility (80, null);
        initResources();
    }

    public void initResources(){
        this.getResources ().add(new Bush (this, 5*this.getHumidity ()));
        this.getResources ().add(new Tree (this, 5*this.getHumidity (), false, 6));
        this.getResources ().add(new Grass (this, 300, 2));
    }
}
