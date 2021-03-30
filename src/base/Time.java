package base;

import enums.EventList;
import graphics.MainGUI;
import sources.Corpse;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Time extends Thread {
    final ArrayList<Event> events;
    private final Random r;
    private double tickSize; //in seconds
    private int ticks = 0;
    private final int cycleSize; //in ticks
    private final World w;
    private graphics.MainGUI gui;
    public double lastPop = 0;
    public boolean running = false;
    public boolean loop = false;
    public HashMap<String, Double> times;
    public double[] totals;
    public static int cycles = 0;
    //public int loops = WorldHandler.ticks;

    public Time(double tickSize, int cycleSize, World w) {
        this.tickSize = tickSize;
        this.cycleSize = cycleSize;
        this.w = w;
        this.gui = w.gui;
        this.events = new ArrayList<> ();
        this.r = w.getR ();
    }

    @Override
    public void run() {
        while (isAlive () && !isInterrupted ()) {
            if (running) {
                loop ();
                /*
                loops--;
                w.setStatus ("Ticks: "+loops);
                if(loops <= 0){
                    w.runState = 2;
                    this.interrupt ();
                    w.score = w.getCritters ().size ();
                    w.setStatus ("Ended: "+w.score);
                }
                 */
            } else {
                if (loop) {
                    loop = false;
                    loop ();
                }
                try {
                    Thread.sleep (50);
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            }
        }
    }

    public void loop() {
        try {
            double elapsed;
            long start = System.nanoTime ();
            if (gui!=null) {
                CountDownLatch latch = new CountDownLatch (1);
                gui.update (latch);
                latch.await ();
            }
            lastPop = w.getCritters ().size ();
            times = new HashMap<> ();
            totals = new double[]{0, 0, 0};
            tick ();
            /*
            System.out.println (totals[0] + ", " + totals[1] + ", " + totals[2]);
            System.out.println (times.toString ());
            System.out.println ("Tick: " + (System.nanoTime () - start) / 1000000);

             */
            ticks++;
            if (ticks >= cycleSize) {
                cycle ();
                cycles++;
                ticks = 0;
            }
            elapsed = (double) (System.nanoTime () - start) / 1000000;
            if(gui!=null){
                gui.lastCycleTime = (int) elapsed;
                gui.panel.totalAmount = w.getCritters ().size ();
            }
            if (elapsed < tickSize && tickSize != 0)
                sleep ((long) tickSize - (long) elapsed);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }

    public void tick() {
        new Vector<> (w.getEnviros ()).forEach (e -> {
            new Vector<> (e.getCritters ()).forEach (c -> {
                if (c.isAlive ()) {
                    c.tick ();
                } else {
                    double amount = c.getBiomass ();
                    double fert = c.getAmassedFertility ();
                    if (!(Double.isNaN (amount) || Double.isNaN (fert) || c.isEaten())) {
                        Resource corpse = new Corpse (c.getEnviro (), amount, fert);
                        c.getEnviro ().merge (corpse);
                    }
                    w.getCritters ().remove (c);
                    c.getEnviro ().getCritters ().remove (c);
                }
            });
        });
    }

    public void cycle() {
        generateEvents (); //TODO DA FIXARE
        cycleEvents ();
        cycleWorld ();
        //w.panel2.repaint ();
    }

    public void generateEvents() {
        long start = System.nanoTime ();
        for (EventList event : EventList.values ()) {
            for (Enviro enviro : w.getEnviros ()) {
                if (event.getBiomes () == null || event.getBiomes ().contains (enviro.getBiome ())) {
                    int randomN = r.nextInt (event.getRarity ());
                    if (event.getHumAsc () == 1) {
                        int prob = (int) Math.floor ((event.getRarity () * 10) / (enviro.getAvgHum () / 4 + 5));
                        randomN = r.nextInt (prob);
                    }
                    if (randomN == 0) {
                        try {
                            Event e = (Event) Class.forName ("events." + event.name ()).getDeclaredConstructor (new Class[]{Enviro.class, String.class}).newInstance (enviro, event.name ());
                            events.add (e);
                        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
                            ex.printStackTrace ();
                        }
                    }
                }
            }
        }
        //System.out.println ("Ciclo generazione: " + (double) (System.nanoTime () - start) / 1000000);
    }

    public void cycleEvents() {
        long start = System.nanoTime ();
        for (int i = 0; i < events.size (); i++) {
            Event event = events.get (i);
            event.tick ();
            if (event.getRemaining () < 0) {
                events.remove (event);
                i--;
            }
        }
        //System.out.println ("Ciclo eventi: " + (double) (System.nanoTime () - start) / 1000000);
    }

    public void cycleWorld() {
        long start = System.nanoTime ();
        double fert = 0;
        for (ArrayList<Enviro> row : w.getMap ()) {
            for (Enviro e : row) {
                cycleEnviro (e);
                fert+=e.getFertility ();
            }
        }
        System.out.println ("Fertility: "+fert);
        //System.out.println ("Ciclo celle: " + (double) (System.nanoTime () - start) / 1000000);
    }

    public void cycleEnviro(Enviro e) {
        //e.setTemperature (e.getTemperature()+(e.getAvgTemp()-e.getTemperature ())/100+r.nextGaussian());
        //e.setHumidity (e.getHumidity ()+(e.getAvgHum()-e.getHumidity ())/50+r.nextGaussian());
        e.setTemperature (e.getTemperature () + (e.getAvgTemp () - e.getTemperature ()) / 10);
        if (e.isRiver ()) {
            e.setHumidity (e.getHumidity () + (e.getAvgHum () + 15 - e.getHumidity ()) / 10);
        } else {
            e.setHumidity (e.getHumidity () + (e.getAvgHum () - e.getHumidity ()) / 10);
        }
        e.cycle();
    }

    public graphics.MainGUI getGui() {
        return gui;
    }

    public void setGui(graphics.MainGUI gui) {
        this.gui = gui;
    }

    public void setTickSize(int size) { this.tickSize = size; }

}
