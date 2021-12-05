package GUI;

import api.DirectedWeightedGraphAlgorithms;
import implementation.AlgorithmsImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphFrame extends JFrame  {
    DirectedWeightedGraphAlgorithms g ;
    protected JRadioButton[] buttons;
    public GraphPanel graphPanel;
    private JPanel sideMenu , weightPanel;
    protected JTextField weightInput;
    private JButton addAllEdges,randomWeights;
    private RadioButtonListener rbl;


    public GraphFrame(DirectedWeightedGraphAlgorithms g){

        graphPanel = new GraphPanel(g);
        this.add(graphPanel);
        this.setTitle("Graph");
        this.setSize(1000,700);
        this.setLocationRelativeTo(null);
        rbl = new RadioButtonListener(this);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.pack();

        setComponents();
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.BLACK);
        this.setVisible(true);
    }
    private void setComponents() {
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
        this.sideMenu = new JPanel();
        sideMenu.setAlignmentX(LEFT_ALIGNMENT);
        sideMenu.setMaximumSize(new Dimension(200,700));
        setSideMenu();
        add(sideMenu);


    }
    private void setSideMenu() {
        sideMenu.setLayout(new GridLayout(9,1));

        buttons = new JRadioButton[5];

        buttons[0] = new JRadioButton("Add Vertex");
        buttons[0].addActionListener(rbl);
        sideMenu.add(buttons[0]);
        buttons[1] = new JRadioButton("Add Edge");
        buttons[1].addActionListener(rbl);
        sideMenu.add(buttons[1]);

        buttons[2] = new JRadioButton("Shortest Path");
        buttons[2].addActionListener(rbl);
        sideMenu.add(buttons[2]);
        buttons[3] = new JRadioButton("isConnected");
        buttons[3].addActionListener(rbl);
        sideMenu.add(buttons[3]);
        buttons[4] = new JRadioButton("CENTER");
        buttons[4].addActionListener(rbl);
        sideMenu.add(buttons[4]);

    }



}
