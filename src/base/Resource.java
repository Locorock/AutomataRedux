package base;

import java.util.ArrayList;

public abstract class Resource {
    protected final String name;
    protected final Enviro e;
    protected double amount;
    protected boolean exhausted = false;
    protected double amassedFertility = 0;
    protected Resource bound;
    public Resource(String name, Enviro e, double amount){
        this.name = name;
        this.e = e;
        this.amount = amount;
    }

    public abstract ArrayList<Resource> tick();

    public double request(double request){
        double returned = request;
        if(amount==Integer.MAX_VALUE){
            return returned;
        }

        returned = Math.min (request, amount);

        amount-=returned;

        if(bound == null){
            exhausted = amount <= 0;
        }else{
            exhausted = false;
        }
        if(Double.isNaN(returned)){
            System.out.println ("returned" + returned);
            System.out.println ("amount" + amount);
        }

        return returned;
    }

    public double provide(double provided){
        if(Double.isNaN(provided)){
            System.out.println ("provided"+ provided);
            System.out.println ("amount" + amount);
        }
        if(amount!=Integer.MAX_VALUE){
            amount+=provided;
        }
        if(Double.isNaN(provided)){
            System.out.println ("provided"+ provided);
            System.out.println ("amount" + amount);
        }
        return provided;
    }

    public void addFertility(double fert){
        if(fert>0){
            this.amassedFertility += fert;
        }
    }

    public double removeFertility(double fert){
        if(fert>0){
            if(fert > amassedFertility){
                fert = amassedFertility;
            }
            this.amassedFertility -= fert;
            return fert;
        }
        return 0;
    }

    public Resource getBound() {
        return bound;
    }

    public void setBound(Resource bound) {
        this.bound = bound;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isExhausted() {
        return exhausted;
    }

    public void setExhausted(boolean exhausted) {
        this.exhausted = exhausted;
    }

    public String getName() {
        return name;
    }

    public double getAmassedFertility(){
        return amassedFertility;
    }

    public void setAmassedFertility(double amassedFertility) {
        this.amassedFertility = amassedFertility;
    }
}
