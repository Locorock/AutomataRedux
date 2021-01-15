package events;

import base.Enviro;

public class Storm extends Precipitation {
    public Storm(Enviro epicenter, String name) {
        super (epicenter, name);
    }

    @Override
    public void update() {
        super.update ();
        epicenter.setLightningStr (epicenter.getLightningStr ()+ strength);
    }

    public void remove(){
        super.remove ();
        epicenter.setLightningStr (epicenter.getLightningStr ()- strength *duration);
    }
}