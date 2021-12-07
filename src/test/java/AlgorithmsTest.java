import assignment2.AlgorithmsImpl;
import assignment2.DirectedWeightedGraphImpl;
import assignment2.Ex2;
import assignment2.api.DirectedWeightedGraph;
import assignment2.api.DirectedWeightedGraphAlgorithms;
import assignment2.api.NodeData;
import assignment2.utils.DirectedGraphFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlgorithmsTest {
    private final static int SEED = 1;


    static DirectedWeightedGraphAlgorithms alg;

    @BeforeAll
    public static void init() {
        alg = new AlgorithmsImpl("G1.json");
    }


    @Test
    public void shortestPath() {
        assertEquals(5.350731924801653, alg.shortestPathDist(0, 4));
    }


    @Test
    public void center() {
        assertEquals(8, alg.center().getKey());
    }


    @Test
    public void tsp() {
        List<NodeData> cities = new ArrayList<>();
        cities.add(alg.getGraph().getNode(1));
        cities.add(alg.getGraph().getNode(2));
        cities.add(alg.getGraph().getNode(3));
        cities.add(alg.getGraph().getNode(4));
        cities.add(alg.getGraph().getNode(5));

        List<NodeData> path =alg.tsp(cities);
        for (int i = 0; i < path.size(); i++) {
            assertEquals(i+1 , path.get(i).getKey());
        }
    }


    @Test
    public void icConnected() {
        assertTrue(alg.isConnected());
    }

}
