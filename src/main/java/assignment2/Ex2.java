package assignment2;

import assignment2.api.DirectedWeightedGraph;
import assignment2.api.DirectedWeightedGraphAlgorithms;
import assignment2.ui.*;
import assignment2.ui.Menu;

import javax.swing.*;
import java.awt.*;


/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGrapg(String json_file) {
        AlgorithmsImpl ag = new AlgorithmsImpl(json_file);
        return ag.getGraph();
    }


    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        AlgorithmsImpl ag = new AlgorithmsImpl(json_file);
        return ag;
    }

    /**
     * This static function will run your GUI using the json fime.
     * <p>
     * MVI architecture is used for structuring the ui (MODEL VIEW INTENT)
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     */
    public static void runGUI(String json_file) {
        new GraphView(getGrapgAlgo(json_file));
    }


    public static void main(String[] args) {
        runGUI("G1.json");
    }
}