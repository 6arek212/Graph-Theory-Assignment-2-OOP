package assignment2.utils;

import assignment2.DirectedWeightedGraphImpl;
import assignment2.api.DirectedWeightedGraph;
import assignment2.api.NodeData;
import assignment2.models.GeoLocationImpl;
import assignment2.models.NodeDataImpl;

import java.util.Iterator;
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


    public static DirectedWeightedGraph instantiateConnectedGraph(int numberOfNodes, long seed) {
        System.out.println("Seed number " + seed);
        DirectedWeightedGraph g = new DirectedWeightedGraphImpl();
        Random r = new Random(seed);

        g.addNode(new NodeDataImpl(0, new GeoLocationImpl(Math.abs(r.nextInt() % 100), Math.abs(r.nextInt() % 100), 0)));
        for (int i = 1; i < numberOfNodes; i++) {
            NodeData nd = new NodeDataImpl(i, new GeoLocationImpl(Math.abs(r.nextInt() % 100), Math.abs(r.nextInt() % 100), 0));
            g.addNode(nd);
            g.connect(g.getNode(i-1).getKey(), nd.getKey(), r.nextInt() % 100);
        }
        g.connect(g.getNode(numberOfNodes-1).getKey(), g.getNode(0).getKey(), r.nextInt() % 100);
        return g;
    }

}
