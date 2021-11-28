import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import json_models.DirectedWeightedGraphJson;
import models.NodeDataImpl;

import java.io.*;
import java.util.*;


public class AlgorithmsImpl implements DirectedWeightedGraphAlgorithms {
    private DirectedWeightedGraph graph;
    private final String defaultJsonFile = "G1.json";


    //as default load G1.json
    public AlgorithmsImpl() {
        load(defaultJsonFile);
    }


    @Override
    public void init(DirectedWeightedGraph g) {
        this.graph = g;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.graph;
    }

    @Override
    public DirectedWeightedGraph copy() {
        return new DirectedWeightedGraphImpl(this.graph);
    }

    private List<EdgeData> getInvertAdj(int src) {
        Iterator<EdgeData> iterator = graph.edgeIter();
        List<EdgeData> adj = new ArrayList<>();
        while (iterator.hasNext()) {
            EdgeData d = iterator.next();
            if (d.getDest() == src)
                adj.add(d);
        }
        return adj;
    }


    private void dfsVisit(NodeData node, List<NodeData> nodesList, boolean reversed) {
        List<EdgeData> adj;
        if (reversed)
            adj = getInvertAdj(node.getKey());
        else
            adj = getAdj(node.getKey());


        node.setTag(NodeDataImpl.GRAY);

        for (EdgeData ed : adj) {
            NodeData nd;
            if (reversed)
                nd = graph.getNode(ed.getSrc());
            else
                nd = graph.getNode(ed.getDest());
            if (nd.getTag() == NodeDataImpl.WHITE) {
                dfsVisit(nd, nodesList, reversed);
            }
        }
        nodesList.add(0, node);
        node.setTag(NodeDataImpl.BLACK);
    }

    private List<NodeData> dfs(List<NodeData> nodesList, boolean reversed) {
        for (NodeData d : nodesList) {
            d.setTag(NodeDataImpl.WHITE);
        }

        List<NodeData> p = new ArrayList<>();
        boolean visitAll = true;
        for (NodeData d : nodesList) {
            if (d.getTag() == NodeDataImpl.WHITE && visitAll)
                dfsVisit(d, p, reversed);

            if (reversed)
                visitAll = false;
        }
        return p;
    }


    /**
     * check if there is only one strongly connected component
     * Algorithm: two DFS call's after the first one invert the edges
     * complexity : O(V+E)
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        Iterator<NodeData> iterator = graph.nodeIter();
        List<NodeData> nodes = new ArrayList<>();
        while (iterator.hasNext()) {
            nodes.add(iterator.next());
        }
//        getGraph().removeEdge(0, 1);
//        getGraph().removeEdge(0, 21);
//        getGraph().removeEdge(0, 16);
//        getGraph().removeEdge(1, 0);
//        getGraph().removeEdge(21, 0);
//        getGraph().removeEdge(16, 0);
        return dfs(dfs(nodes, false), true).size() == nodes.size();
    }




    private List<EdgeData> getAdj(int src) {
        Iterator<EdgeData> iterator = graph.edgeIter(src);
        List<EdgeData> adj = new ArrayList<>();
        while (iterator.hasNext()) {
            adj.add(iterator.next());
        }
        return adj;
    }


    @Override
    public double shortestPathDist(int src, int dest) {
        List<NodeData> path = shortestPath(src, dest);
        if (path == null)
            return -1;
        return pathCost(path);
    }

    private double pathCost(List<NodeData> path) {
        double sum = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            sum += graph.getEdge(path.get(i).getKey(), path.get(i + 1).getKey()).getWeight();
        }

        return sum;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        DirectedWeightedGraphImpl g = new DirectedWeightedGraphImpl(graph);
        Iterator<NodeData> iterator = g.nodeIter();

        while (iterator.hasNext()) {
            NodeData d = iterator.next();
            d.setTag(NodeDataImpl.WHITE);
            if (d.getKey() == src)
                d.setWeight(0);
            else {
                d.setWeight(Double.MAX_VALUE);
            }
        }

        NodeData[] dist = new NodeData[g.nodeSize()];
        dist[src] = g.getNode(src);

        PriorityQueue<NodeData> nodeQ = new PriorityQueue<>((NodeData o1, NodeData o2) -> {
            if (o1.getWeight() < o2.getWeight())
                return -1;
            if (o1.getWeight() > o2.getWeight())
                return 1;
            return 0;
        });
        nodeQ.add(g.getNode(src));

        while (!nodeQ.isEmpty()) {
            NodeData node = nodeQ.poll();

            for (EdgeData edge : getAdj(node.getKey())) {
                NodeData connectedto = g.getNode(edge.getDest());
                if (connectedto.getTag() == NodeDataImpl.WHITE) {
                    if (node.getWeight() + edge.getWeight() < connectedto.getWeight()) {
                        connectedto.setWeight(node.getWeight() + edge.getWeight());
                        dist[connectedto.getKey()] = node;
                    }
                    nodeQ.add(connectedto);
                }
            }
            node.setTag(NodeDataImpl.BLACK);
        }


        if (g.getNode(dest).getWeight() == Double.MAX_VALUE)
            return null;

        List<NodeData> path = new ArrayList<>();
        NodeData node = g.getNode(dest);
        while (node.getKey() != src) {
            path.add(0, dist[node.getKey()]);
            node = dist[node.getKey()];
        }
        path.add(g.getNode(dest));

        return path;
    }

    @Override
    public NodeData center() {
        if (!isConnected() || graph.nodeSize() == 0)
            return null;

        Iterator<NodeData> t = graph.nodeIter();
        double currentMin = Integer.MAX_VALUE;
        int node = 0;

        while (t.hasNext()) {
            NodeData nd = t.next();
            double distance = getMaxDistance(nd.getKey());
            if (distance < currentMin) {
                currentMin = distance;
                node = nd.getKey();
            }
        }

        return graph.getNode(node);
    }


    private double getMaxDistance(int src) {
        double nodeMaxDist = Integer.MAX_VALUE;

        for (int i = 0; i < graph.nodeSize(); i++) {
            double d = shortestPathDist(src, i);
            if (d != -1 && nodeMaxDist < d) {
                nodeMaxDist = d;
            }
        }
        return nodeMaxDist;
    }


    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        //TODO: NEEDS SOME WORK !

        double min = Double.MAX_VALUE;
        List<NodeData> path = new ArrayList<>();

        for (int i = 0; i < cities.size(); i++) {
            List<NodeData> newPath = new ArrayList<>();
            newPath.add(cities.get(i));
            double sum = 0;

            for (int j = 0; j < cities.size(); j++) {
                if (j != i) {
                    List<NodeData> p = shortestPath(cities.get(cities.size() - 1).getKey(), cities.get(j).getKey());
                    // p.remove(p.size()-1);
                    newPath.addAll(p);
                }
            }

            if (pathCost(newPath) < min) {
                min = sum;
                path = newPath;
            }
        }


        return path;
    }

    @Override
    public boolean save(String file) {
        boolean state = true;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(new Gson().toJson(new DirectedWeightedGraphJson(this.graph)));
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
        this.graph = DirectedWeightedGraphImpl.load(file);
        if (this.graph != null)
            return true;
        return false;
    }


    public static void main(String[] args) {
        AlgorithmsImpl ag = new AlgorithmsImpl();
        System.out.println(ag.isConnected());

        //ag.save("./aa.txt");

        //ag.load("G1.json");
        //Iterator<EdgeData> iterator = ag.graph.edgeIter(1);

//        while (iterator.hasNext()) {
//            EdgeData d = iterator.next();
//            System.out.println(d.getSrc() + "--->" + d.getDest());
//        }
//
//
//        List<NodeData> list = new ArrayList<>();
//
//
//        int cnt = 0;
//        Iterator<NodeData> iterator2 = ag.graph.nodeIter();
//        while (iterator2.hasNext()) {
//            NodeData d = iterator2.next();
//            if (cnt < 9)
//                list.add(d);
//            cnt++;
//        }
//


//        System.out.println(ag.shortestPathDist(0, 5));
//        System.out.println(ag.isConnected());


        //ag.tsp(list);

        //System.out.println(ag.shortestPathDist(0,5));
//        System.out.println(ag.shortestPathDist(0,1));
//        System.out.println(ag.shortestPathDist(0,2));
//        System.out.println(ag.shortestPathDist(0, 3));
//        System.out.println(ag.shortestPathDist(0,4));
//        System.out.println(ag.shortestPathDist(0,5));
//        System.out.println(ag.shortestPathDist(0,6));


    }
}
