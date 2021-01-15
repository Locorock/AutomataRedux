package events;

import base.Enviro;
import base.Event;

public class Volcano extends Event {
    public Volcano(Enviro epicenter, String name) {
        super (epicenter, name);
    }

    public void update(){
        epicenter.setEruptionS (epicenter.getEruptionS () + strength);
    }

    public void remove() {
        epicenter.setEruptionS (epicenter.getEruptionS () - strength *duration);
    }
}