package implementation;

import java.awt.Point;

import GUI.GraphFrame;
import GUI.Range2Range;
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


    private DirectedWeightedGraph g;
    private DirectedWeightedGraph reverseGraph;


    public AlgorithmsImpl(DirectedWeightedGraph g) {
        this.g = g;

    }

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


        if (SCC().size() == 1)
            return true;


        return false;
    }


    public List<Set<NodeData>> SCC() {

        Deque<NodeData> stack = new ArrayDeque<>();
        Set<NodeData> visited = new HashSet<>();


        for (Iterator<NodeData> it = g.nodeIter(); it.hasNext(); ) {
            NodeData node = it.next();
            if (visited.contains(node)) {
                continue;
            }

            DFS(node, visited, stack);
        }

        this.reverseGraph = reverse();
        visited.clear();
        List<Set<NodeData>> components = new ArrayList<>();
        while (!stack.isEmpty()) {

            NodeData n = reverseGraph.getNode(stack.poll().getKey());
            if (visited.contains(n)) {
                continue;
            }
            //storing the one scc
            Set<NodeData> set = new HashSet<>();

            DFSForReverseGraph(n, visited, set);
            components.add(set);


        }


        return components;
    }

    //second DFS on reversed Graph
    private void DFSForReverseGraph(NodeData n, Set<NodeData> visited, Set<NodeData> set) {
        visited.add(n);
        set.add(n);
        for (Iterator<EdgeData> it = reverseGraph.edgeIter(n.getKey()); it.hasNext(); ) {
            EdgeData e = it.next();
            NodeData curr = reverseGraph.getNode(e.getDest());
            if (visited.contains(curr)) {
                continue;
            }
            DFSForReverseGraph(curr, visited, set);
        }


    }

    private DirectedWeightedGraph reverse() {
        DirectedWeightedGraph reverseGraph = new DirectedWeightedGraphImpl();

        for (Iterator<EdgeData> it = g.edgeIter(); it.hasNext(); ) {
            EdgeData edge = it.next();
//            System.out.println("No reverse: " + edge);

            reverseGraph.addNode(g.getNode(edge.getSrc()));
            reverseGraph.addNode(g.getNode(edge.getDest()));
            reverseGraph.connect(edge.getDest(), edge.getSrc(), edge.getWeight());

        }
        for (Iterator<NodeData> it = g.nodeIter(); it.hasNext(); ) {
            NodeData nodeData = it.next();
            reverseGraph.addNode(nodeData);

        }

        return reverseGraph;
    }

    //First DFS
    private void DFS(NodeData node, Set<NodeData> visited, Deque<NodeData> stack) {

        visited.add(node);
        for (Iterator<EdgeData> it = g.edgeIter(node.getKey()); it.hasNext(); ) {

            EdgeData e = it.next();

            NodeData curr = g.getNode(e.getDest());
            if (visited.contains(curr)) {
                continue;
            }
            DFS(curr, visited, stack);
        }

        stack.offerFirst(node);

    }


    @Override
    public double shortestPathDist(int src, int dest) {

        double ans = 0;

        List<NodeData> shortestPathList = shortestPath(src, dest);

        if (shortestPathList == null) {
            return -1;
        }


        return shortestPathList.get(shortestPathList.size() - 1).getWeight();

    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {


        PriorityQueue<NodeData> dist = new PriorityQueue<>((NodeData o1, NodeData o2) -> {
            if (o1.getWeight() < o2.getWeight())
                return -1;
            if (o1.getWeight() > o2.getWeight())
                return 1;
            return 0;
        });
        for (Iterator<NodeData> it = g.nodeIter(); it.hasNext(); ) {
            NodeData nodeData = it.next();
            nodeData.setWeight(Integer.MAX_VALUE);
        }
        NodeData start = g.getNode(src);
        HashMap<Integer, ArrayList<NodeData>> shortestListsMap = new HashMap<>();
        for (Iterator<NodeData> it = g.nodeIter(); it.hasNext(); ) {
            NodeData nodeData = it.next();
            shortestListsMap.put(nodeData.getKey(), new ArrayList<>());
            shortestListsMap.get(nodeData.getKey()).add(start);
        }

        start.setWeight(0);
        dist.add(start);
        shortestListsMap.get(dest).add(start);

        while (!dist.isEmpty()) {

            NodeData currentNode = dist.poll();
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
//                    dist.put(neighborNode.getKey(), neighborNode);
                    dist.add(neighborNode);
                }
            }

        }


        return shortestListsMap.get(dest);

    }

    public double maxInThePath(NodeData nodeData) {
        double max = Integer.MIN_VALUE;

        for (Iterator<NodeData> it = g.nodeIter(); it.hasNext(); ) {
            NodeData node = it.next();
            if (nodeData.getKey() != node.getKey()) {
                double shortestP = shortestPathDist(nodeData.getKey(), node.getKey());
                if (max < shortestP) {
                    max = shortestP;


                }

            }
        }
        return max;
    }

    @Override
    public NodeData center() {
        if (g.nodeSize() == 0) {
            return null;
        }

        double min = Integer.MAX_VALUE;
        NodeData ans = null;

        for (Iterator<NodeData> it = g.nodeIter(); it.hasNext(); ) {
            NodeData node = it.next();
            double temp = maxInThePath(node);
            if (temp < min) {
                min = temp;
                ans = node;

            }

        }
        return ans;
    }


    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        // check if sub graph is connected
        if(!isConnected()){
            return null;
        }
        List<Integer> targets = new ArrayList<>();
        for (NodeData n : cities) {
            targets.add(n.getKey());
        }

        List<Integer> targetTo = new ArrayList<Integer>(targets);
        List<NodeData> res = new ArrayList<NodeData>();
        int src = targetTo.get(0);
        if (targets.size() == 1)
            return shortestPath(src, src);

        int dest = targetTo.get(1);

        while (!targetTo.isEmpty()) {

            if (!res.isEmpty() && res.get(res.size() - 1).getKey() == src)
                res.remove(res.size() - 1);
            List<NodeData> tmp = shortestPath(src, dest);
            targetTo.removeAll(nodesToInts(tmp));
            res.addAll(tmp);
            if (!targetTo.isEmpty()) {
                src = dest;
                dest = targetTo.get(0);
            }

        }

        return res;
    }

    private List<Integer> nodesToInts(List<NodeData> list) {
        List<Integer> ans = new ArrayList<Integer>();
        for (NodeData n : list) {
            ans.add(n.getKey());
        }
        return ans;
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


    public NodeData findVertex(Point userClick, Range2Range WorldToFrame) {
        for (Iterator<NodeData> it = this.getGraph().nodeIter(); it.hasNext(); ) {
            NodeDataImpl v = (NodeDataImpl) it.next();
            NodeDataImpl temp = new NodeDataImpl(v.getLocation(), WorldToFrame);

            if (temp.getVisualNode().contains(userClick)) {
                return v;
            }
        }
        return null;

    }
   public Double getRouteCost(List<NodeData> nodes) {
        double tempCost = 0;
        // Add route costs

        for (int i = 0; i < nodes.size() - 1; i++) {
            tempCost += g.getEdge(nodes.get(i).getKey() , nodes.get(i+1).getKey()).getWeight();

        }
        return tempCost;
    }

    public static void main(String[] args) {
        DirectedWeightedGraph g = new DirectedWeightedGraphImpl();


        AlgorithmsImpl ag = new AlgorithmsImpl(g);


        ag.load("G2.json");
        List<NodeData> citits = new ArrayList<>();
//        for (Iterator<NodeData> it = ag.getGraph().nodeIter(); it.hasNext(); ) {
//            NodeData n = it.next();
//            citits.add(n);
//
//        }


//        System.out.println("Cost 1: "+ ag.getRouteCost(ag.tsp(citits)) + "->" + ag.tsp(citits));



        new GraphFrame(ag);

    }

}

