package graphics;

import base.Critter;
import base.GeneLibrary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.Vector;

public class CritterInfo extends JPanel implements WindowStateListener, InfoPanel {
    private final JLabel info;
    private final JLabel actions;
    private final JScrollPane scroll;
    private final JScrollPane scroll2;
    private final JPanel genes;
    private final Critter c;

    public CritterInfo(Critter c) {
        this.c = c;
        BoxLayout layout = new BoxLayout (this, BoxLayout.Y_AXIS);
        this.setLayout (layout);
        info = new JLabel ();
        this.add (info);
        actions = new JLabel ();
        scroll = new JScrollPane (actions);
        this.add (scroll);
        genes = new JPanel ();
        genes.setLayout (new GridLayout (5,5));
        scroll2 = new JScrollPane (genes);
        this.add (scroll2);
        showInfo ();
        setupGenes ();
    }

    public void setupGenes(){
        for(GeneLibrary.GeneIds id: GeneLibrary.GeneIds.values ()){
            JProgressBar jp = new JProgressBar ();
            jp.setMaximum (id.getSize ());
            jp.setValue (c.getCode ().getCardinality (id.getName ()));
            jp.setName (id.getName ());
            jp.setToolTipText ("E"+c.getCode ().getCardinality (id.getName ()));
            jp.setStringPainted (true);
            jp.setString (id.getName ());
            genes.add(jp);
        }
    }

    public void showInfo() {
        String info = "<html>" + c.getName () + "<br>";
        info += "Hunger: " + (float) c.getHunger () + " / " + c.getMaxHunger () + "<br>";
        info += "Thirst: " + (float) c.getThirst () + " / " + c.getMaxThirst () + "<br>";
        info += "Size: " + c.getSize () + "<br>";
        info += "BaseSpeed: " + c.getSpeed () + "<br>";
        info += "Speed: " + c.getSpeed () + "<br>";
        info += "Failure: " + c.getFailureInteger() + "<br>";
        info += "Instability: " + c.total + "<br>";
        info += "Salination: "+c.getBloodSalination ()+ "<br>";
        info += "Generation: "+c.gen+ "<br>";
        info += "OTT: "+c.ott+"<br>";
        info += "Position: " + c.getEnviro ().getX () + " - " + c.getEnviro ().getX () + "<br>";
        info += "</html>";
        this.info.setText (info);
        String s = "";
        s += "<html>";
        for (int i = 0; i < c.getActions ().size (); i++) {
            s+=c.getActions ().get (i) + "<br>";
        }
        for(int i=c.getActions ().size ();i<20;i++){
            s+="<br>";
        }
        this.actions.setText (s + "</html>");
    }

    public void refresh() {
        showInfo ();
    }

    @Override
    public void windowStateChanged(WindowEvent windowEvent) {  //WONKY STUFF
        if (windowEvent.getNewState () == WindowEvent.WINDOW_CLOSED) {
            this.setVisible (false);
        }
    }
}
