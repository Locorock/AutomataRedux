package base;

import battle.PokeBattle;
import enviros.Ocean;
import enviros.Wetland;
import sources.*;

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
                            if (owner.getWorld ().getR ().nextInt (50000) < owner.getHunger ()*owner.getDiet ().get ("Corpse")*owner.getAggressivity ()*(owner.getEnviro ().getCritters ().size ()/20) && hunt ()) {
                                action += "hunting ";
                            } else {
                                if(owner.getEnviro ().getR ().nextInt (20000) < 20 && (owner.getThirst () > 40 * owner.getSize () || owner.getHunger () > 70 * owner.getSize ())){
                                    action = "look for something more";
                                    owner.moveTo (wander());
                                    //SMARTER
                                }else{
                                    if(owner.getEnviro ().getR ().nextInt (100000) < owner.getWanderlust()){
                                        action = "wander ";
                                        owner.moveTo (wander());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return action;
    }

    public Enviro newWander(){
        Enviro best = null;
        int bestValue = -1000;
        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                Enviro e = owner.getWorld ().getEnviroWrapping(j+owner.getEnviro ().getX (), i+owner.getEnviro ().getY ());
                double value = 0;
                for(Resource r: e.getResources ()){
                    if(r instanceof Water && owner.getThirst ()>50){
                        if(Math.abs(((Water) r).getSalination ()-owner.getBloodSalination ())<0.3){
                            value += 1;
                        }
                    }
                    if(owner.getHunger ()>50){
                        if(r instanceof Leaves && owner.getDiet ().get ("Leaves")>0.3){
                            value += 1;
                        }
                        if(r instanceof Fruits && owner.getDiet ().get ("Fruit")>0.3){
                            value += 1;
                        }
                        if(r instanceof Micro && owner.getDiet ().get ("Corpse")>0.3){
                            value += 1;
                        }
                    }
                }
                if(value>0){
                    value = 1;
                }else{
                    value = -1;
                }
                if(owner.getSpeed ()-owner.getWebbedFeet ()<0){
                    if(e instanceof Ocean || e instanceof Wetland || e.isRiver ()){
                        value += 1;
                    }else {
                        value -=1;
                    }
                }else{
                    if(e instanceof Ocean){
                        value -=1;
                    }else{
                        value +=1;
                    }
                }
                value += e.getR ().nextInt (5);
                if(value>bestValue){
                    best = e;
                    bestValue = (int) value;
                }
            }
        }
        return best;
    }

    public Enviro wander(){
        Enviro e;
        int attempt = 0;
        do{
            int directionX = owner.getEnviro ().getR ().nextInt (3)-1;
            int directionY = owner.getEnviro ().getR ().nextInt (3)-1;
            e = owner.getWorld ().getEnviroWrapping(directionX+owner.getEnviro ().getX (), directionY+owner.getEnviro ().getY ());
            attempt++;
        }while(e.getBiome ()=="Ocean" && attempt < 1);
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
            double amount = -10;
            Resource best = null;
        };
        owner.getEnviro ().getResources ().forEach(resource -> {
            if(resource instanceof Water){
                Water water = (Water) resource;
                double request = Math.min(resource.getAmount (), owner.getSize ()*4);
                double gain = request-request*Math.abs((owner.getBloodSalination()-water.getSalination ())*40); // 0.02 -- 0.08 in 0.05 enviro
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
            double amount = 0.05*owner.getSize ();
            Resource best = null;
        };
        owner.getEnviro ().getResources ().forEach(resource -> {
            double eff = evalResource (resource);
            if (eff > wrap.amount) {
                wrap.amount = eff;
                wrap.best = resource;
            }
        });
        this.next = wrap.best;
        if(next!=null){
            action += " "+wrap.amount+" "+owner.getDiet().get (wrap.best.name)+" "+owner.getCode ().getGeneValue ("LeavesEfficiency");
        }
        return next;
    }

    public double evalResource(Resource resource){
        boolean valid = true;
        if(resource instanceof Tree && (((Tree) resource).getHeight ())>owner.getHeight()){
            valid = false;
        }
        if(valid){
            for (String food : owner.getDiet ().keySet ()) {
                if (Objects.equals (resource.name, food)) {
                    double actual = owner.getSize ()*20;
                    if(resource.amount<owner.getSize ()){
                        actual = resource.amount;
                    }
                    double eff = actual*owner.getDiet ().get(food); // EATABLE AMOUNT * EFFICIENCY
                    return eff;
                }
            }
        }
        return 0;
    }

    public boolean hunt() {
        Enviro enviro = owner.getEnviro ();
        for (Critter critter : enviro.getCritters ()) {
            if (owner.getCode ().getHammingDiff ("AppearanceCluster", critter.getCode ().getCode ()) >= 0) {
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
