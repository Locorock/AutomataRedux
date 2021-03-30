package base;

import enums.EnviroList;
import enums.EnviroListRedux;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLOutput;
import java.util.*;

import static base.SimplexNoise.generateOctavedSimplexNoise;

public class World {
    public static final int tickSpeed = 50;
    private static final int size = 48;

    public int tDeaths = 0;
    public int fDeaths = 0;
    public int aDeaths = 0;
    public int kDeaths = 0;

    private String status = "Generating";
    public int runState = 0;
    public int score = 100;
    private ArrayList<ArrayList<Enviro>> map;
    private Random r;
    private ArrayList<Enviro> enviros = new ArrayList<> ();
    private Time t;
    private TreeSet<Critter> critters;
    public graphics.MainGUI gui;
    public final WorldData data;


    public World(WorldData data) {
        int seed = new Random ().nextInt (10000);
        this.critters = new TreeSet<Critter> (Critter::compareTo);
        this.r = new Random (seed);
        this.data = data;
        GeneLibrary gl = new GeneLibrary ();
        t = new Time (tickSpeed, 20, this);
    }

    public static void main(String[] arg) throws InterruptedException {
        new World(new WorldData (null)).run(true);
    }

    public void run(boolean hasWindow) throws InterruptedException {
        generateWorldSimplex (0, 0);
        Enviro start = enviros.get (48*24+2);
        for(int i = 0;i<500;i++){
            int[] values = new int[GeneLibrary.GeneIds.values ().length];
            Arrays.fill (values, 0);
            values[13] = (int) 32;
            //Critter c = new Critter("yeet"+Critter.id, this, e, new GenCode (values, r), 0, 0);
            Critter c2 = new Critter("yeet"+Critter.id, this, start);
            c2.setAge (35);
            start.getCritters ().add(c2);
            critters.add (c2);
        }
        //this.status = "Ticks left: "+WorldHandler.ticks;

        if(hasWindow) {
            gui = new graphics.MainGUI (this);
            t.setGui (gui);
        }
        t.running = true;
        t.start ();
        this.runState = 1;
    }

    private void generateWorldSimplex(int skew, int magnitude) {
        float[][] heightMap = generateOctavedSimplexNoise (World.size, World.size, 3, 0.05f, data.worldGenParams.get ("landSize").floatValue (), r);
        float[][] heatMap = generateOctavedSimplexNoise (World.size, World.size, 3, 0.1f, 0.04f, r);
        float[][] humidityMap = generateOctavedSimplexNoise (World.size, World.size, 3, 0.1f, 0.04f, r);

        double[][][] stats = new double[World.size][World.size][3];
        for (int i = 0; i < World.size; i++) {
            for (int j = 0; j < World.size; j++) {
                double height = (((heightMap[i][j] + 1) / 2) * 200) - nearBorderValue (j, i) + new Random().nextGaussian ()*10;
                double temp = Math.abs (heatMap[i][j]) * 100 + 10 + data.worldGenParams.get ("tempOffset");
                double hum = Math.abs (humidityMap[i][j]) * 100 + 10 * data.worldGenParams.get ("humMult");
                stats[i][j] = new double[]{height, temp, hum};
            }
        }
        boolean[][] rivers = generateRivers (stats);
        boolean[][] lakes = identifyLakes (stats);
        map = new ArrayList<> ();
        for(int i=0;i<World.size;i++){
            map.add (new ArrayList<> ());
            for(int j=0;j<World.size;j++){
                boolean seabound = false;
                for(int k=-1;k<=1;k++){
                    for(int t=-1;t<=1;t++){
                        try{
                            if(stats[k+i][t+j][0] <= data.worldGenParams.get ("seaLevel") && !lakes[k+i][t+j]) {
                                seabound = true;
                            }
                        }catch(ArrayIndexOutOfBoundsException e){
                        };
                    }
                }
                Enviro e = buildEnviro (stats[i][j][0], stats[i][j][1], stats[i][j][2], j, i, rivers[i][j], lakes[i][j], seabound);
                map.get(i).add(e);
                enviros.add(e);
            }
        }

        printBiomes ();
    }

    private boolean[][] identifyLakes(double[][][] stats){
        boolean[][] lakes = new boolean[size][size];
        for(int i=0;i<lakes.length;i++){
            Arrays.fill(lakes[i],true);
        }
        ArrayList<boolean[][]> agglom = new ArrayList<> ();
        for(int i=0;i< World.size;i++) {
            for (int j = 0; j < World.size; j++) {
                if((i==0 || i==World.size-1 || j==0 || j==World.size-1) && stats[i][j][0] <= data.worldGenParams.get ("seaLevel")){
                    boolean[][] ocean = new boolean[size][size];
                    ocean[i][j] = true;
                    loopLake (i, j, stats , ocean);
                    agglom.add(ocean);
                }
            }
        }
        for(boolean[][] ocean : agglom){
            for(int i=0;i<lakes.length;i++) {
                for (int j=0; j<lakes.length; j++) {
                    if(ocean[i][j]==true){
                        lakes[i][j]=false;
                    }
                }
            }
        }
        return lakes;
    }

    private void loopLake(int i, int j, double[][][] stats, boolean[][] ocean){
        for (int k = -1; k <= 1; k++) {
            for (int t = -1; t <= 1; t++) {
                if(k+i>=0 && t+j>=0 && k+i<ocean.length && t+j<ocean.length && k+t!=0){
                    if (stats[i + k][j + t][0] <= data.worldGenParams.get ("seaLevel") && !ocean[i + k][j + t]) {
                        ocean[i + k][j + t] = true;
                        loopLake (i+k, j+t, stats, ocean);
                    }
                }
            }
        }
    }

    private boolean[][] generateRivers(double[][][] stats){
        boolean[][] rivers = new boolean[size][size];
        ArrayList<double[]> localMaxes = new ArrayList<> ();
        for(int i=0;i< World.size;i++){
            for(int j=0;j< World.size;j++){
                if(stats[i][j][0]>data.worldGenParams.get("seaLevel")){
                    double max = stats[i][j][0];
                    for(int y=-1;y<=1;y++){
                        for(int x=-1;x<=1;x++){
                            if(i+y>=0 && i+y<size && j+x>=0 && j+x<size){
                                if(stats[i+y][j+x][0]>max){
                                    max = stats[i+y][j+x][0];
                                }
                            }
                        }
                    }
                    if(stats[i][j][0]==max){
                        localMaxes.add(new double[]{i,j, max});
                    }
                }
            }
        }
        Collections.shuffle (localMaxes);
        List<double[]> loci = localMaxes.subList (0,localMaxes.size ());
        for(double[] locus : loci){
            boolean[][] passed = new boolean[size][size];
            int posy = (int) locus[0];
            int posx = (int) locus[1];
            double min = locus[2];
            int minx = posx;
            int miny = posy;
            for(int i=0;i<100;i++){
                rivers[posy][posx]=true;
                passed[posy][posx]=true;
                for(int j=-1;j<=1;j++){
                    for(int k=-1;k<=1;k++){
                        if(j+posy>=0 && j+posy<size && k+posx>=0 && k+posx<size) {
                            if (!passed[j + posy][k + posx]) {
                                double height = stats[j + posy][k + posx][0];
                                if (height <= min) {
                                    min = height;
                                    minx = k + posx;
                                    miny = j + posy;
                                }
                            }
                        }
                    }
                }
                if((minx!=posx || miny!=posy) && min >= data.worldGenParams.get ("seaLevel")){
                    posx = minx;
                    posy = miny;
                }else{
                    break;
                }
            }
        }
        return rivers;
    }

    private Enviro buildEnviro(double height, double temp, double hum, int x, int y, boolean river, boolean lake, boolean seabound) {
        String biome = assignBiomeRedux (height, temp, hum, river, lake);
        Enviro instance = null;
        try {
            instance = (Enviro) Class.forName ("enviros." + biome)
                    .getDeclaredConstructor (new Class[]{double.class, double.class, double.class, World.class, int.class, int.class, boolean.class, boolean.class})
                    .newInstance (temp, height, hum, this, x, y, river, seabound);
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace ();
        }
        return instance;
    }

    private String assignBiome(double height, double temp, double hum, boolean river) {
        if (height <= data.worldGenParams.get ("seaLevel")) {
            return "Ocean";
        } else {
            ArrayList<Double> wProbs = new ArrayList<> ();
            double tProb = 0;
            for (EnviroList el : EnviroList.values ()) {
                if (temp >= el.getTempMin () && temp <= el.getTempMax () && hum >= el.getHumMin () && hum <= el.getHumMax ()) {
                    double wProb;
                    //System.out.println ("Found possible " + el.name ());
                    double diffT = Math.abs (temp - (el.getTempMin () + el.getTempMax ())) / 2;
                    double diffH = Math.abs (hum - (el.getHumMin () + el.getHumMax ())) / 2;
                    wProb = ((diffT + diffH)) / el.getRarity () * (river ? el.getRiverMod () : 1);
                    wProbs.add (wProb);
                    tProb += wProb;
                } else {
                    wProbs.add (0D);
                }
            }
            double selection = r.nextDouble () * tProb;
            double partial = 0;
            for (int i = 0; i < wProbs.size (); i++) {
                partial += wProbs.get (i);
                if (selection <= partial) {
                    return EnviroList.values ()[i].name ();
                }
            }
        }
        return null;
    }

    private String assignBiomeRedux(double height, double temp, double hum, boolean river, boolean lake) {
        if (height <= data.worldGenParams.get ("seaLevel")) {
            if(lake){
                return "Lake";
            }else{
                return "Ocean";
            }
        } else {
            ArrayList<Double> wProbs = new ArrayList<> ();
            double tProb = 0;
            for (EnviroListRedux el : EnviroListRedux.values ()) {
                //System.out.println ("Found possible " + el.name ());
                double diffT = Math.abs(Math.pow (temp - el.getAvgTemp(), 4));
                double diffH = Math.abs(Math.pow (hum - el.getAvgHum(), 4));
                double base = 1d/(diffT + diffH);
                double rarityMod = base / el.getRarity ();
                double riverMod = rarityMod * (river ? el.getRiverMod ()*5 : 1);
                wProbs.add (riverMod);
                tProb += riverMod;
            }
            double selection = r.nextDouble () * tProb;
            double partial = 0;
            for (int i = 0; i < wProbs.size (); i++) {
                partial += wProbs.get (i);
                if (selection <= partial) {
                    return EnviroListRedux.values ()[i].name ();
                }
            }
        }
        return null;
    }

    public Enviro getEnviroWrapping(int x, int y){
        if(x<0){
            x=size-1;
        }
        if(x>=size){
            x=0;
        }
        if(y<0){
            y=size-1;
        }
        if(y>=size){
            y=0;
        }
        return this.map.get (y).get(x);
    }

    private int nearBorderValue(int j, int i) {
        int n = 0;
        if (size - i <= 7) {
            n += 7 - (size - i);
        }
        if (size - j <= 7) {
            n += 7 - (size - j);
        }
        if (i <= 7) {
            n += 7 - i;
        }
        if (j <= 7) {
            n += 7 - j;
        }
        return n * n * n;
    }

    public void printBiomes() {
        for (ArrayList<Enviro> enviroArrayList : map) {
            for (Enviro enviro : enviroArrayList) {
                if (enviro == null) {
                    System.out.print ("000");
                } else {
                    System.out.print ((enviro.getAltitude () + "  ").substring (0,2));
                }
            }
            System.out.println ();
        }
    }

    public ArrayList<Enviro> getEnviros() {
        return enviros;
    }

    public void setEnviros(ArrayList<Enviro> enviros) {
        this.enviros = enviros;
    }

    public Random getR() {
        return r;
    }

    public void setR(Random r) {
        this.r = r;
    }

    public ArrayList<ArrayList<Enviro>> getMap() {
        return map;
    }

    public TreeSet<Critter> getCritters() {
        return critters;
    }

    public void setCritters(TreeSet<Critter> critters) {
        this.critters = critters;
    }

    public Time getT() {
        return t;
    }

    public void setT(Time t) {
        this.t = t;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }
}
