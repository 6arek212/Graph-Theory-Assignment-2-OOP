import assignment2.DirectedWeightedGraphImpl;
import assignment2.Ex2;
import assignment2.api.DirectedWeightedGraph;
import assignment2.api.DirectedWeightedGraphAlgorithms;
import assignment2.utils.DirectedGraphFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;

public class AlgorithmsTest {
    private final static int SEED = 1;

    DirectedWeightedGraphAlgorithms alg;

    @BeforeEach
    public void init(){
        alg = Ex2.getGrapgAlgo("G1.json");
    }


    @Test
    public void shortestPath() {
        assertEquals(5.350731924801653,alg.shortestPathDist(0,4));
    }


    @Test
    public void center() {
        assertEquals(0,alg.center().getKey());
    }


    @Test
    public void tsp() {

    }


    @Test
    public void icConnected() {
        assertTrue(alg.isConnected());
    }

}
