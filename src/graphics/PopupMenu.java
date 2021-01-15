package graphics;

import base.Enviro;

import javax.swing.*;

public class PopupMenu extends JPopupMenu {
    public Enviro enviro;
    JMenuItem critterInfo, enviroInfo;

    public PopupMenu(MainGUI gui, Enviro enviro) {
        this.enviro = enviro;
        critterInfo = new JMenuItem ("Critter Info");
        critterInfo.setName ("critterInfo");
        critterInfo.addActionListener (gui);
        add (critterInfo);
        enviroInfo = new JMenuItem ("Enviro Info");
        enviroInfo.setName ("enviroInfo");
        enviroInfo.addActionListener (gui);
        add (enviroInfo);
    }
}
