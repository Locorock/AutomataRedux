package base;

import sources.Corpse;
import sources.Excrement;
import sources.Water;

import java.util.ArrayList;
import java.util.Random;

public class Enviro {
    private Enviro[] enviroDirs;
    private double distance;
    private World world;
    private String biome;
    private ArrayList<Critter> critters = new ArrayList<> ();
    private final ArrayList<Resource> resources = new ArrayList<> ();
    private double temperature, humidity, altitude, fertility;
    private double avgTemp, avgHum;
    private Random r;
    private boolean river;
    private int x, y;

    private int rainStr, quakeStr, lightningStr, eruptionS;

    public Enviro(double avgTemp, double altitude, double avgHum, String biome, World world, Random r) {
        this.avgTemp = avgTemp;
        this.altitude = altitude;
        this.avgHum = avgHum;
        this.biome = biome;
        this.world = world;
        this.r = r;
    }

    public Enviro(double avgTemp, double altitude, double avgHum, String biome, World world, int x, int y, boolean river) {
        this.avgTemp = avgTemp;
        this.altitude = altitude;
        this.avgHum = avgHum;
        this.temperature = avgTemp;
        this.humidity = avgHum;
        this.biome = biome;
        this.world = world;
        this.r = world.getR ();
        this.x = x;
        this.y = y;
        this.river = river;
        if(river){
            this.resources.add(new Water(this, 10000000, 0.01));
        }

    }

    public void cycle(){
        for(int i=0; i<resources.size ();i++){
            Resource r = resources.get(i);
            ArrayList<Resource> res = r.tick ();
            this.resources.addAll(res);
            if(r.amount<=0 && r.bound == null){
                resources.remove (i);
                if(r instanceof Excrement)
                    System.out.println(((Excrement) r).delta);
                i--;
            }
        }
    }

    public void merge(Resource res){
        if(!Double.isNaN (res.amount) && !Double.isNaN (res.amassedFertility)){
            for(Resource r : resources){
                if(res.getClass ().equals (r.getClass ())){
                    r.setAmassedFertility (r.getAmassedFertility ()+res.getAmassedFertility ());
                    if(Double.isNaN (r.getAmassedFertility ())){
                        System.out.println ("FLAGGO");
                    }

                    if(Double.isNaN (res.amount)){
                        System.out.println ("provvedo ammontare "+res.amount);
                    }
                    r.provide (res.amount);
                    if(Double.isNaN (r.amount)){
                        System.out.println ("cane");
                    }

                    return;
                }
            }
            this.resources.add(res);
        }
    }

    public void merge(ArrayList<Resource> res){

        resources.addAll(res);
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getBiome() {
        return biome;
    }

    public void setBiome(String biome) {
        this.biome = biome;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Random getR() {
        return r;
    }

    public void setR(Random r) {
        this.r = r;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getRainStr() {
        return rainStr;
    }

    public void setRainStr(int rainStr) {
        this.rainStr = rainStr;
    }

    public int getQuakeStr() {
        return quakeStr;
    }

    public void setQuakeStr(int quakeStr) {
        this.quakeStr = quakeStr;
    }

    public int getLightningStr() {
        return lightningStr;
    }

    public void setLightningStr(int lightningStr) {
        this.lightningStr = lightningStr;
    }

    public int getEruptionS() {
        return eruptionS;
    }

    public void setEruptionS(int eruptionS) {
        this.eruptionS = eruptionS;
    }

    public boolean isRiver() {
        return river;
    }

    public void setRiver(boolean river) {
        this.river = river;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(double avgTemp) {
        this.avgTemp = avgTemp;
    }

    public double getAvgHum() {
        return avgHum;
    }

    public void setAvgHum(double avgHum) {
        this.avgHum = avgHum;
    }

    public Enviro[] getEnviroDirs() {
        return enviroDirs;
    }

    public void setEnviroDirs(Enviro[] dirs) {
        this.enviroDirs = dirs;
    }

    public ArrayList<Critter> getCritters() {
        return critters;
    }

    public ArrayList<Resource> getResources() { return resources; }

    public void setCritters(ArrayList<Critter> critters) {
        this.critters = critters;
    }

    public double getFertility() {
        return fertility;
    }

    public void setFertility(double fertility, Resource source) {
        if(Double.isNaN (fertility)){
            System.out.println ("EHHHH");
            System.out.println (source.getClass ());
            if(source instanceof Corpse){
                System.out.println (source.amount);
                System.out.println (source.amassedFertility);
            }
        }
        this.fertility = fertility;

    }
}
