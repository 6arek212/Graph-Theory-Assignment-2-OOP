package GUI;

import api.*;
import implementation.AlgorithmsImpl;
import implementation.DirectedWeightedGraphImpl;
import implementation.NodeDataImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.Random;

public class GraphPanel extends JPanel implements ActionListener {
    DirectedWeightedGraphAlgorithms graph;
    private WorldGraph worldGraph;
    private Range2Range  WorldToFrame;

    GraphPanel(DirectedWeightedGraphAlgorithms graph) {
        this.graph = graph;
        this.setPreferredSize(new Dimension(700, 700));
        this.setFocusable(true);



    }

    private void updateFrame() {
        Range rx = new Range(70,this.getWidth()-70);
        Range ry = new Range(this.getHeight()-100,150);
        Range2D frame = new Range2D(rx,ry);
        WorldToFrame = worldGraph.w2f(graph.getGraph() , frame);

    }

    public void paint(Graphics g) {
        updateFrame();
        Iterator<NodeData> iter = graph.getGraph().nodeIter();
        while (iter.hasNext()) {
            NodeData n = iter.next();
            g.setColor(Color.red);
            drawNode(n, 5, g);
            Iterator<EdgeData> itr = graph.getGraph().edgeIter(n.getKey());
            while (itr.hasNext()) {
                EdgeData e = itr.next();
                g.setColor(Color.white);
                drawEdge(e, g);
            }
        }
    }

    private void drawEdge(EdgeData e, Graphics g) {
        GeoLocation s = graph.getGraph().getNode(e.getSrc()).getLocation();
        GeoLocation d = graph.getGraph().getNode(e.getDest()).getLocation();
        GeoLocation s0 = this.WorldToFrame.worldToframe(s);
        GeoLocation d0 = this.WorldToFrame.worldToframe(d);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());

    }

    private void drawNode(NodeData n, int r, Graphics g) {
        GeoLocation pos = n.getLocation();
        GeoLocation fp = this.WorldToFrame.worldToframe(pos);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
