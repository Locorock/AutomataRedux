package base;

import battle.Active;
import battle.Passive;
import jdk.swing.interop.SwingInterOpUtils;
import sources.Excrement;
import sources.Water;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class Critter implements Comparable<Critter>{
    public static long id = 0;

    public String name;
    private World world;
    private Enviro enviro;

    private GenCode code;
    private final DecisionalCore decisionalCore;
    private HashMap<String, Double> diet;
    private TreeSet<Active> actives;
    private ArrayList<Passive> passives;
    private ArrayList<String> actions;

    private double thirst;
    private double hunger;
    private double HP;

    private double maxThirst = 100;
    private double maxHunger = 100;
    private double maxHP;

    private double speed;
    private int mateTolerance = 100000;
    private double mateRate;
    private double webbedFeet;
    private double aggressivity;
    private int size;
    private double wanderlust;
    private double stealth;
    private double perception;
    private double bloodSalination;
    private int timeToLive;

    private int failureInteger = 0;

    private double biomass;
    private double amassedFertility;
    private double age = 0;
    private int mateElapsedTime = 0;

    private int poison = 0;
    private int stun = 0;

    private int mateCooldown;
    private boolean alive = true;



    public Critter(String name, World w, Enviro e) {
        this (name, w, e ,new GenCode (w.getR ()), 0, 0);
        this.hunger = 0;
        this.thirst = 0;
        this.age = 35;
        //EXPERIMENT
    }

    public Critter(String name, World w, Enviro e, Critter father, Critter mother, double amassedFertility, double biomass) {
        this (name, w, e,new GenCode (father.getCode (), mother.getCode (), w.getR ()), amassedFertility, biomass);
        this.code.mutate (w.getR (), w.data.critterParams.get ("mutationRate"));
        this.amassedFertility = amassedFertility;
        this.hunger = this.maxHunger/3;
        this.thirst = this.maxThirst/3;
    }

    public Critter(String name, World w, Enviro e, GenCode code, double amassedFertility, double biomass) {
        this.world = w;
        this.enviro = e;
        this.name = name + "_" + id++;
        this.code = code;
        this.decisionalCore = new DecisionalCore (this);
        this.actions = new ArrayList<> ();
        this.actives =  new TreeSet<> (Comparator.comparing (Active::getPow));
        this.passives = new ArrayList<> ();

        this.buildTraits ();

        this.size++; //0-->1
        this.biomass = biomass;
        this.amassedFertility = amassedFertility;
        this.maxHunger = (int) (this.maxHunger * size);
        this.maxThirst = (int) (this.maxThirst * size);
        this.hunger = this.maxHunger / 3;
        this.thirst = this.maxThirst / 3;
    }

    public void buildTraits() {
        this.mateRate = getScaledGene ("MateRate");
        this.speed = getScaledGene ("BaseSpeed");
        this.aggressivity = getScaledGene ("Aggressivity");
        this.webbedFeet = getScaledGene ("WebbedFeet");
        this.size = (int) code.getGeneValue ("Size");
        this.bloodSalination = getScaledGene ("BloodSalination");
        this.wanderlust = getScaledGene ("Wanderlust");
        this.stealth = getScaledGene ("Stealth");
        this.perception = getScaledGene ("Perception");
        this.timeToLive = (int) ((world.data.critterParams.get ("longevityBaseValue") + (world.getR ().nextInt (50)) - (mateRate * world.data.critterParams.get ("longevityMateMultiplier"))) + size * world.data.critterParams.get ("longevitySizeMultiplier"));
        this.mateCooldown = (int) (45 / mateRate * 2);
        buildDiet();
        buildAbilities ();
        stabilityCheck();
    }

    public void stabilityCheck(){
        double total = 0;
        for(GeneLibrary.GeneIds gene : GeneLibrary.GeneIds.values ()){
            if(gene.getInstability ()>0){
                double value = (Integer) code.getGeneValue (gene.getName ())*gene.getInstability ();
                total += value;
            }
        }
        if(total > world.data.critterParams.get("stabilityThreshold")){
            double instability = total - world.data.critterParams.get("stabilityThreshold");
            this.timeToLive -= instability;
            this.mateCooldown += instability;
            this.failureInteger = (int) instability;
            //ADD STUFF IF NOT ENOUGH
        }
    }

    public void buildDiet(){
        double leavesEff = shiftToRange(0,1, (Integer) code.getGeneValue ("LeavesEfficiency"), code.getGene ("LeavesEfficiency").size ());
        double meatEff = shiftToRange(0,1, (Integer) code.getGeneValue ("MeatEfficiency"), code.getGene ("MeatEfficiency").size ());
        double fruitEff = shiftToRange(0,1, (Integer) code.getGeneValue ("FruitEfficiency"), code.getGene ("FruitEfficiency").size ());

        this.diet = new HashMap<> ();
        this.diet.put("Leaves", leavesEff);
        this.diet.put("Corpse", meatEff);
        this.diet.put("Fruit", fruitEff);
    }

    public void buildAbilities(){
        boolean actives = false;
        boolean passives = false;
        for(GeneLibrary.GeneIds values : GeneLibrary.GeneIds.values ()){
            if(values.getName ().equals ("ActiveStart")){
                actives = true;
                continue;
            }
            if(values.getName ().equals ("PassiveStart")){
                passives = true;
                continue;
            }
            if(values.getName ().equals ("ActiveEnd")){
                actives = false;
                continue;
            }
            if(values.getName ().equals ("PassiveEnd")){
                passives = false;
                continue;
            }
            if(actives){
                this.actives.add(buildActive (values.getName (), (Integer)code.getGeneValue (values.getName ())));
            }
            if(passives){
                this.passives.add(buildPassive (values.getName (), (Integer)code.getGeneValue (values.getName ())));
            }
        }
    }

    private Passive buildPassive(String name, double pow) {
        Passive instance = null;
        try {
            instance = (Passive) Class.forName ("battle.passives." + name)
                    .getDeclaredConstructor (new Class[]{Critter.class, String.class, double.class})
                    .newInstance (this, name, pow);
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace ();
        }
        return instance;
    }

    private Active buildActive(String name, double pow) {
        Active instance = null;
        try {
            instance = (Active) Class.forName ("battle.actives." + name)
                    .getDeclaredConstructor (new Class[]{Critter.class, String.class, double.class})
                    .newInstance (this, name, pow);
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace ();
        }
        return instance;
    }

    public void tick() {
        //System.out.println (name + " / " + (int)(hunger) + " / " + (int)(thirst));
        String action = "";
        if (this.thirst > this.maxThirst || this.hunger > this.maxHunger || age > timeToLive) {
            this.alive = false;
            if (this.thirst > this.maxThirst) {
                world.tDeaths++;
            }
            if (this.hunger > this.maxHunger) {
                world.fDeaths++;
            }
            if (this.age > this.timeToLive) {
                world.aDeaths++;
            }
            if(Double.isNaN (this.amassedFertility) || Double.isNaN (this.biomass)){
                System.out.println ("nope");
            }
            return;
        } else {
            this.thirst += 0.05 * Math.pow(size, 1.1);
            this.hunger += 0.1 * Math.pow(size, 1.1);
            this.age += 0.1;
            if (mateElapsedTime > 0) {
                mateElapsedTime--;
            }
        }
        double start = System.nanoTime ();
        if(failureInteger==0 || world.getR ().nextInt (100)>failureInteger){
            action += decisionalCore.act ();
        }else{
            action += "Failure";
        }

        if (action != null && !action.equals ("")) {
            String sub = action.substring (0, 2);
            try {
                world.getT().times.put (sub, world.getT().times.get (sub) + (System.nanoTime () - start) / 1000000);
            } catch (NullPointerException e) {
                world.getT().times.put (sub, (System.nanoTime () - start) / 1000000);
            }

            actions.add (action);
        }
        if (actions.size () > 20) {
            actions.remove (0);
        }
    }

    public boolean mateHandshake(Critter critter, int diff) {
        if (diff < mateTolerance) {
            if (this.decisionalCore.getInteracting () == null && this.mateElapsedTime == 0 && this.age > 35) {
                if (this.hunger < (this.maxHunger - this.maxHunger / world.data.critterParams.get ("mateCostHungerDenom"))) {
                    if (this.thirst < (this.maxThirst - this.maxThirst / world.data.critterParams.get ("mateCostThirstDenom"))) {
                        decisionalCore.setInteracting (critter);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void reproduce(Critter father) {
        Critter child = new Critter ("Salino", world, enviro, father, this, this.getAmassedFertility ()/10, this.biomass/10);
        world.getCritters ().add (child);
        this.enviro.getCritters ().add (child);
        this.mateElapsedTime = mateCooldown;
        this.hunger += this.maxHunger / world.data.critterParams.get ("mateCostHungerDenom");
        this.thirst += this.maxThirst / world.data.critterParams.get ("mateCostThirstDenom");
        this.amassedFertility-=this.amassedFertility/10;
        this.biomass-=this.biomass/10;
    }

    //CELLULAR FUNCTIONS

    public void split() {
        Critter child = new Critter ("Beehhh", world, enviro, this.code, this.getAmassedFertility ()/2, this.biomass/2);
        child.getCode ().mutate (world.getR (), world.data.critterParams.get ("mutationRate"));
        world.getCritters ().add (child);
        this.enviro.getCritters ().add (child);
        this.hunger += this.maxHunger / 2;
        this.thirst += this.maxThirst / 2;
        this.amassedFertility -= this.amassedFertility/2;
        this.biomass -= this.biomass/2;
    }

    public void eatSun() {
        Enviro e = this.getEnviro ();
        int shared = 0;
        for (Critter c : e.getCritters ()) {
            if (c.getSize () == this.getSize ()) {
                shared++;
            }
        }
        double amount = (1 - (shared * 0.1)) * this.size;
        if (amount < 0) {
            amount = 0;
        }
        this.hunger -= amount;
        this.thirst = 0;
        this.biomass += amount/10;
        this.amassedFertility += amount/100;
        enviro.setFertility (enviro.getFertility ()-amount/100, null);
    }

    public void eat(Resource food){
        double total = food.amount;
        double amount = food.request (this.getSize ()*4);
        double fert = (amount / total) * food.amassedFertility;
        food.amassedFertility -= fert;

        double amountA = amount*diet.get (food.name);
        double fertA = fert*diet.get(food.name);
        double amountB = amount - amountA;
        double fertB = fert - fertA;

        this.biomass = amountA/10;
        this.amassedFertility += fertA;
        this.hunger -= amountA;


        if(!(Double.isNaN (amountB) || Double.isNaN (fertB))){
            Resource ex = new Excrement (enviro, amountB, fertB);
            enviro.merge(ex);
        }else{
            System.out.println ("notto guddo 2");
        }
    }

    public void eat(Critter critter){
        double amount = critter.getBiomass ();
        double amountA = amount*diet.get ("Corpse");
        double amountB = amount - amountA;
        double fert = critter.getAmassedFertility ();
        double fertA = fert*diet.get ("Corpse");
        double fertB = fert-fertA;

        this.biomass = amountA/10;
        this.amassedFertility += fertA;
        this.hunger -= amountA;

        Resource ex = new Excrement (enviro, amountB, fertB);
        enviro.merge(ex);
    }

    public void drink(Resource res){
        Water water = (Water) res;
        double amount = water.request (this.getSize ()*4);
        double gain = getSize ()-getSize()*((getBloodSalination()-water.getSalination ())); // DA AGGIUNGERE DI NUOVO IL X10
        this.thirst -= gain;
    }

    public void moveTo(Enviro e) {
        this.enviro.getCritters ().remove (this);
        this.enviro = e;
        this.enviro.getCritters ().add (this);
        this.hunger += 5 * Math.pow(size,1.1);
        this.thirst += 2.5 * Math.pow(size,1.1);
    }

    public String alert(Critter other) {
        decisionalCore.setInteracting (other);
        int chance = (int) ((other.getStealth()/(this.perception+1))*100);
        if(enviro.getR ().nextInt (chance+1)>50){
            if(enviro.getR ().nextInt ((int)aggressivity+1)>50){
                return "approach";
            }else{
                return "flee";
            }
        }else{
            return "nope";
        }
    }

    public double dealDamage(double value, ArrayList<String> types, Critter acting){
        double actual = value;
        for(Passive p: passives){
            actual = p.modify(actual, types, acting);
        }
        this.HP -= Math.round(actual);
        return actual;
    }

    public double getScaledGene(String gene){ //only cardinal
        if(world.data.critterGeneticBounds.get(gene)!=null){
            double value = shiftToRange (world.data.critterGeneticBounds.get (gene)[0], world.data.critterGeneticBounds.get (gene)[1], (Integer)code.getGeneValue (gene), code.getGeneSize (gene));
            return value;
        }else{
            System.out.println ("Error parsing genes: No parameters for min/max of "+gene);
            return 0;
        }
    }

    public static double shiftToRange(double rangeStart, double rangeEnd, double value, int maxValue) {
        return (((rangeEnd - rangeStart) * value) / maxValue) + rangeStart;
    }

    public double getBiomass() {
        return this.biomass;
    }

    public Enviro getEnviro() {
        return enviro;
    }

    public void setEnviro(Enviro enviro) {
        this.enviro = enviro;
    }

    public GenCode getCode() {
        return code;
    }

    public void setCode(GenCode code) {
        this.code = code;
    }

    public double getThirst() {
        return thirst;
    }

    public void setThirst(double thirst) {
        this.thirst = thirst;
    }

    public double getMaxThirst() {
        return maxThirst;
    }

    public void setMaxThirst(int maxThirst) {
        this.maxThirst = maxThirst;
    }

    public double getHunger() {
        return hunger;
    }

    public void setHunger(double hunger) {
        this.hunger = hunger;
    }

    public double getMaxHunger() {
        return maxHunger;
    }

    public void setMaxHunger(int maxHunger) {
        this.maxHunger = maxHunger;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getMateTolerance() {
        return mateTolerance;
    }

    public void setMateTolerance(int mateTolerance) {
        this.mateTolerance = mateTolerance;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getMateRate() {
        return mateRate;
    }

    public void setMateRate(double mateRate) {
        this.mateRate = mateRate;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    public ArrayList<String> getActions() {
        return actions;
    }

    public void setActions(ArrayList<String> actions) {
        this.actions = actions;
    }

    public double getWebbedFeet() {
        return webbedFeet;
    }

    public void setWebbedFeet(double webbedFeet) {
        this.webbedFeet = webbedFeet;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMateCooldown() {
        return mateCooldown;
    }

    public void setMateCooldown(int mateCooldown) {
        this.mateCooldown = mateCooldown;
    }

    public int getMateElapsedTime() {
        return mateElapsedTime;
    }

    public void setMateElapsedTime(int mateElapsedTime) {
        this.mateElapsedTime = mateElapsedTime;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public HashMap<String, Double> getDiet() {
        return diet;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public double getFoodEfficiency(String food) { //TODO
        return 1;
    }

    public void setDiet(HashMap<String, Double> diet) {
        this.diet = diet;
    }

    public double getAggressivity() {
        return aggressivity;
    }

    public void setAggressivity(double aggressivity) {
        this.aggressivity = aggressivity;
    }

    public double getWanderlust() {
        return wanderlust;
    }

    public void setWanderlust(double wanderlust) {
        this.wanderlust = wanderlust;
    }

    public double getStealth() {
        return stealth;
    }

    public void setStealth(double stealth) {
        this.stealth = stealth;
    }

    public double getPerception() {
        return perception;
    }

    public void setPerception(double perception) {
        this.perception = perception;
    }

    public void setBiomass(double biomass) {
        this.biomass = biomass;
    }

    public double getAmassedFertility() {
        return amassedFertility;
    }

    public void setAmassedFertility(double amassedFertility) {
        this.amassedFertility = amassedFertility;
    }

    @Override
    public int compareTo(Critter critter) {
        if(critter.speed == speed){
            return name.compareTo (critter.getName ());
        }else{
            return Double.compare (speed, critter.getSpeed ());
        }
    }

    public double getBloodSalination() {
        return bloodSalination;
    }

    public TreeSet<Active> getActives() {
        return actives;
    }

    public void setActives(TreeSet<Active> actives) {
        this.actives = actives;
    }

    public ArrayList<Passive> getPassives() {
        return passives;
    }

    public void setPassives(ArrayList<Passive> passives) {
        this.passives = passives;
    }

    public double getHP() {
        return HP;
    }

    public void setHP(double HP) {
        this.HP = HP;
    }

    public double getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(double maxHP) {
        this.maxHP = maxHP;
    }

    public void setBloodSalination(double bloodSalination) {
        this.bloodSalination = bloodSalination;
    }

    public int getPoison() {
        return poison;
    }

    public void setPoison(int poison) {
        this.poison = poison;
    }

    public int getStun() {
        return stun;
    }

    public void setStun(int stun) {
        this.stun = stun;
    }
}
