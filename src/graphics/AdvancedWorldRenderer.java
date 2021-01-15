package graphics;

import base.Critter;
import base.Enviro;
import base.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

public class AdvancedWorldRenderer extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    private final int wUnit;
    private final World w;
    private final MainGUI gui;
    private double zoom = 2;
    private final boolean init = false;
    private int selectionOriginX = 0;
    private int selectionOriginY = 0;
    private int selectionEndX = 0;
    private int selectionEndY = 0;
    private Rectangle2D selection;
    private int oldPosX;
    private int oldPosY;
    private boolean toInit = true;
    private int cameraPosX = 0;
    private int cameraPosY = 0;
    private int dragStartX = 0;
    private int dragStartY = 0;
    public int totalAmount = 0;
    private int count = 0;
    private Enviro enviroSelected;
    private CountDownLatch latch;
    private AffineTransform translate;
    private AffineTransform scale;
    private boolean showAll = false;
    private boolean humidityView = false;
    private boolean heightView = false;
    private boolean fertView = false;
    private AffineTransform transform = null;
    private boolean biomeView = true;
    private double renderThreshold = 15;

    public AdvancedWorldRenderer(World w, MainGUI gui) {
        this.w = w;
        this.gui = gui;
        this.wUnit = 16;
        this.addMouseListener (this);
        this.addMouseMotionListener (this);
        this.addMouseWheelListener (this);
        System.out.println (oldPosX + " - " + oldPosY);
        repaint ();
        validate ();
    }

    public static Color getGreyscale(double num) {
        float[] color = Color.RGBtoHSB (0, (int) num, 0, null);
        return Color.getHSBColor (color[0], color[1], color[2]);
    }

    public static Color getRedScale(double num, Color c) {
        float[] color = Color.RGBtoHSB ((int) (c.getRed () + num * 10), c.getGreen (), c.getBlue (), null);
        return Color.getHSBColor (color[0], color[1], (float) (color[2] - num / 30.0));
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

    public int translateOnScale(int coordinate, boolean horizontal) {
        if (horizontal) {
            return (int) ((coordinate - (this.getWidth () - (this.getWidth () / zoom)) / 2));
        } else {
            return (int) ((coordinate - (this.getHeight () - (this.getHeight () / zoom)) / 2));
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent (graphics);
        if (toInit) {
            oldPosX = this.getWidth () / 4;
            oldPosY = this.getHeight () / 4;
            toInit = false;
        }
        Graphics2D g = (Graphics2D) graphics;
        translate = AffineTransform.getTranslateInstance (translateOnScale (cameraPosX - dragStartX + oldPosX, true), translateOnScale (cameraPosY - dragStartY + oldPosY, false));
        scale = AffineTransform.getScaleInstance (zoom, zoom);
        scale.concatenate (translate);
        transform = scale;
        g.setTransform (transform);

        if (zoom > renderThreshold) {
            MacroOverlays.call (g, w, "microView");
        } else {
            if (biomeView) {
                MacroOverlays.call (g, w, "biomeView");
            } else {
                if (humidityView) {
                    MacroOverlays.call (g, w, "humView");
                } else {
                    if (heightView) {
                        MacroOverlays.call (g, w, "heightView");
                    } else {
                        if (fertView){
                            MacroOverlays.call (g, w, "fertView");
                        }else{
                            MacroOverlays.call (g, w, "popView");
                        }
                    }
                }
            }
            if (showAll) {
                MacroOverlays.call (g, w, "allView");
            }
        }

        g.setColor (Color.red);
        g.drawRect (selectionOriginX, selectionOriginY, selectionEndX - selectionOriginX, selectionEndY - selectionOriginY);
        g.setFont (g.getFont ().deriveFont ((float) (g.getFont ().getSize () / zoom)));
        g.drawString (String.valueOf (count), selectionOriginX, selectionOriginY);
        if (latch != null) {
            this.latch.countDown ();
        }

    }

    public Point getAbsolutePoint(Point p) {
        try {
            p.x = p.x - (int) (zoom / 2); //REMOVE HALF OF THE ZOOM VALUE TO FIX IMPRECISIONS IN THE REVERSED TRANSFORMATION
            p.y = p.y - (int) (zoom / 2);
            Point np = new Point ();
            transform.inverseTransform (p, np);
            return np;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(SwingUtilities.isMiddleMouseButton (mouseEvent)){
            Enviro e = getPointingEnviro ();
            EnviroInfo ei = new EnviroInfo(e);
            JFrame f = new JFrame ();
            f.setSize (600, 400);
            f.setContentPane (ei);
            gui.openRenders.add (ei);
            f.setVisible (true);
        }
    }


    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton (mouseEvent)) {
            dragStartX = mouseEvent.getX ();
            dragStartY = mouseEvent.getY ();
            cameraPosX = dragStartX;
            cameraPosY = dragStartY;
            cameraPosY = dragStartY;
        } else {
            if (mouseEvent.isPopupTrigger ()) {
                Point p = getAbsolutePoint (mouseEvent.getPoint ());
                Enviro enviroSelected = getAbsEnviro (p.x, p.y);
                PopupMenu menu = new PopupMenu (w.gui, enviroSelected);
                menu.show (mouseEvent.getComponent (), mouseEvent.getX (), mouseEvent.getY ());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (SwingUtilities.isMiddleMouseButton (mouseEvent) && !mouseEvent.isControlDown ()) {
            /*
            selectionEndX = getAbsolutePoint (mouseEvent.getPoint ()).x;
            selectionEndY = getAbsolutePoint (mouseEvent.getPoint ()).y;
            count = 0;
            int[] traits = new int[35];
            ArrayList<Critter> critters = new ArrayList<> ();
            Arrays.fill (traits, 0);
            for (Critter current : (TreeSet<Critter>) w.getCritters ().clone ()) {
                if (current.getAbsx () < selectionEndX && current.getAbsy () < selectionEndY && current.getAbsx () > selectionOriginX && current.getAbsy () > selectionOriginY) {
                    critters.add (current);
                }
            }
            count = critters.size ();
            JFrame jf = new JFrame ();
            jf.setLayout (new GridLayout (2, 1));
            jf.setSize (600, 30 * 32);
            jf.setVisible (true);
            jf.add (new PopulationRenderer (critters));
            JPanel jp = new JPanel ();
            jp.setLayout (new GridLayout (20, 2));
            jp.add (new JLabel (critters.get (0).name));
            ArrayList<String> actions = critters.get (0).getActions ();
            for (int i = 0; i < actions.size (); i++) {
                JLabel desc = new JLabel (actions.get (i));
                jp.add (desc);
                System.out.println (desc.getText ());
            }
            JLabel desc = new JLabel (String.valueOf (critters.get (0).getSize ()));
            jp.add (desc);
            JLabel desc2 = new JLabel (String.valueOf (critters.get (0).getAggressiveness ()));
            jp.add (desc2);
            jf.add (jp);
            */
        } else {
            if (SwingUtilities.isLeftMouseButton (mouseEvent)) {
                oldPosX = oldPosX + cameraPosX - dragStartX;
                oldPosY = oldPosY + cameraPosY - dragStartY;
                cameraPosX = 0;
                cameraPosY = 0;
                dragStartX = 0;
                dragStartY = 0;
            }
        }
        repaint ();
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton (mouseEvent)) {
            cameraPosX = mouseEvent.getX ();
            cameraPosY = mouseEvent.getY ();
        }
        repaint ();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        zoom -= (double) mouseWheelEvent.getWheelRotation () / 2;
        if (zoom < 1) {
            zoom = 1;
        }
        renderThreshold = 10 + (this.getWidth () * this.getHeight ()) / 520000;
        repaint ();
    }

    public Enviro getPointingEnviro() {
        Point p = new Point (this.getMousePosition ().x, this.getMousePosition ().y);
        p = getAbsolutePoint (p);
        enviroSelected = getAbsEnviro (p.x, p.y);
        return enviroSelected;
    }

    public Enviro getAbsEnviro(int x, int y){
        try {
            return w.getMap ().get (y / 16).get (x / 16);
        } catch (IndexOutOfBoundsException e) {
        }
        return null;
    }

    public double getZoom() {
        return zoom;
    }

    public double getRenderTreshold() {
        return renderThreshold;
    }

    public void setPopView() {
        biomeView = false;
        humidityView = false;
        heightView = false;
        fertView = false;
    }

    public void setHumView() {
        biomeView = false;
        humidityView = true;
        heightView = false;
        fertView = false;
    }

    public void setBiomeView() {
        biomeView = true;
        humidityView = false;
        heightView = false;
        fertView = false;
    }

    public void setHeightView() {
        biomeView = false;
        humidityView = false;
        heightView = true;
        fertView = false;
    }

    public void setFertView() {
        biomeView = false;
        humidityView = false;
        heightView = false;
        fertView = true;
    }

    public void switchAllView() {
        showAll = !showAll;
    }

    //UNUSED LISTENERS
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    }


}