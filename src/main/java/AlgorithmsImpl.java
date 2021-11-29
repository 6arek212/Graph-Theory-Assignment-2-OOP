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


    //as default load G1.json
    public AlgorithmsImpl(String file) {
        load(file);
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

    /**
     * get the transpose of the graph
     * complexity  O(V+E)
     */
    private DirectedWeightedGraph reverse() {
        DirectedWeightedGraph reverseGraph = new DirectedWeightedGraphImpl();
        Iterator<EdgeData> it = graph.edgeIter();
        while (it.hasNext()) {
            EdgeData edge = it.next();
            if (reverseGraph.getNode(edge.getSrc()) == null)
                reverseGraph.addNode(graph.getNode(edge.getSrc()));
            if (reverseGraph.getNode(edge.getDest()) == null)
                reverseGraph.addNode(graph.getNode(edge.getDest()));
            reverseGraph.connect(edge.getDest(), edge.getSrc(), edge.getWeight());
        }

        Iterator<NodeData> iterator = graph.nodeIter();
        while (iterator.hasNext()) {
            NodeData nodeData = iterator.next();
            if (reverseGraph.getNode(nodeData.getKey()) == null) {
                reverseGraph.addNode(nodeData);
            }
        }
        return reverseGraph;
    }


    private void dfsVisit(DirectedWeightedGraph g, NodeData node, List<NodeData> nodesList) {
        List<EdgeData> adj;
        adj = getAdj(g, node.getKey());
        node.setTag(NodeDataImpl.GRAY);

        for (EdgeData ed : adj) {
            NodeData nd = g.getNode(ed.getDest());
            if (nd.getTag() == NodeDataImpl.WHITE) {
                dfsVisit(g, nd, nodesList);
            }
        }
        nodesList.add(0, node);
        node.setTag(NodeDataImpl.BLACK);
    }

    private List<NodeData> dfs(DirectedWeightedGraph g, List<NodeData> nodesList, boolean reversed) {
        for (NodeData d : nodesList) {
            d.setTag(NodeDataImpl.WHITE);
        }
        List<NodeData> p = new ArrayList<>();
        for (NodeData d : nodesList) {
            if (d.getTag() == NodeDataImpl.WHITE)
                dfsVisit(g, d, p);
            if (reversed)
                return p;
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
        return dfs(reverse(), dfs(graph, nodes, false), true).size() == graph.nodeSize();
    }


    private List<EdgeData> getAdj(DirectedWeightedGraph g, int src) {
        Iterator<EdgeData> iterator = g.edgeIter(src);
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
        return path.get(path.size() - 1).getWeight();
    }


    /**
     * Dijkstra algorithm O(|V|^2)
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        Iterator<NodeData> iterator = graph.nodeIter();

        while (iterator.hasNext()) {
            NodeData d = iterator.next();
            d.setTag(NodeDataImpl.WHITE);
            if (d.getKey() == src)
                d.setWeight(0);
            else {
                d.setWeight(Double.MAX_VALUE);
            }
        }

        NodeData[] dist = new NodeData[graph.nodeSize()];
        dist[src] = graph.getNode(src);

        PriorityQueue<NodeData> nodeQ = new PriorityQueue<>((NodeData o1, NodeData o2) -> {
            if (o1.getWeight() < o2.getWeight())
                return -1;
            if (o1.getWeight() > o2.getWeight())
                return 1;
            return 0;
        });
        nodeQ.add(graph.getNode(src));

        while (!nodeQ.isEmpty()) {
            NodeData node = nodeQ.poll();

            for (EdgeData edge : getAdj(graph, node.getKey())) {
                NodeData connectedto = graph.getNode(edge.getDest());
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


        if (graph.getNode(dest).getWeight() == Double.MAX_VALUE)
            return null;

        List<NodeData> path = new ArrayList<>();
        NodeData node = graph.getNode(dest);
        while (node.getKey() != src) {
            path.add(0, dist[node.getKey()]);
            node = dist[node.getKey()];
        }
        path.add(graph.getNode(dest));

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


    //    private double pathCost(List<NodeData> path) {
//        double sum = 0;
//        for (int i = 0; i < path.size() - 1; i++) {
//            sum += graph.getEdge(path.get(i).getKey(), path.get(i + 1).getKey()).getWeight();
//        }
//
//        return sum;
//    }
//
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
        AlgorithmsImpl ag = new AlgorithmsImpl("G1.json");
        System.out.println(ag.isConnected());
    }
}
