package graphics;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.KeyEvent;

public class WorldMenuBar extends JMenuBar {
    private final JMenu overlays;
    private final JRadioButtonMenuItem biomeView;
    private final JRadioButtonMenuItem humidityView;
    private final JRadioButtonMenuItem popView;
    private final JRadioButtonMenuItem heightView;
    private final JRadioButtonMenuItem fertView;
    private final JMenuItem pops;
    private final JMenuItem filters;
    public JLabel pop, fDeaths, aDeaths, tDeaths, kDeaths, ticks;
    private final MainGUI gui;

    public WorldMenuBar(MainGUI gui) {
        this.gui = gui;

        overlays = new JMenu ("Overlays");
        overlays.setMnemonic (KeyEvent.VK_O);
        this.add (overlays);

        this.add (Box.createHorizontalGlue ()); //SEPARATOR

        this.add (new JLabel ("    Deaths: "));
        fDeaths = new JLabel ("0");
        fDeaths.setBorder (new BevelBorder (1));
        tDeaths = new JLabel ("0");
        tDeaths.setBorder (new BevelBorder (1));
        aDeaths = new JLabel ("0");
        aDeaths.setBorder (new BevelBorder (1));
        kDeaths = new JLabel ("0");
        kDeaths.setBorder (new BevelBorder (1));
        this.add (fDeaths);
        this.add (new JLabel (" "));
        this.add (tDeaths);
        this.add (new JLabel (" "));
        this.add (aDeaths);
        this.add (new JLabel (" "));
        this.add (kDeaths);

        this.add (new JLabel ("    Pop:"));

        pop = new JLabel ("0");
        pop.setBorder (new BevelBorder (1));
        this.add (pop);

        this.add (new JLabel ("  Cycles:"));

        ticks = new JLabel ("0");
        ticks.setBorder (new BevelBorder (1));
        this.add (ticks);

        this.add (new JLabel ("   "));

        overlays.addSeparator ();

        ButtonGroup group = new ButtonGroup ();

        biomeView = new JRadioButtonMenuItem ("Biome View");
        biomeView.setName ("biomeView");
        biomeView.setSelected (true);
        biomeView.setMnemonic (KeyEvent.VK_B);
        group.add (biomeView);
        overlays.add (biomeView);
        biomeView.addActionListener (gui);

        popView = new JRadioButtonMenuItem ("Population View");
        popView.setName ("popView");
        popView.setMnemonic (KeyEvent.VK_P);
        group.add (popView);
        overlays.add (popView);
        popView.addActionListener (gui);

        humidityView = new JRadioButtonMenuItem ("Humidity View");
        humidityView.setName ("humView");
        humidityView.setMnemonic (KeyEvent.VK_U);
        group.add (humidityView);
        overlays.add (humidityView);
        humidityView.addActionListener (gui);

        heightView = new JRadioButtonMenuItem ("Height View");
        heightView.setName ("heightView");
        heightView.setMnemonic (KeyEvent.VK_H);
        group.add (heightView);
        overlays.add (heightView);
        heightView.addActionListener (gui);

        fertView = new JRadioButtonMenuItem ("Fertility View");
        fertView.setName ("fertView");
        fertView.setMnemonic (KeyEvent.VK_F);
        group.add (fertView);
        overlays.add (fertView);
        fertView.addActionListener (gui);

        overlays.addSeparator ();

        pops = new JMenuItem ("Population");
        pops.setName ("graph");
        pops.setMnemonic (KeyEvent.VK_O);
        group.add (pops);
        overlays.add (pops);
        pops.addActionListener (gui);

        filters = new JMenuItem ("Filters");
        filters.setName ("filters");
        filters.setMnemonic (KeyEvent.VK_F);
        group.add (filters);
        overlays.add (filters);
        filters.addActionListener (gui);
    }
}
