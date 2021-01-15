package base;

import battle.PokeBattle;
import sources.Water;

import java.util.*;

public class DecisionalCore {
    final Critter owner;
    Critter interacting;
    String behaviour = "";
    ArrayDeque<int[]> path;
    Resource next;
    String action;

    public DecisionalCore(Critter c) {
        this.owner = c;
    }

    public String act() {
        action = "";
        if(interacting!=null){
            interacting=null;
            action+="interacting ";
        }else{
            if(owner.getSize () == 0){
                if(owner.getHunger () < owner.getMaxHunger () / 3){
                    owner.split ();
                    action += "split ";
                }else{
                    owner.eatSun ();
                    action += "sun ";
                }
            } else {
                if (owner.getAge () > 15 && lookForMate ()) {
                    action += "found mate ";
                } else {
                    if (owner.getThirst () > 10 * owner.getSize () && lookForWater() != null) {
                        owner.drink(next);
                        action += "drinking ";
                    } else {
                        if(owner.getHunger () > 10 * owner.getSize () && lookForFood () != null){
                            owner.eat(next);
                            action += "eating ";
                        } else {
                            if (owner.getWorld ().getR ().nextInt (200) < owner.getAggressivity () && hunt ()) {
                                action += "hunting ";
                            } else {
                                if(owner.getEnviro ().getR ().nextInt (100) < owner.getWanderlust()){
                                    action = "wander ";
                                    owner.moveTo (wander());
                                    //SMARTER
                                }else{
                                    action = "play ";
                                }
                            }
                        }
                    }
                }
            }
        }

        return action;
    }

    public Enviro wander(){
        Enviro e;
        int attempt = 0;
        do{
            int directionX = owner.getEnviro ().getR ().nextInt (3)-1;
            int directionY = owner.getEnviro ().getR ().nextInt (3)-1;
            e = owner.getWorld ().getEnviroWrapping(directionX+owner.getEnviro ().getX (), directionY+owner.getEnviro ().getY ());
            attempt++;
        }while(e.getBiome ()=="Ocean" && attempt < 2);
        return e;
    }

    public boolean lookForMate() {
        Enviro enviro = owner.getEnviro ();
        for(Critter critter : enviro.getCritters ()){
            if (!critter.equals (owner) && critter.getSize () >= 1) {
                int diff = critter.getCode ().getHammingDiff ("AppearanceCluster", owner.getCode ().getCode ()); //IMPLEMENT MASKING BY INDIRECT CHECKS
                if (diff < owner.getMateTolerance ()) {
                    if (critter.mateHandshake (owner, diff)) {
                        critter.reproduce (owner);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Resource lookForWater(){
        var wrap = new Object () {
            double amount = 1;
            Resource best = null;
        };
        owner.getEnviro ().getResources ().forEach(resource -> {
            if(resource instanceof Water){
                Water water = (Water) resource;
                double request = Math.min(resource.getAmount (), owner.getSize ()*4);
                double gain = request-request*Math.abs((owner.getBloodSalination()-water.getSalination ()));
                if(gain>wrap.amount){
                    wrap.amount = gain;
                    wrap.best = resource;
                }
           }
        });
        this.next = wrap.best;
        return next;
    }

    public Resource lookForFood(){
        var wrap = new Object () {
            double amount = 1;
            Resource best = null;
        };
        owner.getEnviro ().getResources ().forEach(resource -> {
            for (String food : owner.getDiet ().keySet ()) {
                if (Objects.equals (resource.name, food)) {
                    double eff = resource.amount*owner.getDiet ().get(food); // AMOUNT * EFFICIENCY
                    if (eff > wrap.amount) {
                        wrap.amount = eff;
                        wrap.best = resource;
                    }
                }
            }
        });
        this.next = wrap.best;
        if(next!=null){
            action += " "+wrap.amount;
        }
        return next;
    }

    public boolean hunt() {
        Enviro enviro = owner.getEnviro ();
        for (Critter critter : enviro.getCritters ()) {
            if (owner.getCode ().getHammingDiff ("AppearanceCluster", critter.getCode ().getCode ()) > 8) {
                String reaction = critter.alert (owner);
                if (Objects.equals (reaction, "flee")) {
                    if (owner.getSpeed () > critter.getSpeed ()) {
                        new PokeBattle (owner, critter, true);
                    }
                }
                if (Objects.equals (reaction, "approach")) {
                    new PokeBattle (owner, critter, false);
                }
                if (Objects.equals (reaction, "nope")) {
                    new PokeBattle (owner, critter, true);
                }
                return true;
            }
        }
        return false;
    }

    public Critter getInteracting() {
        return interacting;
    }

    public void setInteracting(Critter interacting) {
        this.interacting = interacting;
    }

    public String getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(String behaviour) {
        this.behaviour = behaviour;
    }
}
