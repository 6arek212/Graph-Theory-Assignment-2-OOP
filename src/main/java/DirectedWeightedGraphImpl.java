import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;
import json_models.DirectedWeightedGraphJson;
import models.EdgeDataImpl;
import models.NodeDataImpl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.*;

public class DirectedWeightedGraphImpl implements DirectedWeightedGraph {

    private HashMap<Integer, NodeData> nodes;
    private HashMap<Integer, HashMap<Integer, EdgeData>> edges;

    private int numOfEdges;
    private int modeCounter;


    public static DirectedWeightedGraphImpl load(String filename) {
        try {
            DirectedWeightedGraphJson dgj = new Gson()
                    .fromJson(
                            new FileReader(Paths.get("").toAbsolutePath() + "/src/main/java/data/" + filename),
                            DirectedWeightedGraphJson.class
                    );
            return new DirectedWeightedGraphImpl(dgj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    // construct this object from json object
    public DirectedWeightedGraphImpl(DirectedWeightedGraphJson dgj) {
        this.nodes = new HashMap<>();
        numOfEdges = dgj.edges.size();
        modeCounter = 0;


        for (int i = 0; i < dgj.nodes.size(); i++) {
            NodeDataImpl node = new NodeDataImpl(dgj.nodes.get(i));
            this.nodes.put(node.getKey(), node);
        }

        this.edges = new HashMap<>();
        for (int i = 0; i < nodeSize(); i++) {
            this.edges.put(nodes.get(i).getKey(), new HashMap<>());
        }

        for (int i = 0; i < edgeSize(); i++) {
            EdgeDataImpl eg = new EdgeDataImpl(dgj.edges.get(i));
            this.edges.get(eg.getSrc()).put(eg.getDest(), eg);
        }
    }


    // deep copy constructor
    public DirectedWeightedGraphImpl(DirectedWeightedGraph g) {
        this.numOfEdges = g.edgeSize();
        this.modeCounter = g.getMC();
        this.nodes = new HashMap<>();

        //copy nodes
        Iterator<NodeData> it = g.nodeIter();
        while (it.hasNext()) {
            NodeData d = new NodeDataImpl(it.next());
            this.nodes.put(d.getKey(), d);
        }


        //copy edges
        this.edges = new HashMap<>();
        for (int i = 0; i < nodeSize(); i++) {
            this.edges.put(nodes.get(i).getKey(), new HashMap<>());
        }

        Iterator<EdgeData> itEg = g.edgeIter();
        while (itEg.hasNext()) {
            EdgeData ed = itEg.next();
            this.edges.get(ed.getSrc()).put(ed.getDest(), ed);
        }
    }


    private void changeHappened() {
        modeCounter++;
    }


    @Override
    public NodeData getNode(int key) {
        if (nodes.isEmpty())
            throw new RuntimeException("Node are empty");
        return nodes.get(key);
    }


    @Override
    public EdgeData getEdge(int src, int dest) {
        return this.edges.get(src).get(dest);
    }


    @Override
    public void addNode(NodeData n) {
        this.nodes.put(n.getKey(), n);
        this.edges.put(n.getKey(), new HashMap<>());
        changeHappened();
    }


    @Override
    public void connect(int src, int dest, double w) {
        this.edges.get(src).put(dest, new EdgeDataImpl(src, dest, w));
        numOfEdges++;
        changeHappened();
    }


    @Override
    public Iterator<NodeData> nodeIter() {
        return new Iterator<NodeData>() {
            Iterator<NodeData> it = nodes.values().iterator();
            private int mc = modeCounter;

            @Override
            public boolean hasNext() {
                if (mc != modeCounter)
                    throw new RuntimeException("object has been changed");
                return it.hasNext();
            }

            @Override
            public NodeData next() {
                if (mc != modeCounter)
                    throw new RuntimeException("object has been changed");
                return it.next();
            }
        };
    }

    private List<EdgeData> getEdges() {
        List<EdgeData> edgeData = new ArrayList<>();
        for (HashMap<Integer, EdgeData> value : edges.values()) {
            edgeData.addAll(value.values());
        }
        return edgeData;
    }


    @Override
    public Iterator<EdgeData> edgeIter() {
        return new Iterator<EdgeData>() {
            private int mc = modeCounter;
            private int i = 0;
            private List<EdgeData> ed = getEdges();


            @Override
            public boolean hasNext() {
                if (mc != modeCounter)
                    throw new RuntimeException("object has been changed");
                return i < ed.size();
            }

            @Override
            public EdgeData next() {
                if (mc != modeCounter)
                    throw new RuntimeException("object has been changed");
                return ed.get(i++);
            }
        };
    }




    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        if (!nodes.containsKey(node_id))
            throw new RuntimeException("node id not exists");
        return new Iterator<EdgeData>() {
            int mc = modeCounter;
            Iterator<EdgeData> it = edges.get(node_id).values().iterator();

            @Override
            public boolean hasNext() {
                if (mc != modeCounter)
                    throw new RuntimeException("object has been changed");
                return it.hasNext();
            }

            @Override
            public EdgeData next() {
                if (mc != modeCounter)
                    throw new RuntimeException("object has been changed");
                return it.next();
            }
        };
    }


    @Override
    public NodeData removeNode(int key) {
        numOfEdges -= this.edges.get(key).size();
        this.edges.remove(key);

        for (HashMap<Integer, EdgeData> value : edges.values()) {
            value.remove(key);
            numOfEdges -= 1;
        }
        changeHappened();
        return nodes.remove(key);
    }


    @Override
    public EdgeData removeEdge(int src, int dest) {
        EdgeData ed = null;
        if (this.edges.get(src) != null)
            ed = this.edges.get(src).remove(dest);

        numOfEdges--;
        changeHappened();
        return ed;
    }


    @Override
    public int nodeSize() {
        return nodes.size();
    }


    @Override
    public int edgeSize() {
        return numOfEdges;
    }


    @Override
    public int getMC() {
        return modeCounter;
    }


    @Override
    public String toString() {
        return "DirectedWeightedGraphImpl2{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                ", numOfEdges=" + numOfEdges +
                ", modeCounter=" + modeCounter +
                '}';
    }
}
