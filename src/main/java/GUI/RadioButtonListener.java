package GUI;

import api.GeoLocation;
import api.NodeData;
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
//            disableUnselectedButtons(0);
            gui.graphPanel.setRadioButtonState("Add Vertex");
            gui.graphPanel.setIsEnabled(true);
            System.out.println("----------------Adding Vertex--------------");

        }
        //adding new Edge to GUI
        if (e.getSource()==gui.buttons[1]) {
//            disableUnselectedButtons(1);
            gui.graphPanel.setRadioButtonState("Add Edge");
            gui.graphPanel.setIsEnabled(true);
            System.out.println("----------------Adding Edge--------------");

        }
        if (e.getSource()==gui.buttons[2]) {
//            disableUnselectedButtons(2);
            gui.graphPanel.setRadioButtonState("Shortest Path");
            gui.graphPanel.setIsEnabled(true);
            System.out.println("----------------Shortest Path--------------");

        }
        if (e.getSource()==gui.buttons[3]) {
//            disableUnselectedButtons(3);
            gui.graphPanel.setRadioButtonState("isConnected");
            gui.graphPanel.setIsEnabled(true);
            System.out.println("----------------isConnected--------------");

        }
        if (e.getSource()==gui.buttons[4]) {
//            disableUnselectedButtons(4);
            gui.graphPanel.setRadioButtonState("CENTER");
            gui.graphPanel.setIsEnabled(true);
            System.out.println("----------------CENTER--------------");

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
