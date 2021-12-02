package GUI;

import api.DirectedWeightedGraphAlgorithms;
import implementation.AlgorithmsImpl;

import javax.swing.*;
import java.awt.*;

public class GraphFrame extends JFrame {
//    DirectedWeightedGraphAlgorithms g ;
    public GraphFrame(DirectedWeightedGraphAlgorithms g){

        this.add(new GraphPanel(g));
        this.setTitle("Graph");
        this.setSize(1000,700);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.BLACK);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.pack();
        this.setVisible(true);
        this.setResizable(true);
        //in middle of the screen
        this.setLocationRelativeTo(null);

    }

}
