package assignment2.ui;


import assignment2.api.DirectedWeightedGraphAlgorithms;
import assignment2.api.EdgeData;
import assignment2.api.NodeData;
import assignment2.models.NodeDataImpl;
import assignment2.ui.utils.Range;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;


/**
 * Simple UI for presenting the graph
 */
public class GraphView extends JPanel {
    private final int radios = 20;
    private final int padding = 70;


    private GraphViewModel controller;
    private Range r1x;
    private Range r1y;
    private Range r2x;
    private Range r2y;
    private JFrame j;
    private JLabel numberOfNodes;
    private JLabel numberOfEdges;
    private JLabel graphText;


    public GraphView(DirectedWeightedGraphAlgorithms g) {
        init(g);
    }

    private void init(DirectedWeightedGraphAlgorithms alg) {
        this.j = new JFrame();

        j.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
               controller.clear();
            }
        });

        numberOfNodes = new JLabel();
        numberOfEdges = new JLabel();
        graphText = new JLabel();
        graphText.setText("Graph Theory");
        j.add(numberOfEdges);
        j.add(numberOfNodes);
        j.add(graphText);

        ActionListener actionListener = (UIEvents event) -> {
            if (event instanceof UIEvents.ShowMessage)
                JOptionPane.showMessageDialog(null, ((UIEvents.ShowMessage) event).getMessage());
            if (event instanceof UIEvents.Labels) {
                numberOfEdges.setText("Edges : " + ((UIEvents.Labels) event).getNumberOfEdges() + "");
                numberOfNodes.setText("Nodes : " + ((UIEvents.Labels) event).getNumberOfNode() + "");
            }
            if (event instanceof UIEvents.UpdateUi) {
                calculateRange();
                updateUI();
            }
        };
        this.controller = new GraphViewModel(alg, actionListener);
        Menu.initMenu(j, this, actionListener);

        j.setBackground(Color.WHITE);
        j.setSize(800, 600);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);
        j.add(this);
        calculateRange();
    }


    public void calculateRange() {
        Iterator<NodeData> d = controller.getNodes().iterator();

        double minX = Integer.MAX_VALUE;
        double maxX = Integer.MIN_VALUE;
        double minY = Integer.MAX_VALUE;
        double maxY = Integer.MIN_VALUE;
        while (d.hasNext()) {
            NodeData node = d.next();
            if (minX > node.getLocation().x())
                minX = node.getLocation().x();

            if (maxX < node.getLocation().x())
                maxX = node.getLocation().x();

            if (minY > node.getLocation().y())
                minY = node.getLocation().y();

            if (maxY < node.getLocation().y())
                maxY = node.getLocation().y();
        }
        r1x = new Range(minX, maxX);
        r1y = new Range(minY, maxY);
        r2x = new Range(padding, j.getWidth() - padding);
        r2y = new Range(padding, j.getHeight() - padding);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        numberOfNodes.setBounds(16, 16, padding + 16, 10);
        numberOfEdges.setBounds(16, 32, padding + 16, 10);
        graphText.setBounds(getWidth() / 2, 16, 100, 40);
        r2x.setMax(getWidth() - padding);
        r2y.setMax(getHeight() - padding);
        printGraph(g);
    }


    private void printGraph(Graphics g) {
        g.setColor(Color.BLUE);
        for (int i = 0; i < controller.getEdges().size(); i++) {
            EdgeData edgeData = controller.getEdges().get(i);
            NodeData d1 = controller.getNode(edgeData.getSrc());
            NodeData d2 = controller.getNode(edgeData.getDest());
            if (d1 != null && d2 != null) {
                g.drawLine((int) r1x.toRange(r2x, d1.getLocation().x()), (int) r1y.toRange(r2y, d1.getLocation().y()),
                        (int) r1x.toRange(r2x, d2.getLocation().x()), (int) r1y.toRange(r2y, d2.getLocation().y()));
            }
        }


        g.setColor(Color.BLACK);
        for (int i = 0; i < controller.getNodes().size(); i++) {
            NodeData node = controller.getNodes().get(i);
            if (node != null) {
                if (node.getTag() == NodeDataImpl.WHITE) {
                    g.setColor(Color.BLACK);
                } else if (node.getTag() == NodeDataImpl.GRAY) {
                    g.setColor(Color.GRAY);
                } else {
                    g.setColor(Color.RED);
                }

                g.fillOval((int) r1x.toRange(r2x, node.getLocation().x()) - radios / 2, (int) r1y.toRange(r2y, node.getLocation().y()) - radios / 2, radios, radios);
            }

        }
    }

    public GraphViewModel getController() {
        return controller;
    }
}