package assignment2.ui;

import assignment2.models.GeoLocationImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    public static void initMenu(JFrame j, GraphView view, ActionListener actionListener) {
        JMenuBar mb = new JMenuBar();
        JMenu menu1, menu2, menu3;
        JMenuItem open, save, shortestPath, shortestPathDist, isConnected, center,
                tsp, deleteNode, addNode, deleteEdge, addEdge, newGraph, randomGraph;

        //menu 1
        menu1 = new JMenu("File");
        open = new JMenuItem("Open File");
        save = new JMenuItem("Save");
        newGraph = new JMenuItem("New Graph");
        randomGraph = new JMenuItem("Random Graph");

        menu1.add(newGraph);
        menu1.add(open);
        menu1.add(save);
        menu1.add(randomGraph);

        //menu 2
        menu2 = new JMenu("Algorithms");
        shortestPath = new JMenuItem("Shortest Path");
        shortestPathDist = new JMenuItem("Shortest Path Dist");
        isConnected = new JMenuItem("Is Strongly Connected ?");
        center = new JMenuItem("Center");
        tsp = new JMenuItem("TSP");

        menu2.add(shortestPath);
        menu2.add(shortestPathDist);
        menu2.add(isConnected);
        menu2.add(center);


        //graph actions
        menu3 = new JMenu("Graph");
        deleteNode = new JMenuItem("Remove Node");
        addNode = new JMenuItem("Add Node");
        deleteEdge = new JMenuItem("Remove Edge");
        addEdge = new JMenuItem("Add Edge");

        menu3.add(addNode);
        menu3.add(deleteNode);
        menu3.add(addEdge);
        menu3.add(deleteEdge);


        mb.add(menu1);
        mb.add(menu2);
        mb.add(menu3);
        j.setJMenuBar(mb);


        //menu 1
        save.addActionListener((ActionEvent e) -> {
            String res = JOptionPane.showInputDialog(null, "Enter file name :", "");
            if (res != null && !res.trim().isEmpty())
                view.getController().onTriggerEvent(new GraphEvents.SaveGraph(res + ".json"));
        });


        open.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(fileChooser);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filepath = fileChooser.getSelectedFile().getAbsolutePath();
                view.getController().onTriggerEvent(new GraphEvents.LoadGraph(filepath));
            }
        });


        newGraph.addActionListener((ActionEvent e) -> {
            view.getController().onTriggerEvent(new GraphEvents.NewGraph());
        });

        randomGraph.addActionListener((ActionEvent e) -> {
            view.getController().onTriggerEvent(new GraphEvents.RandomGraph());
        });

        //menu 2
        shortestPathDist.addActionListener((ActionEvent event) -> {
            String res = JOptionPane.showInputDialog(null, "Enter 2 nodes ids : ", "0,4");
            if (res == null)
                return;
            view.getController().onTriggerEvent(new GraphEvents.ShortestPathDist(Integer.parseInt(res.charAt(0) + ""),
                    Integer.parseInt(res.charAt(2) + "")));
            view.updateUI();
        });

        shortestPath.addActionListener((ActionEvent event) -> {
            String res = JOptionPane.showInputDialog(null, "Enter 2 nodes ids : ", "0,4");
            if (res == null)
                return;
            String[] data = res.split(",");
            int src, dest;

            try {
                src = Integer.parseInt(data[0].trim());
                dest = Integer.parseInt(data[1].trim());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                actionListener.actionEvent(new UIEvents.ShowMessage("Enter numbers only separated by ,"));
                return;
            }

            view.getController().onTriggerEvent(new GraphEvents.ShortestPath(src, dest));
        });

        isConnected.addActionListener((ActionEvent event) -> {
            view.getController().onTriggerEvent(new GraphEvents.IsConnected());
        });

        center.addActionListener((ActionEvent event) -> {
            view.getController().onTriggerEvent(new GraphEvents.Center());
        });

        tsp.addActionListener((ActionEvent e) -> {
            String res = JOptionPane.showInputDialog(null, "Enter nodes ", "1,2,3,4,5");
            if (res == null)
                return;
            String[] nodes = res.split(",");
            List<Integer> cities = new ArrayList<>();

            for (int i = 0; i < nodes.length; i++) {
                try {
                    cities.add(Integer.parseInt(nodes[i].trim()));
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    actionListener.actionEvent(new UIEvents.ShowMessage("Enter numbers only separated by ,"));
                    return;
                }
            }

            view.getController().onTriggerEvent(new GraphEvents.TSP(cities));
        });


        //graph
        deleteNode.addActionListener((ActionEvent e) -> {
            String res = JOptionPane.showInputDialog(null, "Enter node key ", "0");
            if (res == null)
                return;
            int key;
            try {
                key = Integer.parseInt(res.trim());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                actionListener.actionEvent(new UIEvents.ShowMessage("enter numbers only !"));
                return;
            }
            view.getController().onTriggerEvent(new GraphEvents.RemoveNode(key));
        });


        addNode.addActionListener((ActionEvent e) -> {
            String res = JOptionPane.showInputDialog(null, "Enter node KEY,X,Y ", "20,50,50");
            if (res == null)
                return;
            double x, y;
            int key;
            try {
                String[] data = res.split(",");
                key = Integer.parseInt(data[0].trim());
                x = Double.parseDouble(data[1].trim());
                y = Double.parseDouble(data[2].trim());
            } catch (Exception ex) {
                ex.printStackTrace();
                actionListener.actionEvent(new UIEvents.ShowMessage("enter numbers only separated by , !"));
                return;
            }
            double newX = view.screenXRange.toRange(view.worldXRange, x);
            double newY = view.screenYRange.toRange(view.worldYRange, y);

            view.getController().onTriggerEvent(new GraphEvents.AddNode(newX, newY, key));
        });


        deleteEdge.addActionListener((ActionEvent e) -> {
            String res = JOptionPane.showInputDialog(null, "Enter node SRC,DEST ", "2,5");
            if (res == null)
                return;
            String[] data = res.split(",");
            int src, dest;
            try {
                src = Integer.parseInt(data[0].trim());
                dest = Integer.parseInt(data[1].trim());
            } catch (Exception ex) {
                ex.printStackTrace();
                actionListener.actionEvent(new UIEvents.ShowMessage("enter numbers only separated by , !"));
                return;
            }
            view.getController().onTriggerEvent(new GraphEvents.DeleteEdge(src, dest));
        });


        addEdge.addActionListener((ActionEvent e) -> {
            String res = JOptionPane.showInputDialog(null, "Enter node SRC,DEST,WEIGHT ", "2,5");
            if (res == null)
                return;
            String[] data = res.split(",");
            int src, dest;
            double w;
            try {
                src = Integer.parseInt(data[0].trim());
                dest = Integer.parseInt(data[1].trim());
                w = Double.parseDouble(data[2].trim());
            } catch (Exception ex) {
                ex.printStackTrace();
                actionListener.actionEvent(new UIEvents.ShowMessage("enter numbers only separated by , !"));
                return;
            }
            view.getController().onTriggerEvent(new GraphEvents.AddEdge(src, dest, w));
        });


    }

}
