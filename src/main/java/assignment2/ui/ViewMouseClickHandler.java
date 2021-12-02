package assignment2.ui;

import assignment2.api.NodeData;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/***
 *      this class handles the Click on the graph
 *
 *      add new node by clicking anywhere
 *      add new edge by drag and drop
 *
 *
 */
public class ViewMouseClickHandler implements MouseListener {

    private NodeData fromNode;
    private ActionListener actionListener;
    private GraphView view;

    public ViewMouseClickHandler(ActionListener actionListener, GraphView view) {
        this.actionListener = actionListener;
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (view.getNodeByCoordinates(e.getX(), e.getY()) != null) {
            actionListener.actionEvent(new UIEvents.ShowMessage("There is already an existing node in these coordinates"));
            return;
        }

        String res = JOptionPane.showInputDialog(null, "Enter node KEY", "20");
        if (res == null)
            return;
        int key;
        try {
            key = Integer.parseInt(res.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            actionListener.actionEvent(new UIEvents.ShowMessage("enter numbers only !"));
            return;
        }

        double worldX = view.screenXRange.toRange(view.worldXRange, e.getX());
        double worldY = view.screenYRange.toRange(view.worldYRange, e.getY());
        view.getController().onTriggerEvent(new GraphEvents.AddNode(worldX, worldY, key));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        fromNode = view.getNodeByCoordinates(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (fromNode != null) {
            NodeData node = view.getNodeByCoordinates(e.getX(), e.getY());
            if (node != null && node != fromNode) {
                String res = JOptionPane.showInputDialog(null, "Enter edge weight", "20");
                if (res == null)
                    return;
                double w;
                try {
                    w = Double.parseDouble(res.trim());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    actionListener.actionEvent(new UIEvents.ShowMessage("enter numbers only !"));
                    return;
                }

                view.getController().onTriggerEvent(new GraphEvents.AddEdge(fromNode.getKey(), node.getKey(), w));
            }
        }
        fromNode = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}
