package events;

import base.Enviro;
import base.Event;

public class Earthquake extends Event {
    public Earthquake(Enviro epicenter, String name) {
        super (epicenter, name);
    }

    public void update(){
        epicenter.setQuakeStr (epicenter.getQuakeStr ()+ strength);
    }

    public void remove(){
        epicenter.setQuakeStr (epicenter.getQuakeStr ()- strength *duration);
    }
}
