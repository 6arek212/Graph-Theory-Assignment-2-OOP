package ui;


import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import ui.utils.Range;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DisplayView extends Canvas {
    private DirectedWeightedGraphAlgorithms graphAlg;
    private Range r1x;
    private Range r1y;
    private Range r2x;
    private Range r2y;
    private final int radios = 20;

    private int src, dest;

    private int mode = PAINT_GRAPH;
    public final static int PAINT_GRAPH = 1;
    public final static int PAINT_SHORTEST = 2;


    public DisplayView(DirectedWeightedGraphAlgorithms graphAlg, int height, int width) {
        this.graphAlg = graphAlg;
        double minX = Integer.MAX_VALUE;
        double maxX = Integer.MIN_VALUE;
        double minY = Integer.MAX_VALUE;
        double maxY = Integer.MIN_VALUE;

        Iterator<NodeData> d = graphAlg.getGraph().nodeIter();
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

        r2x = new Range(0, width);
        r2y = new Range(0, height);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        r2x.setMax(getWidth());
        r2y.setMax(getHeight());
        switch (mode) {
            case PAINT_GRAPH:
                paintFullGraph();
                break;

            case PAINT_SHORTEST:
                paintShortestPath(this.src, this.dest);
                break;
        }
    }


    public void paintFullGraph() {
        this.mode = PAINT_GRAPH;
        List<EdgeData> ed = new ArrayList<>();
        Iterator<EdgeData> itEdge = graphAlg.getGraph().edgeIter();
        while (itEdge.hasNext()) {
            ed.add(itEdge.next());
        }

        List<NodeData> nodes = new ArrayList<>();
        Iterator<NodeData> d = graphAlg.getGraph().nodeIter();
        while (d.hasNext()) {
            nodes.add(d.next());
        }
        printGraph(nodes, ed);
    }


    public void paintShortestPath(int src, int dest) {
        mode = PAINT_SHORTEST;
        this.src = src;
        this.dest = dest;

        List<NodeData> path = graphAlg.shortestPath(src, dest);
        List<EdgeData> edges = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            edges.add(graphAlg.getGraph().getEdge(path.get(i).getKey(), path.get(i + 1).getKey()));
        }

        printGraph(path, edges);
    }


    private void printGraph(List<NodeData> nodes, List<EdgeData> edges) {
        Graphics g = getGraphics();
        g.setColor(Color.BLUE);
        for (int i = 0; i < edges.size(); i++) {
            EdgeData edgeData = edges.get(i);
            NodeData d1 = graphAlg.getGraph().getNode(edgeData.getSrc());
            NodeData d2 = graphAlg.getGraph().getNode(edgeData.getDest());
            g.drawLine((int) r1x.toRange(r2x, d1.getLocation().x()), (int) r1y.toRange(r2y, d1.getLocation().y()),
                    (int) r1x.toRange(r2x, d2.getLocation().x()), (int) r1y.toRange(r2y, d2.getLocation().y()));
        }


        g.setColor(Color.BLACK);
        for (int i = 0; i < nodes.size(); i++) {
            NodeData node = nodes.get(i);
            g.fillOval((int) r1x.toRange(r2x, node.getLocation().x()) - radios / 2, (int) r1y.toRange(r2y, node.getLocation().y()) - radios / 2, radios, radios);
        }
    }


}