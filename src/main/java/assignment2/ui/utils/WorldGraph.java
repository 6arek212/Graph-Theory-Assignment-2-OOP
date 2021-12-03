package assignment2.ui.utils;


import assignment2.api.DirectedWeightedGraph;
import assignment2.api.GeoLocation;
import assignment2.api.NodeData;

import java.util.Iterator;

public class WorldGraph {

    private static Range2D GraphRange(DirectedWeightedGraph g, Range2D frame) {
        Iterator<NodeData> itr = g.nodeIter();
        double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
        boolean first = true;
        while (itr.hasNext()) {
            GeoLocation p = itr.next().getLocation();
            if (first) {
                x0 = p.x();
                x1 = x0;
                y0 = p.y();
                y1 = y0;
                first = false;
            } else {
                if (p.x() < x0) {
                    x0 = p.x();
                }
                if (p.x() > x1) {
                    x1 = p.x();
                }
                if (p.y() < y0) {
                    y0 = p.y();
                }
                if (p.y() > y1) {
                    y1 = p.y();
                }
            }
        }
        if (x0 == x1) {
            x0 = 0;
            x1 = frame.getMaxY();
        }

        if (y0 == y1) {
            y0 = 0;
            y1 = frame.getMaxY();
        }

        Range xr = new Range(x0, x1);
        Range yr = new Range(y0, y1);
        return new Range2D(xr, yr);
    }

    public static Range2Range w2f(DirectedWeightedGraph g, Range2D frame) {
        Range2D world = GraphRange(g, frame);
        return new Range2Range(world, frame);
    }


    public static Range2D getWorldRange2D(DirectedWeightedGraph g, Range2D frame) {
        return GraphRange(g, frame);
    }
}