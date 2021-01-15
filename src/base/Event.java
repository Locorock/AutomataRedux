package base;

import enums.EventList;

import java.util.Random;

public abstract class Event {
    protected Enviro epicenter;
    protected int duration;
    protected int remaining;
    protected String name;
    protected final Random r;
    protected int strength;

    public Event(Enviro epicenter, String name) {
        this.epicenter = epicenter;
        this.name = name;
        this.r = epicenter.getR ();
        generateEvent ();
    }

    public void tick(){
        if(remaining>0){
            this.update ();
        }else{
            remove ();
        }
        this.remaining--;
    }

    public abstract void update();

    public abstract void remove();

    private void generateEvent() {
        EventList eventType = EventList.valueOf (name);
        this.duration = (int) Math.round (eventType.getMeanDuration () + r.nextGaussian () * eventType.getStdDuration ());
        this.strength = (int) Math.round (Math.abs(r.nextGaussian ()*4+2));
        if (duration < 1) {
            duration = 1;
        }
        if(strength < 1){
            strength = 1;
        }
        this.remaining = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Enviro getEpicenter() {
        return epicenter;
    }

    public void setEpicenter(Enviro epicenter) {
        this.epicenter = epicenter;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }
}
