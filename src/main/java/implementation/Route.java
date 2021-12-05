package implementation;


import api.NodeData;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private final NodeData startCity; // The first city in route
    private NodeData currentCity; // Location
    private List<NodeData> route = new ArrayList<>(); // List of cities


    public Route() {
        startCity = null;
    }

    /**
     * Constructor with variables
     *
     * @param startCity
     */
    public Route(NodeData startCity) {
        this.startCity = startCity;
        this.currentCity = startCity;
        this.route.add(startCity);
    }

    // Mutator functions

    public NodeData getStartCity() {
        return startCity;
    }

    public NodeData getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(NodeData currentCity) {
        this.currentCity = currentCity;
    }

    public List<NodeData> getRoute() {
        return route;
    }

    public void setRoute(List<NodeData> route) {
        this.route = route;
    }

    @Override
    public String toString() {
        String r = "";
        if (!route.isEmpty()) {
            // Short route for easier display
            for (NodeData c : route) {
                r += c.getKey() + ",";
            }

            // Remove trailing comma
            r = r.substring(0, r.length() - 1);
        }
        return "Route{" + r + '}';
    }
}