package assignment2;

import assignment2.api.DirectedWeightedGraph;
import assignment2.api.DirectedWeightedGraphAlgorithms;
import assignment2.api.EdgeData;
import assignment2.api.NodeData;
import assignment2.utils.DirectedGraphFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import assignment2.json_models.DirectedWeightedGraphJson;
import assignment2.models.NodeDataImpl;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;


public class AlgorithmsImpl implements DirectedWeightedGraphAlgorithms {
    private DirectedWeightedGraph graph;

    public AlgorithmsImpl() {
        this.graph = new DirectedWeightedGraphImpl();
    }

    public AlgorithmsImpl(String file) {
        load(Paths.get("").toAbsolutePath() + "/src/main/java/assignment2/data/" + file);
    }

    public AlgorithmsImpl(DirectedWeightedGraph graph) {
        this.graph = graph;
    }

    @Override
    public void init(DirectedWeightedGraph g) {
        this.graph = new DirectedWeightedGraphImpl(g);
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
    private DirectedWeightedGraph reverse(DirectedWeightedGraph graph) {
        DirectedWeightedGraph reverseGraph = new DirectedWeightedGraphImpl();
        Iterator<EdgeData> it = graph.edgeIter();
        while (it.hasNext()) {
            EdgeData edge = it.next();
            reverseGraph.addNode(graph.getNode(edge.getSrc()));
            reverseGraph.addNode(graph.getNode(edge.getDest()));
            reverseGraph.connect(edge.getDest(), edge.getSrc(), edge.getWeight());
        }

        Iterator<NodeData> iterator = graph.nodeIter();
        while (iterator.hasNext()) {
            NodeData nodeData = iterator.next();
            reverseGraph.addNode(new NodeDataImpl(nodeData));
        }
        return reverseGraph;
    }


    /**
     * DFS scan
     *
     * @param g         directed graph
     * @param node      the starting node
     * @param nodesList
     */
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
        DirectedWeightedGraph graph = new DirectedWeightedGraphImpl(this.graph);
        Iterator<NodeData> iterator = graph.nodeIter();
        List<NodeData> nodes = new ArrayList<>();
        while (iterator.hasNext()) {
            nodes.add(iterator.next());
        }
        return dfs(reverse(graph), dfs(graph, nodes, false), true).size() == graph.nodeSize();
    }


    /**
     * Gets a list of edges which the src is there source
     * Complexity : O(|V|)
     *
     * @param g   graph
     * @param src source node key
     * @return list of edges which the src is there source
     */
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
        return pathCost(path);
    }


    /**
     * Dijkstra algorithm O(|V|^2)
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return path for the shortest path between src and dest
     */
    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        if (graph.nodeSize() == 0 || graph.getNode(dest) == null || graph.getNode(src) == null)
            return null;

        //copy the graph
        DirectedWeightedGraph graph = copy();
        Iterator<NodeData> iterator = graph.nodeIter();

        //init nodes
        while (iterator.hasNext()) {
            NodeData d = iterator.next();
            d.setTag(NodeDataImpl.WHITE);
            if (d.getKey() == src)
                d.setWeight(0);
            else {
                d.setWeight(Double.MAX_VALUE);
            }
        }


        //run the algorithm
        HashMap<Integer, NodeData> dist = new HashMap<>();
        dist.put(src, graph.getNode(src));

        PriorityQueue<NodeData> nodeQ = new PriorityQueue<>(Comparator.comparingDouble(NodeData::getWeight));
        nodeQ.add(graph.getNode(src));

        while (!nodeQ.isEmpty()) {
            NodeData node = nodeQ.poll();
            for (EdgeData edge : getAdj(graph, node.getKey())) {
                NodeData connectedto = graph.getNode(edge.getDest());
                if (connectedto.getTag() == NodeDataImpl.WHITE) {
                    if (node.getWeight() + edge.getWeight() < connectedto.getWeight()) {
                        connectedto.setWeight(node.getWeight() + edge.getWeight());
                        dist.put(connectedto.getKey(), node);
                    }
                    nodeQ.add(connectedto);
                }
            }
            node.setTag(NodeDataImpl.BLACK);
        }


        if (graph.getNode(dest).getWeight() == Double.MAX_VALUE)
            return null;

        // get this graph nodes not the copied graph nodes
        List<NodeData> path = new ArrayList<>();
        NodeData node = graph.getNode(dest);
        while (node.getKey() != src) {
            path.add(0, this.graph.getNode(dist.get(node.getKey()).getKey()));
            node = dist.get(node.getKey());
        }
        path.add(this.graph.getNode(dest));

        return path;
    }


    @Override
    public NodeData center() {
        if (!isConnected() || graph.nodeSize() == 0)
            return null;
        Iterator<NodeData> t = graph.nodeIter();
        double currentMin = Integer.MAX_VALUE;
        int node = -1;

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


    /**
     * assumes the graph is strongly connected , get the max distance from the node src
     *
     * @param src the node source key
     * @return max distance from this node to any other
     */
    private double getMaxDistance(int src) {
        double nodeMaxDist = Integer.MIN_VALUE;

        Iterator<NodeData> it = graph.nodeIter();
        while (it.hasNext()) {
            double d = shortestPathDist(src, it.next().getKey());
            if (nodeMaxDist < d) {
                nodeMaxDist = d;
            }
        }

        return nodeMaxDist;
    }


    /**
     * @param path list of adjacent nodes
     * @return the sum of the edges weights
     */
    private double pathCost(List<NodeData> path) {
        double sum = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            sum += graph.getEdge(path.get(i).getKey(), path.get(i + 1).getKey()).getWeight();
        }
        return sum;
    }


    /**
     * This function gets a list of Nodes and returns all possible permutation
     * Complexity O(N!) when N is the number of nodes
     * <p>
     * this function was taken from StackOverFlow !
     *
     * @param list permutate list
     * @param k    start index
     * @param res  the result will be added to this list
     */
    void permute(List<NodeData> list, int k, List<List<NodeData>> res) {
        for (int i = k; i < list.size(); i++) {
            Collections.swap(list, i, k);
            permute(list, k + 1, res);
            Collections.swap(list, k, i);
        }
        if (k == list.size() - 1) {
            res.add(list);
        }
    }


    /**
     * @param list path list (the nodes not necessarily adjacent)
     * @return the full path between the nodes in the list
     */
    private List<NodeData> pathFrom(List<NodeData> list) {
        List<NodeData> path = new ArrayList<>();
        for (int i = 0; i < list.size() - 1; i++) {
            List<NodeData> p = shortestPath(list.get(i).getKey(), list.get(i + 1).getKey());
            if (p == null) {
                return null;
            }
            if (i > 0)
                p.remove(0);

            path.addAll(p);
        }
        return path;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        if (cities.isEmpty())
            return null;

        List<List<NodeData>> per = new ArrayList<>();
        permute(cities, 0, per);

        List<NodeData> currentPath = new ArrayList<>();
        double minPathCost = Double.MAX_VALUE;

        for (int i = 0; i < per.size() - 1; i++) {
            List<NodeData> current = pathFrom(per.get(i));
            if (current != null) {
                double cost = pathCost(current);
                if (cost < minPathCost) {
                    currentPath = current;
                    minPathCost = cost;
                }
            }
        }

        if (currentPath.isEmpty())
            return null;

        return currentPath;
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
        DirectedWeightedGraph g = DirectedWeightedGraphImpl.load(file);
        if (g != null) {
            this.graph = g;
            return true;
        }
        return false;
    }


}
