package graphics;

import base.Critter;
import base.Enviro;
import base.Resource;
import enums.EnviroList;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class EnviroInfo extends JPanel implements InfoPanel, ActionListener {
    private Enviro enviro;
    private JLabel info;
    private JTextArea resources;
    private JScrollPane scrollr, scrollc;
    private JPanel bottom, critters;
    private GridLayout grid;
    public EnviroInfo(Enviro e){
        this.enviro = e;
        this.setLayout (new GridLayout (2,1));
        info = new JLabel ();
        bottom = new JPanel ();
        resources = new JTextArea ();
        scrollr = new JScrollPane (resources);
        critters = new JPanel ();
        scrollc = new JScrollPane (critters);
        this.add(info);
        this.add(bottom);
        bottom.add(scrollr);
        bottom.add(scrollc);
        bottom.setLayout (new GridLayout (1,2));
        grid = new GridLayout (e.getCritters ().size (), 1);
        critters.setLayout(grid);
        this.setPreferredSize (new Dimension (800,800));
    }
    @Override
    public void refresh() {
        String in = "";
        in += "<html>";
        in += enviro.getBiome ()+"<br>";
        in += enviro.getAltitude ()+"<br>";
        in += enviro.getHumidity ()+"<br>";
        in += enviro.getTemperature ()+"<br>";
        in += enviro.getFertility ()+"<br>";
        in += "</html>";
        this.info.setText (in);
        resources.setVisible (false);
        resources.setText ("");
        for(Resource r : enviro.getResources ()){

            resources.append (r.getName()+" - "+r.getAmount ()+" - "+r.getAmassedFertility ()+"\n");
        }
        resources.setVisible (true);
        critters.setVisible (false);
        critters.removeAll ();
        for(Critter c : enviro.getCritters ()){
            JButton jb = new JButton(c.getName()+" - "+(c.getHunger ()+"").substring (0,3)+" - "+(c.getThirst ()+"").substring (0,3)+" - "+(c.getAge()+"").substring (0,3));
            jb.setName (c.getName ());
            jb.addActionListener (this);
            critters.add(jb);
        }
        grid.setRows (enviro.getCritters ().size ());
        critters.setVisible (true);
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        for(Critter c : enviro.getCritters ()){
            if(c.getName () == ((JButton)actionEvent.getSource ()).getName ()){
                CritterInfo ci = new CritterInfo (c);
                JFrame f = new JFrame ();
                f.setSize (300, 400);
                f.setContentPane (ci);
                enviro.getWorld ().getT ().getGui ().openRenders.add (ci); // ORRIBILE
                f.setVisible (true);
            }
        }
    }
}
