import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import ui.DisplayView;


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
        AlgorithmsImpl ag = new AlgorithmsImpl();
        ag.load(json_file);
        return ag.getGraph();
    }


    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        AlgorithmsImpl ag = new AlgorithmsImpl();
        ag.load(json_file);
        return ag;
    }

    /**
     * This static function will run your GUI using the json fime.
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     */
    public static void runGUI(String json_file) {
        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);
        JFrame j = new JFrame();
        j.setBackground(Color.WHITE);
        j.setSize(500, 400);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);

        //remove node
        //alg.getGraph().removeNode(0);

        //remove edge
//        alg.getGraph().removeEdge(0,1);
//        alg.getGraph().removeEdge(0,21);
//        alg.getGraph().removeEdge(0,16);
//        alg.getGraph().removeEdge(1,0);
//        alg.getGraph().removeEdge(21,0);
//        alg.getGraph().removeEdge(16,0);

        //connect
        //alg.getGraph().connect(16,0,50);
        DisplayView v = new DisplayView(alg, j.getHeight(), j.getWidth());
        v.fullGraph();
        j.add(v);

    }


    public static void main(String[] args) {
        runGUI("G1.json");
    }
}