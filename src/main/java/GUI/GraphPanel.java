package GUI;

import api.*;
import implementation.AlgorithmsImpl;
import implementation.DirectedWeightedGraphImpl;
import implementation.GeoLocationImpl;
import implementation.NodeDataImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class GraphPanel extends JPanel implements MouseListener {
    protected static DirectedWeightedGraphAlgorithms graph;
    private WorldGraph worldGraph;
    private Range2Range WorldToFrame;
    private GraphFrame frame;
    private static String radioButtonState;
    private static boolean isEnabled;


    private static NodeData endpt1, endpt2;

    GraphPanel(DirectedWeightedGraphAlgorithms graph) {
        this.graph = graph;
        this.setPreferredSize(new Dimension(700, 700));
        this.setFocusable(true);
        this.addMouseListener(this);

        this.setBackground(Color.black);
    }





    public DirectedWeightedGraph getGraph() {
        return (DirectedWeightedGraph) this.graph;
    }

    private void updateFrame() {
        Range rx = new Range(70, this.getWidth() - 70);
        Range ry = new Range(this.getHeight() - 100, 150);
        Range2D frame = new Range2D(rx, ry);
        WorldToFrame = worldGraph.w2f(graph.getGraph(), frame);

    }

    public NodeData getNodeByCoordinates(double x, double y) {
        GeoLocation g = new GeoLocationImpl(x, y, 0);
        Iterator<NodeData> list = graph.getGraph().nodeIter();
        GeoLocation g2;

        for (Iterator<NodeData> it = list; it.hasNext(); ) {
            NodeData node = it.next();
            g2 = WorldToFrame.worldToframe(node.getLocation());
            if (g2.distance(g) <= (int) (20 / 2)) {
                return node;
            }
        }
        return null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        updateFrame();
        Iterator<NodeData> iter = graph.getGraph().nodeIter();
        while (iter.hasNext()) {
            NodeData n = iter.next();
            g2d.setColor(Color.red);
            drawNode(n, 5, g);
            Iterator<EdgeData> itr = graph.getGraph().edgeIter(n.getKey());
            while (itr.hasNext()) {
                EdgeData e = itr.next();
                g2d.setColor(Color.white);
                drawEdge(e, g);
            }
        }


    }

    private void drawEdge(EdgeData e, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        GeoLocation s = graph.getGraph().getNode(e.getSrc()).getLocation();
        GeoLocation d = graph.getGraph().getNode(e.getDest()).getLocation();
        GeoLocation s0 = this.WorldToFrame.worldToframe(s);
        GeoLocation d0 = this.WorldToFrame.worldToframe(d);
        Line2D line = new Line2D.Double((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.white);
        g2d.fill(line);
        g2d.draw(line);
        if (e.getWeight() != 0) {
            g2d.setFont(new Font("Dialog", Font.PLAIN, 10));
            g2d.setColor(Color.cyan);
            g2d.drawString("" + e.getWeight(), (int) ((s0.x() + d0.x()) / 2), (int) (s0.y() + d0.y()) / 2);

        }


    }

    private void drawNode(NodeData n, int r, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
//        Shape node= new Ellipse2D.Double((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        GeoLocation pos = n.getLocation();
        GeoLocation fp = this.WorldToFrame.worldToframe(pos);
        System.out.println(fp);
        Shape node = new Ellipse2D.Double((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g2d.fill(node);
//        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }


    public void setIsEnabled(boolean e) {
        isEnabled = e;
    }


    public void setEndpt1(NodeData v) {
        endpt1 = v;
    }


    public void setEndpt2(NodeData v) {
        endpt2 = v;
    }


    public NodeData getEndpt1() {
        return endpt1;
    }


    public NodeData getEndpt2() {
        return endpt2;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }


    public void setRadioButtonState(String s) {
        radioButtonState = s;
    }


    public String getRadioButtonState() {
        return radioButtonState;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();


        if (radioButtonState.equals("")) {
            this.paintComponent(this.getGraphics());
        }

//        if(!isEnabled)return;


        if (radioButtonState.equals("Add Vertex")) {

            if (this.getNodeByCoordinates(x, y) != null) {
//                actionListener.actionEvent(new UIEvents.ShowMessage("There is already an existing node in these coordinates"));
                System.out.println("cyka");
                return;
            }


            GeoLocation geoLocation = new GeoLocationImpl(x , y , 0.0);
            NodeData v = new NodeDataImpl(graph.getGraph().nodeSize() + 1, geoLocation);
            graph.getGraph().addNode(v);

            repaint();

            this.paintComponent(this.getGraphics());



        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}
