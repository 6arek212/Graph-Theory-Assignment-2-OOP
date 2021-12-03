package assignment2.ui;

import assignment2.api.DirectedWeightedGraphAlgorithms;
import assignment2.ui.utils.WorldGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GraphViewFrame extends JFrame {

    private GraphViewModel controller;
    private JFrame j;
    private JLabel numberOfNodes;
    private JLabel numberOfEdges;
    private ActionListener actionListener;
    private GraphViewPanel panel;

    public GraphViewFrame(DirectedWeightedGraphAlgorithms alg) {
        initJframe();
        initActionListener();
        initLabels();
        initWindowListener();

        this.controller = new GraphViewModel(alg, actionListener);
        this.panel = new GraphViewPanel(controller);

        Menu.initMenu(j, panel, actionListener);
        panel.addMouseListener(new ViewMouseClickHandler(actionListener, panel));
        j.add(panel);
        j.setVisible(true);
    }


    /**
     * initialize objects
     */

    private void initJframe() {
        this.j = new JFrame();
        j.setBackground(Color.WHITE);
        j.setSize(800, 600);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private void initLabels() {
        numberOfNodes = new JLabel();
        numberOfEdges = new JLabel();
        System.out.println(numberOfEdges);
        numberOfNodes.setBounds(16, 16, GraphViewPanel.padding + 16, 10);
        numberOfEdges.setBounds(16, 32, GraphViewPanel.padding + 16, 20);
        j.add(numberOfEdges);
        j.add(numberOfNodes);
    }


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
                j.repaint();
            }
            if (event instanceof UIEvents.CalculateRange) {
                panel.updateWorld();
            }
        };
    }

    private void initWindowListener() {
        j.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controller.clear();
            }
        });
    }

}
