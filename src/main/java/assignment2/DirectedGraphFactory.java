package assignment2;

import assignment2.api.DirectedWeightedGraph;
import assignment2.models.GeoLocationImpl;
import assignment2.models.NodeDataImpl;
import assignment2.ui.utils.Range;

import java.util.Random;

public class DirectedGraphFactory {
    static final int THRESHOLD = 100;

    public static DirectedWeightedGraph instantiate(long seed) {
        System.out.println("Seed number " + seed);
        DirectedWeightedGraph g = new DirectedWeightedGraphImpl();
        Random r = new Random(seed);
        int numberOfNodes = Math.abs(r.nextInt()) % THRESHOLD;
        int numberOfEdges = Math.abs(r.nextInt()) % THRESHOLD;


        for (int i = 0; i < numberOfNodes; i++) {
            g.addNode(new NodeDataImpl(i, new GeoLocationImpl(Math.abs(r.nextInt() % 100), Math.abs(r.nextInt() % 100), 0)));
        }

        for (int i = 0; i < numberOfEdges; i++) {
            g.connect(Math.abs(r.nextInt()) % numberOfNodes, Math.abs(r.nextInt()) % numberOfNodes, r.nextInt() % 100);
        }

        return g;
    }

    public static DirectedWeightedGraph instantiate(int numberOfNodes, int numberOfEdges, long seed) {
        if (numberOfEdges > numberOfNodes * (numberOfNodes - 1) / 2)
            throw new RuntimeException("number of edges is not possible ");
        System.out.println("Seed number " + seed);
        DirectedWeightedGraph g = new DirectedWeightedGraphImpl();
        Random r = new Random(seed);

        for (int i = 0; i < numberOfNodes; i++) {
            g.addNode(new NodeDataImpl(i, new GeoLocationImpl(Math.abs(r.nextInt() % 100), Math.abs(r.nextInt() % 100), 0)));
        }

        for (int i = 0; i < numberOfEdges; i++) {
            g.connect(Math.abs(r.nextInt()) % numberOfNodes, Math.abs(r.nextInt()) % numberOfNodes, r.nextInt() % 100);
        }

        return g;
    }
}
