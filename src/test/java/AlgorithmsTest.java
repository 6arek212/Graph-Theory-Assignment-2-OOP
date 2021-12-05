import assignment2.AlgorithmsImpl;
import assignment2.DirectedWeightedGraphImpl;
import assignment2.Ex2;
import assignment2.api.DirectedWeightedGraph;
import assignment2.api.DirectedWeightedGraphAlgorithms;
import assignment2.utils.DirectedGraphFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.Arrays;

public class AlgorithmsTest {
    private final static int SEED = 1;


    static DirectedWeightedGraphAlgorithms alg;

    @BeforeAll
    public static void init() {
        alg = new AlgorithmsImpl("G1.json");
    }


    @Test
    //@Timeout(value = 5000,unit = TimeUnit.MILLISECONDS)
    public void bigRandomGraph() {
        DirectedWeightedGraphAlgorithms alg = new AlgorithmsImpl();
        alg.init(DirectedGraphFactory.instantiateConnectedGraph(1000, SEED));
        long start = System.currentTimeMillis();
        alg.center();
        long end = System.currentTimeMillis();
        long totalTime = end - start;
        System.out.println("totalTime " + totalTime);
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

    }


    @Test
    public void icConnected() {
        assertTrue(alg.isConnected());
    }

}
