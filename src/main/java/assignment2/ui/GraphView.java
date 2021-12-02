package assignment2.ui;


import assignment2.api.DirectedWeightedGraphAlgorithms;
import assignment2.api.EdgeData;
import assignment2.api.GeoLocation;
import assignment2.api.NodeData;
import assignment2.models.GeoLocationImpl;
import assignment2.models.NodeDataImpl;
import assignment2.ui.utils.Range;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;


/**
 * Simple UI for presenting the graph
 */
public class GraphView extends JPanel implements MouseListener {
    private final int radios = 20;
    private final int padding = 70;


    private GraphViewModel controller;
    public Range worldXRange;
    public Range worldYRange;
    public Range screenXRange;
    public Range screenYRange;
    private JFrame j;
    private JLabel numberOfNodes;
    private JLabel numberOfEdges;
    private JLabel graphText;
    private ActionListener actionListener;

    private NodeData getNodeByCoordinates(double x, double y) {
        GeoLocation g = new GeoLocationImpl(x, y, 0);
        List<NodeData> list = controller.getNodes();
        GeoLocation g2;
        for (NodeData node : list) {
            g2 = new GeoLocationImpl(worldXRange.toRange(screenXRange, node.getLocation().x()), worldYRange.toRange(screenYRange, node.getLocation().y()), 0);
            if (g2.distance(g) <= (int) (radios / 2)) {
                return node;
            }
        }
        return null;
    }

    NodeData fromNode;

    @Override
    public void mouseClicked(MouseEvent e) {
        if (getNodeByCoordinates(e.getX(), e.getY()) != null) {
            actionListener.actionEvent(new UIEvents.ShowMessage("There is already an existing node in these coordinates"));
            return;
        }

        String res = JOptionPane.showInputDialog(null, "Enter node KEY", "20");
        int key;
        try {
            key = Integer.parseInt(res.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            actionListener.actionEvent(new UIEvents.ShowMessage("enter numbers only separated by , !"));
            return;
        }

        double worldX = screenXRange.toRange(worldXRange, e.getX());
        double worldY = screenYRange.toRange(worldYRange, e.getY());
        controller.onTriggerEvent(new GraphEvents.AddNode(worldX, worldY, key));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        fromNode = getNodeByCoordinates(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (fromNode != null) {
            NodeData node = getNodeByCoordinates(e.getX(), e.getY());
            if (node != null && node != fromNode) {
                String res = JOptionPane.showInputDialog(null, "Enter edge weight", "20");
                double w;
                try {
                    w = Double.parseDouble(res.trim());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    actionListener.actionEvent(new UIEvents.ShowMessage("enter numbers only !"));
                    return;
                }

                controller.onTriggerEvent(new GraphEvents.AddEdge(fromNode.getKey(), node.getKey(), w));
            }
        }
        fromNode = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    public GraphView(DirectedWeightedGraphAlgorithms g) {
        init(g);
    }

    // init JFrame
    private void init(DirectedWeightedGraphAlgorithms alg) {
        this.j = new JFrame();
        j.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controller.clear();
            }
        });
        addMouseListener(this);
        numberOfNodes = new JLabel();
        numberOfEdges = new JLabel();
        numberOfNodes.setBounds(16, 16, padding + 16, 10);
        numberOfEdges.setBounds(16, 32, padding + 16, 10);
        graphText = new JLabel();
        graphText.setText("Graph Theory");
        j.add(numberOfEdges);
        j.add(numberOfNodes);
        j.add(graphText);

        actionListener = (UIEvents event) -> {
            if (event instanceof UIEvents.ShowMessage)
                JOptionPane.showMessageDialog(null, ((UIEvents.ShowMessage) event).getMessage());
            if (event instanceof UIEvents.Labels) {
                numberOfEdges.setText("Edges : " + ((UIEvents.Labels) event).getNumberOfEdges() + "");
                numberOfNodes.setText("Nodes : " + ((UIEvents.Labels) event).getNumberOfNode() + "");
            }
            if (event instanceof UIEvents.UpdateUi) {
                updateUI();
                j.repaint();
            }
        };
        this.controller = new GraphViewModel(alg, actionListener, screenXRange, screenYRange, worldXRange, worldYRange);
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
        worldXRange = new Range(minX, maxX);
        worldYRange = new Range(minY, maxY);


        screenXRange = new Range(padding, j.getWidth() - padding);
        screenYRange = new Range(padding, j.getHeight() - padding);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        graphText.setBounds(getWidth() / 2, 16, 100, 40);
        screenXRange.setMax(getWidth() - padding);
        screenYRange.setMax(getHeight() - padding);
        printGraph(g);
    }


    private void printGraph(Graphics g) {
        g.setColor(Color.BLUE);
        for (int i = 0; i < controller.getEdges().size(); i++) {
            EdgeData edgeData = controller.getEdges().get(i);
            NodeData d1 = controller.getNode(edgeData.getSrc());
            NodeData d2 = controller.getNode(edgeData.getDest());
            if (d1 != null && d2 != null) {
                g.drawLine((int) worldXRange.toRange(screenXRange, d1.getLocation().x()), (int) worldYRange.toRange(screenYRange, d1.getLocation().y()),
                        (int) worldXRange.toRange(screenXRange, d2.getLocation().x()), (int) worldYRange.toRange(screenYRange, d2.getLocation().y()));
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

                g.fillOval((int) worldXRange.toRange(screenXRange, node.getLocation().x()) - radios / 2, (int) worldYRange.toRange(screenYRange, node.getLocation().y()) - radios / 2, radios, radios);
            }

        }
    }

    public GraphViewModel getController() {
        return controller;
    }
}