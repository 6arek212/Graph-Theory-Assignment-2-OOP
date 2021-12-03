package assignment2.ui;


import assignment2.api.DirectedWeightedGraphAlgorithms;
import assignment2.api.EdgeData;
import assignment2.api.GeoLocation;
import assignment2.api.NodeData;
import assignment2.models.GeoLocationImpl;
import assignment2.models.NodeDataImpl;
import assignment2.ui.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;


/**
 * Simple UI for presenting the graph
 */
public class GraphViewPanel extends JPanel {
    public static final int radios = 20;
    public static final int padding = 70;


    public Range2Range world2Frame;
    private GraphViewModel controller;


    public GraphViewPanel(GraphViewModel controller) {
        this.controller = controller;
        world2Frame = WorldGraph.w2f(controller.getAlgo().getGraph(), getFrame());
    }




    /***
     *     getting the node at specific coordinates
     * @param x
     * @param y
     * @return
     */
    public NodeData getNodeByCoordinates(double x, double y) {
        GeoLocation g = new GeoLocationImpl(x, y, 0);
        List<NodeData> list = controller.getNodes();
        GeoLocation g2;
        for (NodeData node : list) {
            g2 = world2Frame.worldToframe(node.getLocation());
            if (g2.distance(g) <= (int) (radios / 2)) {
                return node;
            }
        }
        return null;
    }


    public void updateWorld() {
        world2Frame.updateWorld(WorldGraph.getWorldRange2D(controller.getAlgo().getGraph(), world2Frame.getFrame()));
    }

    private Range2D getFrame() {
        Range rx = new Range(padding, getWidth() - padding);
        Range ry = new Range(padding, getHeight() - padding);
        return new Range2D(rx, ry);
    }

    private void updateFrame() {
        world2Frame.updateFrame(getFrame());
    }


    /**
     * paint graph and ui
     *
     * @param g
     */

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        updateFrame();
        printGraph(g);
    }


    private void printGraph(Graphics g) {
        g.setColor(Color.BLUE);
        for (int i = 0; i < controller.getEdges().size(); i++) {
            drawEdge(controller.getEdges().get(i), g);
        }
        g.setColor(Color.BLACK);
        for (int i = 0; i < controller.getNodes().size(); i++) {
            drawNode(controller.getNodes().get(i), g);
        }
    }


    private void drawEdge(EdgeData edgeData, Graphics g) {
        NodeData s = controller.getNode(edgeData.getSrc());
        NodeData d = controller.getNode(edgeData.getDest());
        if (s != null && d != null) {
            GeoLocation s0 = world2Frame.worldToframe(s.getLocation());
            GeoLocation d0 = world2Frame.worldToframe(d.getLocation());
            g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
        }
    }


    private void drawNode(NodeData node, Graphics g) {
        if (node.getTag() == NodeDataImpl.WHITE) {
            g.setColor(Color.BLACK);
        } else if (node.getTag() == NodeDataImpl.GRAY) {
            g.setColor(Color.GRAY);
        } else {
            g.setColor(Color.RED);
        }

        GeoLocation pos = node.getLocation();
        GeoLocation fp = world2Frame.worldToframe(pos);
        g.drawString(node.getKey() + "", (int) fp.x() - radios / 2, (int) fp.y() - radios / 2);
        g.fillOval((int) fp.x() - radios / 2, (int) fp.y() - radios / 2, radios, radios);
    }


    public GraphViewModel getController() {
        return controller;
    }
}