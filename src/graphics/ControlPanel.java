package graphics;

import javax.swing.*;

public class ControlPanel extends JPanel {
    private JButton pause;
    private JSlider simSpeed;
    private JButton frameForward;
    public ControlPanel(MainGUI gui){
        pause = new JButton ("Pause");
        pause.setName ("pause");
        pause.addActionListener (gui);
        simSpeed = new JSlider ();
        simSpeed.setMajorTickSpacing (20);
        simSpeed.setMaximum (100);
        simSpeed.setMinimum (0);
        simSpeed.setValue (50);
        simSpeed.setBorder (BorderFactory.createTitledBorder (BorderFactory.createBevelBorder (0), "Sim Speed"));
        simSpeed.setPaintTicks(true);
        simSpeed.setPaintLabels(true);
        simSpeed.addChangeListener (gui);
        frameForward = new JButton("Step 1");
        frameForward.setName ("step");
        frameForward.addActionListener (gui);
        this.add(simSpeed);
        this.add(pause);
        this.add(frameForward);

        this.setBorder (BorderFactory.createBevelBorder (0));
    }

    public void swap(){
        if(this.pause.getText ().equals ("Pause")){
            this.pause.setText ("Resume");
        }else{
            this.pause.setText ("Pause");
        }
    }
}
