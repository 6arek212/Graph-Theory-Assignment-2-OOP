package assignment2;

import assignment2.api.DirectedWeightedGraph;
import assignment2.api.DirectedWeightedGraphAlgorithms;
import assignment2.ui.*;

import java.nio.file.Paths;


/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    private static final String path = Paths.get("").toAbsolutePath() + "/src/main/java/assignment2/data/";


    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGrapg(String json_file) {
        return DirectedWeightedGraphImpl.load(path + json_file);
    }


    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        return new AlgorithmsImpl(getGrapg(json_file));
    }

    /**
     * This static function will run your GUI using the json fime.
     * <p>
     * MVI architecture is used for structuring the ui (MODEL VIEW INTENT)
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     */
    public static void runGUI(String json_file) {
        new GraphViewFrame(getGrapgAlgo(json_file));
    }


    public static void main(String[] args) {
        //runGUI("G1.json");
        try {
            runGUI(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error graph filename was not found ! ,\nExample \"java -jar .\\Ex2.jar G1.json\" ");
        }
    }
}