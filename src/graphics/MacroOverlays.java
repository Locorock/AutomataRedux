package graphics;

import base.Critter;
import base.Enviro;
import base.World;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class MacroOverlays {
    private static final int wUnit = 16;

    public static void call(Graphics g, World w, String code) {
        AdvancedWorldRenderer panel = w.gui.panel;
        switch (code) {
            case "popView":
                popView ((Graphics2D) g, w, panel);
                break;
            case "biomeView":
                biomeView ((Graphics2D) g, w, panel);
                break;
            case "humView":
                humidityView ((Graphics2D) g, w, panel);
                break;
            case "heightView":
                heightView ((Graphics2D) g, w, panel);
                break;
            case "fertView":
                fertilityView ((Graphics2D) g, w, panel);
        }
    }

    public static void popView(Graphics2D g, World w, AdvancedWorldRenderer panel) {
        g.setColor (Color.black);
        g.fill (g.getClipBounds ());
        ((ArrayList<ArrayList<Enviro>>) w.getMap ().clone ()).forEach (current -> {
            ((ArrayList<Enviro>) current.clone ()).forEach (currentEnviro -> {
                Color c;
                if (currentEnviro.getBiome () == "Ocean") {
                    c = Color.darkGray;
                } else {
                    c = Color.gray;
                }
                if (currentEnviro.getCritters ().size () > 1) {
                    c = getRedScale (currentEnviro.getCritters ().size (), c);
                }
                g.setColor (c);
                Rectangle2D r = new Rectangle (wUnit * currentEnviro.getX (), wUnit * currentEnviro.getY (), Math.round (wUnit), Math.round (wUnit));
                if (isOnScreen (r, g)) {
                    g.fill (r);
                    g.setColor (Color.decode ("#0077E0"));
                }
            });
        });
    }

    public static void biomeView(Graphics2D g, World w, AdvancedWorldRenderer panel) {
        g.setColor (Color.decode ("#0077E0"));
        g.fill (g.getClipBounds ());
        ((ArrayList<ArrayList<Enviro>>) w.getMap ().clone ()).forEach (current -> {
            ((ArrayList<Enviro>) current.clone ()).forEach (currentEnviro -> {
                Color c;
                switch (currentEnviro.getBiome ()) {
                    case "Ocean": {
                        c = Color.decode ("#0077E0");
                        break;
                    }
                    case "Forest": {
                        c = Color.decode ("#0CFF00");
                        break;
                    }
                    case "Plains": {
                        c = Color.decode ("#4AB004");
                        break;
                    }
                    case "Taiga": {
                        c = Color.decode ("#0E4B00");
                        break;
                    }
                    case "Tundra": {
                        c = Color.decode ("#974A1C");
                        break;
                    }
                    case "Jungle": {
                        c = Color.green;
                        break;
                    }
                    case "Savanna": {
                        c = Color.yellow;
                        break;
                    }
                    case "Arctic": {
                        c = Color.decode ("#7DCB8C");
                        break;
                    }
                    case "Desert": {
                        c = Color.decode ("#5087FF");
                        break;
                    }
                    case "Wetland": {
                        c = Color.decode ("#42B260");
                        break;
                    }
                    case "Steppe": {
                        c = Color.decode ("#A7D87A");
                        break;
                    }
                    case "Wasteland": {
                        c = Color.decode ("#A7D87b");
                        break;
                    }
                    default: {
                        c = Color.lightGray;
                    }
                }
                g.setColor (c);
                Rectangle2D r = new Rectangle (wUnit * currentEnviro.getX (), wUnit * currentEnviro.getY (), Math.round (wUnit), Math.round (wUnit));
                if (isOnScreen (r, g)) {
                    g.fill (r);
                    g.setColor (Color.decode ("#0077E0"));
                }
                if(currentEnviro.isRiver ()){
                    g.setColor (Color.black);
                    g.drawString ("R", wUnit*currentEnviro.getX (), wUnit*currentEnviro.getY ()+wUnit);
                }
            });
        });
    }

    public static void humidityView(Graphics2D g, World w, AdvancedWorldRenderer panel) {
        ((ArrayList<ArrayList<Enviro>>) w.getMap ().clone ()).forEach (current -> {
            ((ArrayList<Enviro>) current.clone ()).forEach (currentEnviro -> {
                Color c = getGreyscale (currentEnviro.getHumidity () * 2.5);
                g.setColor (c);
                Rectangle2D r = new Rectangle (wUnit * currentEnviro.getX (), wUnit * currentEnviro.getY (), Math.round (wUnit), Math.round (wUnit));
                if (isOnScreen (r, g)) {
                    g.fill (r);
                    g.setColor (Color.decode ("#0077E0"));
                }
            });
        });
    }

    public static void fertilityView(Graphics2D g, World w, AdvancedWorldRenderer panel) {
        ((ArrayList<ArrayList<Enviro>>) w.getMap ().clone ()).forEach (current -> {
            ((ArrayList<Enviro>) current.clone ()).forEach (currentEnviro -> {
                Color c = getGreyscale (currentEnviro.getFertility () * 2.5);
                g.setColor (c);
                Rectangle2D r = new Rectangle (wUnit * currentEnviro.getX (), wUnit * currentEnviro.getY (), Math.round (wUnit), Math.round (wUnit));
                if (isOnScreen (r, g)) {
                    g.fill (r);
                    g.setColor (Color.decode ("#0077E0"));
                }
            });
        });
    }

    public static void heightView(Graphics2D g, World w, AdvancedWorldRenderer panel) {
        ((ArrayList<ArrayList<Enviro>>) w.getMap ().clone ()).forEach (current -> {
            ((ArrayList<Enviro>) current.clone ()).forEach (currentEnviro -> {
                Color c = getGreyscale (currentEnviro.getAltitude ());
                g.setColor (c);
                Rectangle2D r = new Rectangle (wUnit * currentEnviro.getX (), wUnit * currentEnviro.getY (), Math.round (wUnit), Math.round (wUnit));
                if (isOnScreen (r, g)) {
                    g.fill (r);
                    g.setColor (Color.white);
                    g.setFont (g.getFont ().deriveFont (6f));
                    g.drawString (String.valueOf (currentEnviro.getAltitude ()/10).substring (0,4), wUnit * currentEnviro.getX (), wUnit * currentEnviro.getY ()+wUnit);
                    g.setColor (Color.decode ("#0077E0"));
                }
            });
        });
    }


    public static Color getRedScale(double num, Color c) {
        float[] color = Color.RGBtoHSB ((int) (c.getRed () + num * 10), c.getGreen (), c.getBlue (), null);
        return Color.getHSBColor (color[0], color[1], (float) (color[2] - num / 30.0));
    }

    public static Color getGreyscale(double num) {
        float[] color = Color.RGBtoHSB (0, (int) num, 0, null);
        return Color.getHSBColor (color[0], color[1], color[2]);
    }

    public static boolean isOnScreen(Rectangle2D rect, Graphics2D g) {
        if (g.getClipBounds ().contains (new Point ((int) rect.getX (), (int) rect.getY ()))) {
            return true;
        }
        if (g.getClipBounds ().contains (new Point ((int) (rect.getX () + rect.getWidth ()), (int) rect.getY ()))) {
            return true;
        }
        if (g.getClipBounds ().contains (new Point ((int) rect.getX (), (int) (rect.getY () + rect.getHeight ())))) {
            return true;
        }
        return g.getClipBounds ().contains (new Point ((int) (rect.getX () + rect.getWidth ()), (int) (rect.getY () + rect.getHeight ())));
    }
}
