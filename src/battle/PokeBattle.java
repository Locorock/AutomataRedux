package battle;

import base.Critter;
import battle.actives.Struggle;

import java.util.ArrayList;
import java.util.TreeSet;

public class PokeBattle {
    private Critter first;
    private Critter second;
    private TreeSet<Active> firstMoves;
    private TreeSet<Active> secondMoves;
    private int turn = 0;
    public PokeBattle(Critter first, Critter second, boolean surprise){
        this.first = first;
        this.second = second;
        firstMoves = (TreeSet<Active>) first.getActives ().clone ();
        secondMoves = (TreeSet<Active>) second.getActives ().clone ();
        for(turn = 0; turn < 100; turn++){
            if((turn==0 && surprise) || first.getSpeed ()>second.getSpeed ()){
                if(turn(first, second, firstMoves) || turn(second, first, secondMoves)){
                    break;
                }
            }else{
                if(turn(second, first, secondMoves) || turn(first, second, firstMoves)){
                    break;
                }
            }
        }
        resolve();
    }

    public boolean turn(Critter acting, Critter receiving, TreeSet<Active> moves){
        if(acting.getStun() > 0){
            acting.setStun(acting.getStun()-1);
        }else{
            double limit = acting.getAggressivity ()-(acting.getMaxHP ()-acting.getHP());
            if(acting.getEnviro ().getR ().nextInt (100)<limit){
                if(moves.isEmpty ()){
                    new Struggle (acting, "Struggle").run(acting, receiving);
                }else{
                    Active active = moves.pollFirst ();
                    active.run(acting, receiving);
                }
            }else{
                int diff = (int) (acting.getSpeed ()-receiving.getSpeed ());
                if(acting.getEnviro ().getR ().nextInt (100)>75-diff){
                    return true;
                }else{
                    return false;
                }
            }
        }

        if(acting.getPoison() > 0){
            acting.setPoison(acting.getPoison()-1);
            ArrayList<String> types = new ArrayList<> ();
            types.add("Poison");
            types.add("Self");
            acting.dealDamage (acting.getMaxHP ()/10, types, acting);
        }
        if(first.getHP ()>0 && second.getHP ()>0){
            return false;
        }else{
            return true;
        }
    }

    public void resolve(){
        if(first.getHP ()<=0 && second.getHP ()<=0){
            first.setAlive (false);
            second.setAlive(false);
            first.getWorld ().kDeaths+=2;
        }else{
            first.setHunger (first.getHunger ()-first.getSize ());
            first.setThirst (first.getThirst ()-first.getSize ()/2);
            second.setHunger (second.getHunger ()-second.getSize ());
            second.setThirst (second.getThirst ()-second.getSize ()/2);
            if(first.getHP ()<=0 || second.getHP()<=0){
                if(first.getHP ()>0){ //IMPLEMENTARE LE CARCASSE
                    second.setAlive(false);
                    first.getWorld ().kDeaths+=1;
                    first.eat(second);
                }else {
                    first.setAlive (false);
                    first.getWorld ().kDeaths+=1;
                    second.eat(first);
                }
            }
        }

    }
}
