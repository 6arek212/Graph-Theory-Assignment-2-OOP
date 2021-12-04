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
        for (int i = 0; i < controller.getNodes().size(); i++) {
            drawNode(controller.getNodes().get(i), g);
        }

        for (int i = 0; i < controller.getEdges().size(); i++) {
            drawEdge(controller.getEdges().get(i), g);
        }
    }


    private void drawEdge(EdgeData edgeData, Graphics g) {
        g.setColor(Color.BLUE);
        NodeData s = controller.getNode(edgeData.getSrc());
        NodeData d = controller.getNode(edgeData.getDest());
        if (s != null && d != null) {
            GeoLocation s0 = world2Frame.worldToframe(s.getLocation());
            GeoLocation d0 = world2Frame.worldToframe(d.getLocation());
            drawArrowLine(g, (int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y(), 10, 5);
        }
    }


    private void drawNode(NodeData node, Graphics g) {
        g.setColor(Color.BLACK);
        if (node.getTag() == NodeDataImpl.WHITE) {
            g.setColor(Color.WHITE);
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


    private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);

        g.setColor(Color.GREEN);
        g.fillPolygon(xpoints, ypoints, 3);
    }

    public GraphViewModel getController() {
        return controller;
    }
}