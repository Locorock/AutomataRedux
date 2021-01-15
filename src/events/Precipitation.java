package events;

import base.Enviro;
import base.Event;
import sources.Water;

public class Precipitation extends Event {
    public Precipitation(Enviro epicenter, String name) {
        super (epicenter, name);
    }

    public void update(){
        epicenter.setHumidity (epicenter.getHumidity ()+ strength);
        epicenter.setTemperature (epicenter.getTemperature() -(epicenter.getTemperature ()-20)/5);
        epicenter.getResources ().add(new Water (epicenter, strength *10,0.01));
        epicenter.setRainStr (epicenter.getRainStr ()+ strength);
        epicenter.getResources ().add (new Water(epicenter,strength*200, 0.01));
        epicenter.setFertility (epicenter.getFertility ()+0.5, null);
    }

    public void remove(){
        epicenter.setRainStr (epicenter.getRainStr ()- strength *duration);
    }
}