package assignment2.ui;

import assignment2.api.DirectedWeightedGraphAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GraphViewFrame extends JFrame {

    private final static int WIDTH = 1000;
    private final static int HEIGHT = 700;

    private GraphViewModel controller;
    private JLabel numberOfNodes;
    private JLabel numberOfEdges;
    private ActionListener actionListener;
    private GraphViewPanel panel;

    public GraphViewFrame(DirectedWeightedGraphAlgorithms alg) {
        initActionListener();
        initLabels();
        initWindowListener();

        this.controller = new GraphViewModel(alg, actionListener);
        this.panel = new GraphViewPanel(controller);
        panel.setBackground(Color.BLACK);

        Menu.initMenu(this, panel, actionListener);
        panel.addMouseListener(new GraphViewMouseClickHandler(actionListener, panel));
        add(panel);
        initJframe();
    }


    /**
     * initialize JFrame
     */
    private void initJframe() {
        this.setTitle("Graph");
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
    }


    private void initLabels() {
        numberOfNodes = new JLabel();
        numberOfEdges = new JLabel();
        numberOfNodes.setBounds(16, 16, GraphViewPanel.padding + 16, 10);
        numberOfEdges.setBounds(16, 32, GraphViewPanel.padding + 16, 20);
        numberOfEdges.setForeground(Color.WHITE);
        numberOfNodes.setForeground(Color.WHITE);
        add(numberOfEdges);
        add(numberOfNodes);
    }


    /**
     *  View action Listener
     */
    private void initActionListener() {
        actionListener = (UIEvents event) -> {
            if (event instanceof UIEvents.ShowMessage)
                JOptionPane.showMessageDialog(null, ((UIEvents.ShowMessage) event).getMessage());
            if (event instanceof UIEvents.Labels) {
                numberOfEdges.setText("Edges : " + ((UIEvents.Labels) event).getNumberOfEdges() + "");
                numberOfNodes.setText("Nodes : " + ((UIEvents.Labels) event).getNumberOfNode() + "");
            }
            if (event instanceof UIEvents.UpdateUi) {
                panel.updateUI();
                repaint();
            }
            if (event instanceof UIEvents.CalculateRange) {
                panel.updateWorld();
            }
        };
    }

    /**
     * listener to clear the controller tasks when exist
     */
    private void initWindowListener() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controller.clear();
            }
        });
    }

}
