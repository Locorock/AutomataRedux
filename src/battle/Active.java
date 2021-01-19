package battle;

import base.Critter;

import java.util.ArrayList;

public abstract class Active {
    protected String name;
    protected double pow;
    protected Critter owner;

    protected double multiplier;
    protected ArrayList<String> types = new ArrayList<> ();

    public Active(Critter owner, String name, double pow, double multiplier){
        this.owner = owner;
        this.name = name;
        this.pow = pow;
        this.multiplier = multiplier;
    }

    public void run(Critter acting, Critter receiving){
        receiving.dealDamage (pow*multiplier*owner.getSize (), types, acting);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPow() {
        return pow;
    }

    public void setPow(double pow) {
        this.pow = pow;
    }
}
