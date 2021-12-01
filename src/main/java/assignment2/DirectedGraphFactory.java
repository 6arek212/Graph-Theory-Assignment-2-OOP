package assignment2;

import assignment2.api.DirectedWeightedGraph;
import assignment2.models.GeoLocationImpl;
import assignment2.models.NodeDataImpl;
import assignment2.ui.utils.Range;

import java.util.Random;

public class DirectedGraphFactory {


    public static DirectedWeightedGraph instantiate(int numberOfNodes, int numberOfEdges, long seed) {
        DirectedWeightedGraph g = new DirectedWeightedGraphImpl();
        Range x = new Range(0, 1);
        Range y = new Range(0, 1);
        Random r = new Random(seed);

        for (int i = 0; i < numberOfNodes; i++) {
            g.addNode(new NodeDataImpl(i, new GeoLocationImpl(r.nextInt(),r.nextInt(),0)));
        }

        for (int i = 0; i < numberOfEdges; i++)
            g.connect(r.nextInt(),r.nextInt(),r.nextInt());


        return g;
    }


}
