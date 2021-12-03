package GUI;

import api.GeoLocation;
import implementation.GeoLocationImpl;
import implementation.NodeDataImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class RadioButtonListener implements ActionListener {


    private GraphFrame gui;



    public RadioButtonListener(GraphFrame gui) {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

     //adding new node to GUI
        if (e.getSource()==gui.buttons[0]) {
            disableUnselectedButtons(0);
            gui.graphPanel.setRadioButtonState("Add Vertex");
            gui.graphPanel.setIsEnabled(true);
            System.out.println("----------------Adding --------------");



        }
        gui.graphPanel.repaint();
    }


            private void disableUnselectedButtons( int index){
                gui.graphPanel.setIsEnabled(false);
                for (int i = 0; i < gui.buttons.length; ++i) {
                    if (i == index) continue;
                    gui.buttons[i].setEnabled(false);
                }
                gui.graphPanel.setEndpt1(null);
                gui.graphPanel.setEndpt2(null);
            }



    }
