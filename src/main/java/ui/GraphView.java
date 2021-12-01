package ui;


import api.EdgeData;
import api.NodeData;
import ui.utils.Range;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;


/**
 * Simple UI for presenting the graph
 */
public class GraphView extends JPanel {

    private GraphViewModel controller;
    private int height;
    private int width;
    private final int radios = 20;
    private Range r1x;
    private Range r1y;
    private Range r2x;
    private Range r2y;

    public GraphView(GraphViewModel controller, int height, int width) {
        super();
        this.controller = controller;
        this.height=height;
        this.width=width;
        calculateRange();
    }

    public void calculateRange(){
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

        r2x = new Range(0, width);
        r2y = new Range(0, height);
    }



    @Override
    public void paint(Graphics g) {
        super.paint(g);
        r2x.setMax(getWidth());
        r2y.setMax(getHeight());
        printGraph(g);
    }


    private void printGraph(Graphics g) {
        g.setColor(Color.BLUE);
        for (int i = 0; i < controller.getEdges().size(); i++) {
            EdgeData edgeData = controller.getEdges().get(i);
            NodeData d1 = controller.getNode(edgeData.getSrc());
            NodeData d2 = controller.getNode(edgeData.getDest());
            if (d1 != null && d2 != null)
                g.drawLine((int) r1x.toRange(r2x, d1.getLocation().x()), (int) r1y.toRange(r2y, d1.getLocation().y()),
                        (int) r1x.toRange(r2x, d2.getLocation().x()), (int) r1y.toRange(r2y, d2.getLocation().y()));
        }


        g.setColor(Color.BLACK);
        for (int i = 0; i < controller.getNodes().size(); i++) {
            NodeData node = controller.getNodes().get(i);
            if (node != null)
                g.fillOval((int) r1x.toRange(r2x, node.getLocation().x()) - radios / 2, (int) r1y.toRange(r2y, node.getLocation().y()) - radios / 2, radios, radios);
        }
    }

    public GraphViewModel getController() {
        return controller;
    }
}