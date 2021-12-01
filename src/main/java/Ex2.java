import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import ui.*;
import ui.Menu;
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
        JFrame j = new JFrame();
        ActionListener actionListener = (UIEvents event) -> {
            if (event instanceof UIEvents.ShowMessage)
                JOptionPane.showMessageDialog(null, ((UIEvents.ShowMessage) event).getMessage());
        };
        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);
        GraphView v = new GraphView(new GraphViewModel(alg, actionListener), j.getHeight(), j.getWidth());
        Menu.initMenu(j, v, actionListener);
        j.setBackground(Color.WHITE);
        j.setSize(800, 600);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);
        j.add(v);
    }



    public static void main(String[] args) {
        runGUI("G3.json");
    }
}