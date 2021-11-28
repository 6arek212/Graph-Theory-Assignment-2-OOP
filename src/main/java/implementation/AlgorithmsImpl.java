package implementation;

import json_impl.fromJsonToGraph;
import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.nio.file.Paths;
import java.sql.Array;
import java.util.*;
import java.io.*;

public class AlgorithmsImpl implements DirectedWeightedGraphAlgorithms {
    static final int WHITE = 1, GRAY = -1, BLACK = 0;

    private DirectedWeightedGraph g;


    @Override
    public void init(DirectedWeightedGraph g) {
        this.g = g;

    }

    @Override
    public DirectedWeightedGraph getGraph() {

        return this.g;
    }

    @Override
    public DirectedWeightedGraph copy() {


        DirectedWeightedGraph new_graph = new DirectedWeightedGraphImpl();
        for (Iterator<NodeData> it = this.g.nodeIter(); it.hasNext(); ) {
            NodeData NodeCopy = it.next();

            for (Iterator<EdgeData> iter = this.g.edgeIter(NodeCopy.getKey()); iter.hasNext(); ) {
                EdgeData EdgeCopy = iter.next();
                new_graph.connect(NodeCopy.getKey(), EdgeCopy.getDest(), EdgeCopy.getWeight());

            }


        }
        for (Iterator<NodeData> it = this.g.nodeIter(); it.hasNext(); ) {
            NodeData nodeCopy = it.next();
            NodeData newNode = this.g.getNode(nodeCopy.getKey());
            new_graph.addNode(newNode);
            String newInfo = nodeCopy.getInfo();
            new_graph.getNode(nodeCopy.getKey()).setInfo(newInfo);

        }

        return new_graph;

    }

    @Override
    public boolean isConnected() {

        for (int i = 0; i < g.nodeSize(); i++) {
            for (int j = 0; j < g.nodeSize(); j++) {
                ArrayList<NodeData> temp = new ArrayList<NodeData>();
                if (i != j && shortestPath(i, j) == null) {
                    return false;


                }


            }
        }


        return true;

    }

//    @Override
//    public double shortestPathDist(int src, int dest) {
//
//
//        HashMap<Integer, NodeData> dist = new HashMap<>();
//        for (Iterator<NodeData> it = g.nodeIter(); it.hasNext(); ) {
//            NodeData nodeData = it.next();
//            nodeData.setWeight(Integer.MAX_VALUE);
//
//        }
//
//        NodeData startingNode = g.getNode(src);
//        startingNode.setWeight(0);
//
//        dist.put(src, startingNode);
//
//        while (!dist.isEmpty()) {
//
//            //go to min wighted node that adj to src node
//            NodeData currNode = this.MinWeightedNode(dist);
//            if (currNode.getKey() == dest) {
//                break;
//            }
//
//            int currKey = currNode.getKey();
//            double currWeight = currNode.getWeight();
//            dist.remove(currKey);
//
//            for (Iterator<EdgeData> it = g.edgeIter(currKey); it.hasNext(); ) {
//                EdgeData e = it.next();
//                Integer neighbor = e.getDest();
//
//                NodeData neighborNode = g.getNode(neighbor);
//                double CurrNodeAndEdgeWeight = g.getEdge(currKey, neighbor).getWeight() + currWeight;
//                if (CurrNodeAndEdgeWeight < neighborNode.getWeight()) {
//                    neighborNode.setWeight(CurrNodeAndEdgeWeight);
//                    dist.put(neighbor, neighborNode);
//                }
//            }
//
//        }
//
//
//        return (g.getNode(dest).getWeight() == Integer.MAX_VALUE) ? -1 : g.getNode(dest).getWeight();
//    }

    @Override
    public double shortestPathDist(int src, int dest) {

        double ans = 0;

        List<NodeData> shortestPathList = shortestPath(src, dest);

        if (shortestPathList == null) {
            return -1;
        }


        return shortestPathList.get(shortestPathList.size()-1).getWeight();

    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        HashMap<Integer, NodeData> dist = new HashMap<Integer, NodeData>();
        for (Iterator<NodeData> it = g.nodeIter(); it.hasNext(); ) {
            NodeData nodeData = it.next();
            nodeData.setWeight(Integer.MAX_VALUE);
        }
        HashMap<Integer, ArrayList<NodeData>> shortestListsMap = new HashMap<>();
        for (Iterator<NodeData> it = g.nodeIter(); it.hasNext(); ) {
            NodeData nodeData = it.next();
            shortestListsMap.put(nodeData.getKey(), new ArrayList<>());
        }
        NodeData start = g.getNode(src);
        start.setWeight(0);
        dist.put(src, start);
        while (!dist.isEmpty()) {
            NodeData currentNode = this.MinWeightedNode(dist);
            if (currentNode.getKey() == dest) {
                break;
            }

            int currentNodeKey = currentNode.getKey();
            double currentNodeDistance = currentNode.getWeight();
            dist.remove(currentNodeKey);
            ArrayList<NodeData> currPath = shortestListsMap.get(currentNodeKey);
            for (Iterator<EdgeData> it = g.edgeIter(currentNodeKey); it.hasNext(); ) {
                EdgeData e = it.next();
                int neighbor = e.getDest();
                double CurrNodeAndEdgeWeight = g.getEdge(currentNodeKey, neighbor).getWeight() + currentNodeDistance;
                NodeData neighborNode = g.getNode(neighbor);
                if (CurrNodeAndEdgeWeight < neighborNode.getWeight()) {
                    ArrayList<NodeData> currNeighborPath = new ArrayList<>(currPath);
                    currNeighborPath.add(neighborNode);
                    shortestListsMap.put(neighborNode.getKey(), currNeighborPath);
                    neighborNode.setWeight(CurrNodeAndEdgeWeight);
                    dist.put(neighborNode.getKey(), neighborNode);
                }
            }

        }

        return shortestListsMap.get(dest);

    }

    private NodeData MinWeightedNode(HashMap<Integer, NodeData> nodes_Map) {
        NodeData minNode = null;
        double minDis = Integer.MAX_VALUE;
        for (NodeData node : nodes_Map.values()) {
            if (node.getWeight() <= minDis) {
                minNode = node;
                minDis = node.getWeight();
            }
        }

        return minNode;
    }


    @Override
    public NodeData center() {

        return null;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;

    }

    @Override
    public boolean save(String file) {
        boolean state = true;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(new Gson().toJson(new fromJsonToGraph(this.g)));
        String prettyJsonString = gson.toJson(je);
        try {
            FileWriter fr = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fr);
            bw.write(prettyJsonString);
            bw.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
            state = false;
        }
        return state;
    }

    @Override
    public boolean load(String file) {
        this.g = DirectedWeightedGraphImpl.load(file);
        if (this.g != null)
            return true;
        return false;
    }


    public static void main(String[] args) {
        DirectedWeightedGraph g = new DirectedWeightedGraphImpl();

        AlgorithmsImpl ag = new AlgorithmsImpl();

        System.out.println(ag.getGraph());
        ag.load("G1.json");
        System.out.println(ag.getGraph());
        System.out.println("Shortest Path -> " + ag.shortestPathDist(0, 7));
        System.out.println("Shortest PathList -> " + ag.shortestPath(0, 7));
    }

}

