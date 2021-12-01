package ui;

import models.GeoLocationImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    public static void initMenu(JFrame j, GraphView view, ActionListener actionListener) {
        JMenuBar mb = new JMenuBar();
        JMenu menu1, menu2, menu3;
        JMenuItem load, save, shortestPath, shortestPathDist, isConnected, center,
                fullGraph, tsp, deleteNode, addNode, deleteEdge, addEdge;

        //menu 1
        menu1 = new JMenu("File");
        load = new JMenuItem("load");
        save = new JMenuItem("save");
        menu1.add(load);
        menu1.add(save);

        //menu 2
        menu2 = new JMenu("Algorithms");
        shortestPath = new JMenuItem("shortest path");
        shortestPathDist = new JMenuItem("shortest path dist");
        isConnected = new JMenuItem("is strongly connected ?");
        center = new JMenuItem("center");
        fullGraph = new JMenuItem("Show Full Graph");
        tsp = new JMenuItem("tsp");

        menu2.add(shortestPath);
        menu2.add(shortestPathDist);
        menu2.add(isConnected);
        menu2.add(center);
        menu2.add(fullGraph);


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
            view.getController().onTriggerEvent(new GraphEvents.SaveGraph(res + ".txt"));
        });


        load.addActionListener((ActionEvent e) -> {
            String res = JOptionPane.showInputDialog(null, "Enter json file name :", "G1") + ".json";
            if (!view.getController().getAlgo().load(res)) {
                JOptionPane.showMessageDialog(null, "Error file was not found");
            } else {
                view.getController().onTriggerEvent(new GraphEvents.LoadGraph(res));
                view.calculateRange();
            }
            view.updateUI();
        });


        //menu 2
        shortestPathDist.addActionListener((ActionEvent event) -> {
            String res = JOptionPane.showInputDialog(null, "Enter 2 nodes ids : ", "0,4");
            view.getController().onTriggerEvent(new GraphEvents.ShortestPathDist(Integer.parseInt(res.charAt(0) + ""),
                    Integer.parseInt(res.charAt(2) + "")));
            view.updateUI();
        });

        shortestPath.addActionListener((ActionEvent event) -> {
            String res = JOptionPane.showInputDialog(null, "Enter 2 nodes ids : ", "0,4");
            view.getController().onTriggerEvent(new GraphEvents.ShortestPath(Integer.parseInt(res.charAt(0) + ""),
                    Integer.parseInt(res.charAt(2) + "")));
            view.updateUI();
        });

        isConnected.addActionListener((ActionEvent event) -> {
            view.getController().onTriggerEvent(new GraphEvents.IsConnected());
            view.updateUI();
        });

        center.addActionListener((ActionEvent event) -> {
            view.getController().onTriggerEvent(new GraphEvents.Center());
            view.updateUI();
        });

        tsp.addActionListener((ActionEvent e) -> {
            String res = JOptionPane.showInputDialog(null, "Enter nodes ", "1,2,3,4,5");
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
            view.updateUI();
        });

        fullGraph.addActionListener((ActionEvent e) -> {
            view.getController().onTriggerEvent(new GraphEvents.FullGraph());
            view.updateUI();
        });


        //graph
        deleteNode.addActionListener((ActionEvent e) -> {
            String res = JOptionPane.showInputDialog(null, "Enter node key ", "0");
            int key;
            try {
                key = Integer.parseInt(res.trim());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                actionListener.actionEvent(new UIEvents.ShowMessage("enter numbers only !"));
                return;
            }
            view.getController().onTriggerEvent(new GraphEvents.RemoveNode(key));
            view.updateUI();
        });


        addNode.addActionListener((ActionEvent e) -> {
            String res = JOptionPane.showInputDialog(null, "Enter node KEY,X,Y ", "20,50,50");
            String[] data = res.split(",");
            double x, y;
            int key;
            try {
                key = Integer.parseInt(data[0].trim());
                x = Double.parseDouble(data[1].trim());
                y = Double.parseDouble(data[2].trim());
            } catch (Exception ex) {
                ex.printStackTrace();
                actionListener.actionEvent(new UIEvents.ShowMessage("enter numbers only separated by , !"));
                return;
            }
            view.getController().onTriggerEvent(new GraphEvents.AddNode(new GeoLocationImpl(x, y, 0), key));
            view.calculateRange();
            view.updateUI();
        });


        deleteEdge.addActionListener((ActionEvent e) -> {
            String res = JOptionPane.showInputDialog(null, "Enter node SRC,DEST ", "2,5");
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
            view.updateUI();
        });


        addEdge.addActionListener((ActionEvent e) -> {
            String res = JOptionPane.showInputDialog(null, "Enter node SRC,DEST,WEIGHT ", "2,5");
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
            view.updateUI();
        });


    }

}
