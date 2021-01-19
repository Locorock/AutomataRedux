package graphics;

import base.Critter;
import base.Enviro;
import base.World;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class MainGUI implements KeyListener, ActionListener, WindowListener, ChangeListener {
    private final World w;
    public ArrayList<InfoPanel> openRenders;
    public graphics.AdvancedWorldRenderer panel;
    public JPanel content;
    public ControlPanel control;
    public WorldMenuBar menu;
    private CountDownLatch latch;
    private JFrame frame;
    public int lastCycleTime;
    private boolean rendered = false;

    public MainGUI(World w) {
        this.w = w;
        openRenders = new ArrayList<InfoPanel> ();
        SwingUtilities.invokeLater (new Runnable () {
            public void run() {
                createAndShowGUI ();
            }
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame ();
        frame.setSize (new Dimension (w.getMap ().size () * 16, w.getMap ().size() * 16));
        frame.setVisible (true);
        content = new JPanel ();
        menu = new WorldMenuBar (this);
        panel = new graphics.AdvancedWorldRenderer (w, this);

        control = new ControlPanel(this);
        control.addKeyListener (this);

        frame.setContentPane (content);
        frame.setJMenuBar (menu);
        frame.addKeyListener (this);

        content.setLayout (new BorderLayout ());
        content.add(control, BorderLayout.PAGE_END);
        content.add(panel, BorderLayout.CENTER);
        content.addKeyListener (this);

        panel.repaint ();
        panel.addKeyListener (this);
        panel.setFocusable(true);
        panel.requestFocus();

        rendered = true;
    }

    public static String getFixedString(int original, int length) {
        String originalS = original + "";
        String result = "";
        if (originalS.length () > length) {
            return originalS + "";
        }
        for (int i = 0; i < 8 - originalS.length (); i++) {
            result += " ";
        }
        result += originalS;
        return result;
    }

    public void update(CountDownLatch latch) {
        this.latch = latch;
        while(!rendered){
            try {
                Thread.sleep (50);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
        }
        panel.repaint ();
        for (int i = 0; i < openRenders.size (); i++) {
            InfoPanel cr = openRenders.get (i);
            cr.refresh ();
        }

        menu.pop.setText (getFixedString (w.getCritters ().size (), 5));
        menu.fDeaths.setText (getFixedString (w.fDeaths, 4));
        menu.tDeaths.setText (getFixedString (w.tDeaths, 4));
        menu.aDeaths.setText (getFixedString (w.aDeaths, 4));
        menu.kDeaths.setText (getFixedString (w.kDeaths, 4));
        menu.ticks.setText (getFixedString (lastCycleTime, 4));
        latch.countDown ();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JComponent comp = (JComponent) actionEvent.getSource ();
        String name = comp.getName ();
        switch (name) {
            case "biomeView": {
                panel.setBiomeView ();
                break;
            }
            case "popView": {
                panel.setPopView ();
                break;
            }
            case "humView": {
                panel.setHumView ();
                break;
            }
            case "heightView": {
                panel.setHeightView ();
                break;
            }
            case "fertView": {
                panel.setFertView ();
                break;
            }
            case "graph": {
                JFrame f = new JFrame ();
                f.addWindowListener (this);
                f.setSize (300, 400);
                Graph cr = new Graph ("PopDelta", w);
                f.setContentPane (cr);
                openRenders.add (cr);
                f.setVisible (true);
                break;
            }
            case "pause":{
                control.swap ();
                w.getT ().running = !w.getT ().running;
                System.out.println (w.getT ().getState ());
                break;
            }
            case "step":{
                if(w.getT ().running == false){
                    w.getT ().loop = true;
                }
                break;
            }
        }
        panel.repaint ();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode ();
        System.out.println (key);
        if (key == KeyEvent.VK_G) {

        }
        if (key == KeyEvent.VK_F) {
            w.getT ().loop = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            control.swap ();
            w.getT ().running = !w.getT ().running;
        }
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        this.w.getT ().setTickSize (((JSlider)changeEvent.getSource ()).getValue ());
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        System.out.println ("CLOSED");
        if (windowEvent.getSource () != this) {
            JPanel cr = (JPanel) ((JFrame) windowEvent.getSource ()).getContentPane ();
            openRenders.remove (cr);
        }
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }


    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }
}

