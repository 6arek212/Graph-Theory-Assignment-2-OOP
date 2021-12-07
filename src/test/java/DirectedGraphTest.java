import assignment2.AlgorithmsImpl;
import assignment2.DirectedWeightedGraphImpl;
import assignment2.api.DirectedWeightedGraph;
import assignment2.api.DirectedWeightedGraphAlgorithms;
import assignment2.models.GeoLocationImpl;
import assignment2.models.NodeDataImpl;
import assignment2.utils.DirectedGraphFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectedGraphTest {



    @Test
    public void add1000Nodes(){
        long start = System.currentTimeMillis();
        DirectedWeightedGraph graph = DirectedGraphFactory.instantiate(1000,20,1);
        long end = System.currentTimeMillis();
        long totalTime = end - start;
        System.out.println("totalTime " + totalTime);
    }



    @Test
    public void addNode() {
        DirectedWeightedGraph graph = new DirectedWeightedGraphImpl();
        graph.addNode(new NodeDataImpl(0, new GeoLocationImpl(2, 3, 0)));
        assertEquals(1, graph.nodeSize());
    }

    @Test
    public void deleteNode() {
        DirectedWeightedGraph graph = new DirectedWeightedGraphImpl();
        graph.addNode(new NodeDataImpl(0, new GeoLocationImpl(2, 3, 0)));
        graph.removeNode(0);
        assertEquals(0, graph.nodeSize());
    }


    @Test
    public void addEdge() {
        DirectedWeightedGraph graph = new DirectedWeightedGraphImpl();
        graph.addNode(new NodeDataImpl(0, new GeoLocationImpl(2, 3, 0)));
        graph.addNode(new NodeDataImpl(1, new GeoLocationImpl(2, 3, 0)));
        graph.connect(0, 1, 5);
        assertEquals(1, graph.edgeSize());
    }

    @Test
    public void removeEdge() {
        DirectedWeightedGraph graph = new DirectedWeightedGraphImpl();
        graph.addNode(new NodeDataImpl(0, new GeoLocationImpl(2, 3, 0)));
        graph.addNode(new NodeDataImpl(1, new GeoLocationImpl(2, 3, 0)));
        graph.connect(0, 1, 5);
        graph.removeEdge(0, 1);
        assertEquals(0, graph.edgeSize());
    }
}
