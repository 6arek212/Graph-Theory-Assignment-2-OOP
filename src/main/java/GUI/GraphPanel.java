package GUI;

import api.*;
import implementation.AlgorithmsImpl;
import implementation.EdgeDataImpl;
import implementation.GeoLocationImpl;
import implementation.NodeDataImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.*;
import java.util.List;

public class GraphPanel extends JPanel implements MouseListener {
    protected static DirectedWeightedGraphAlgorithms graph;
    private WorldGraph worldGraph;

    private Range2Range WorldToFrame;
    private GraphFrame frame;
    private RadioButtonListener radioButtonListener;
    private static String radioButtonState;
    private static boolean isEnabled;
    private final int NODE_SIZE = 10; // need to be even
    private final int ARROW_SIZE = NODE_SIZE - 2;

    private static NodeDataImpl endpt1, endpt2;

    GraphPanel(DirectedWeightedGraphAlgorithms graph) {

        this.graph = graph;
        this.setPreferredSize(new Dimension(700, 700));
        this.setFocusable(true);
        this.addMouseListener(this);
        this.radioButtonListener = new RadioButtonListener(frame);
       this.setBackground(Color.BLACK);
        JOptionPane.showMessageDialog(null, "Click For Action", "NOTE", JOptionPane.INFORMATION_MESSAGE);


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

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        updateFrame();
        Iterator<NodeData> iter = graph.getGraph().nodeIter();
        while (iter.hasNext()) {
            NodeDataImpl n = (NodeDataImpl) iter.next();


             n.setNodeState(Color.RED);
            drawNode((NodeDataImpl) n, 7, g);
            Iterator<EdgeData> itr = graph.getGraph().edgeIter(n.getKey());
            while (itr.hasNext()) {
                EdgeData e = itr.next();


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
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.ORANGE);
        g2d.fill(line);
        g2d.draw(line);
        g.setColor(Color.yellow);
//        g.fillRect(((int) (s0.x() + 7 * d0.x()) / 8 - ARROW_SIZE / 2), (int) ((s0.y() + 7 * d0.y()) / 8 - ARROW_SIZE / 2), ARROW_SIZE, ARROW_SIZE);

        if (e.getWeight() != 0) {
            g2d.setFont(new Font("Dialog", Font.PLAIN, 10));
        }


    }

    private void drawNode(NodeDataImpl n, int r, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        GeoLocation pos = n.getLocation();
        GeoLocation fp = this.WorldToFrame.worldToframe(pos);

        Shape node = new Ellipse2D.Double((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g2d.setColor(n.getNodeState());
        n.setVisualNode(node);
        g2d.fill(node);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }


    public void setIsEnabled(boolean e) {
        isEnabled = e;
    }


    public void setEndpt1(NodeDataImpl v) {
        endpt1 = v;
    }


    public void setEndpt2(NodeDataImpl v) {
        endpt2 = v;
    }





    public void setRadioButtonState(String s) {
        radioButtonState = s;
    }

    public String getRadioButtonState() {
        return radioButtonState;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        AlgorithmsImpl ag = (AlgorithmsImpl) this.graph;
        int x = e.getX();
        int y = e.getY();
        GeoLocation geoLocation = WorldToFrame.frameToWorld(new GeoLocationImpl(x, y, 0));


        if (radioButtonState.equals("")) {
            this.paintComponent(this.getGraphics());
        }

        if (!isEnabled) return;


        if (radioButtonState.equals("Add Vertex")) {

           NodeData v = ag.findVertex(e.getPoint(), WorldToFrame);

            if (v != null) {

                JOptionPane.showMessageDialog(null, "Node Exist", "Add Vertex", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            v = new NodeDataImpl(ag.getGraph().nodeSize() +1 , geoLocation);
            graph.getGraph().addNode(v);

            this.paintComponent(this.getGraphics());
            updateUI();

        }

        if (radioButtonState.equals("Add Edge")) {


            if (endpt1 == null) {

                endpt1 = (NodeDataImpl) ag.findVertex(e.getPoint(), WorldToFrame);
                JOptionPane.showMessageDialog(null, "Node 1: " + endpt1, "Add Edge", JOptionPane.INFORMATION_MESSAGE);


            } else if(endpt1 != null && endpt2 == null) {
                endpt2 = (NodeDataImpl) ag.findVertex(e.getPoint(), WorldToFrame);
                JOptionPane.showMessageDialog(null, "Node 2: " + endpt2, "Add Edge", JOptionPane.INFORMATION_MESSAGE);

            }
            if (endpt1 != null && endpt2 != null&& !endpt1.equals(endpt2)) {
//                String w = JOptionPane.showInputDialog("Enter Weight: ");
                double weight = Double.parseDouble(JOptionPane.showInputDialog("Enter Weight: "));
                try {

                    graph.getGraph().connect(endpt1.getKey(), endpt2.getKey(), weight);
                    System.out.println(graph.getGraph().getEdge(endpt1.getKey(), endpt2.getKey()));

                    this.paintComponent(this.getGraphics());
                    updateUI();
                    endpt1 = null;
                    endpt2 = null;


                } catch (NullPointerException ex) {

                }
            }


        }


        if (radioButtonState.equals("Shortest Path")) {

            String src = JOptionPane.showInputDialog("Enter src: ");
            int from = Integer.parseInt(src);

            String dest = JOptionPane.showInputDialog("Enter dest: ");
            int to = Integer.parseInt(dest);
            try {
                List<NodeData> shortestPathList = graph.shortestPath(from, to);

                for (int i = 0; i < graph.shortestPath(from, to).size(); i++) {
                    NodeDataImpl n = (NodeDataImpl) shortestPathList.get(i);
                    n.setNodeState(Color.cyan);
                    drawNode(n, 7, this.getGraphics());
                    updateUI();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                }

                String ans = "Shortest Path Dist: " + graph.shortestPathDist(from, to) + ", Path List: " + graph.shortestPath(from, to);
                JOptionPane.showMessageDialog(null, ans, "Shortest Path", JOptionPane.INFORMATION_MESSAGE);
            }catch (IndexOutOfBoundsException ex){
                JOptionPane.showMessageDialog(null, "No Path Between", "Shortest Path", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        Color randomColor = new Color(r , g,b);
        if (radioButtonState.equals("isConnected")) {
            List<Set<NodeData>> list = ag.SCC();
            for (Set<NodeData> set : list) {
                for(NodeData nodeData : set){
                    NodeDataImpl n = (NodeDataImpl) nodeData;
                    n.setNodeState(randomColor);
                    drawNode(n, 7, this.getGraphics());
                    updateUI();
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

            }


            String ans = "isConnected: " + String.valueOf(graph.isConnected());
            JOptionPane.showMessageDialog(null, ans, "isConnected", JOptionPane.INFORMATION_MESSAGE);

        }
        if (radioButtonState.equals("CENTER")) {
            String ans = "CENTER: " + graph.center();
            NodeDataImpl n = (NodeDataImpl) graph.center();
            n.setNodeState(Color.cyan);
            drawNode(n, 5 , this.getGraphics());
            try {
                Thread.sleep(400);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, ans, "CENTER", JOptionPane.INFORMATION_MESSAGE);
            updateUI();
        }

        if(radioButtonState.equals("TSP")){
            List<NodeData> nodes = new ArrayList<>();
            String st = JOptionPane.showInputDialog("Enter Number of Nodes: ");
            int n = Integer.parseInt(st);
            while (n>0){
                String src = JOptionPane.showInputDialog("Enter key: ");
                int keyZ = Integer.parseInt(src);

                nodes.add(graph.getGraph().getNode(keyZ));
                n--;
            }
             List<NodeData> tsp =ag.tsp(nodes);

            for (int i = 0; i <tsp.size(); i++) {
                NodeDataImpl node = (NodeDataImpl) tsp.get(i);

                node.setNodeState(Color.cyan);

                drawNode(node, 7, this.getGraphics());
                updateUI();
                try {
                    Thread.sleep(700);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

            }
            JOptionPane.showMessageDialog(null, "Path Cost: "+ ag.getRouteCost(tsp)+ " ,  Path: " + tsp, "TSP", JOptionPane.INFORMATION_MESSAGE);

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
