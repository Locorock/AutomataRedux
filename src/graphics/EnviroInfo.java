package graphics;

import base.Critter;
import base.Enviro;
import base.GeneLibrary;
import base.Resource;
import enums.EnviroList;
import sources.Water;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;


public class EnviroInfo extends JPanel implements InfoPanel, ActionListener {
    private Enviro enviro;
    private JLabel info;
    private JTextArea resources;
    private JScrollPane scrollr, scrollc, scrollg;
    private JPanel bottom, critters, genes;
    private HashMap<String, JProgressBar> geneBars;
    private GridLayout grid;
    HashMap<String, Integer> geneT;
    public EnviroInfo(Enviro e){
        this.enviro = e;
        this.setLayout (new GridLayout (3,1));
        info = new JLabel ();
        bottom = new JPanel ();
        resources = new JTextArea ();
        scrollr = new JScrollPane (resources);
        critters = new JPanel ();
        scrollc = new JScrollPane (critters);
        genes = new JPanel ();
        genes.setLayout (new GridLayout (5,5));
        geneBars = new HashMap<> ();
        scrollg = new JScrollPane (genes);
        this.add(info);
        this.add(bottom);
        this.add(genes);
        bottom.add(scrollr);
        bottom.add(scrollc);
        bottom.setLayout (new GridLayout (1,2));
        grid = new GridLayout (e.getCritters ().size (), 1);
        critters.setLayout(grid);
        setupGenes();
        this.setPreferredSize (new Dimension (800,1200));
    }

    private void setupGenes(){
        geneT = new HashMap<String, Integer> ();
        for(GeneLibrary.GeneIds id: GeneLibrary.GeneIds.values ()){
            JProgressBar jp = new JProgressBar ();
            int max = 32;
            if(id.getName () == "LeavesEfficiency" || id.getName () == "BloodSalination" || id.getName () == "MeatEfficiency" || id.getName () == "FruitEfficiency")
                max = 8;
            jp.setMaximum (max*enviro.getCritters ().size ());
            jp.setValue (0);
            jp.setName (id.getName ());
            jp.setStringPainted (true);
            jp.setString (id.getName ());
            genes.add(jp);
            geneBars.put(id.getName (), jp);
        }
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
        in += enviro.cycles+"<br>";
        in += enviro.subcycles+"<br>";
        in += "</html>";
        this.info.setText (in);
        resources.setVisible (false);
        resources.setText ("");
        for(Resource r : enviro.getResources ()){
            resources.append (r.getName()+" - "+r.getAmount ()+" - "+r.getAmassedFertility ()+" - "+(r instanceof Water ? ((Water) r).getSalination () : "")+" \n");
        }
        resources.setVisible (true);
        critters.setVisible (false);
        critters.removeAll ();
        for(GeneLibrary.GeneIds id: GeneLibrary.GeneIds.values ()) {
            geneT.put (id.getName (), 0);
        }
        for(Critter c : enviro.getCritters ()){
            JButton jb = new JButton(c.getName()+" - "+(c.getHunger ()+"").substring (0,3)+" - "+(c.getThirst ()+"").substring (0,3)+" - "+(c.getAge()+"").substring (0,3));
            jb.setName (c.getName ());
            jb.addActionListener (this);
            critters.add(jb);
            for(GeneLibrary.GeneIds id: GeneLibrary.GeneIds.values ()){
                geneT.put (id.name (),  geneT.get(id.name ())+c.getCode ().getCardinality (id.getName ()));
            }
        }
        grid.setRows (enviro.getCritters ().size ());
        geneT.forEach ( (name, amount) -> {
            JProgressBar jp = geneBars.get (name);
            int max = 32;
            if(name == "LeavesEfficiency" || name == "BloodSalination" || name == "MeatEfficiency" || name == "FruitEfficiency")
                max = 8;
            jp.setMaximum (max*enviro.getCritters ().size ());
            jp.setValue (amount);
        });
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
