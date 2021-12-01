package assignment2;

import assignment2.api.DirectedWeightedGraph;
import assignment2.api.EdgeData;
import assignment2.api.NodeData;
import com.google.gson.Gson;
import assignment2.json_models.DirectedWeightedGraphJson;
import assignment2.models.EdgeDataImpl;
import assignment2.models.NodeDataImpl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.*;

public class DirectedWeightedGraphImpl implements DirectedWeightedGraph {

    private HashMap<Integer, NodeData> nodes;
    private HashMap<Integer, HashMap<Integer, EdgeData>> edges;
    private HashMap<Integer, HashMap<Integer, EdgeData>> edgesIn;

    private int numOfEdges;
    private int modeCounter;


    public DirectedWeightedGraphImpl() {
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        this.edgesIn = new HashMap<>();
        this.numOfEdges = 0;
        this.modeCounter = 0;
    }

    public static DirectedWeightedGraph load(String filename) {
        try {
            DirectedWeightedGraphJson dgj = new Gson()
                    .fromJson(
                            new FileReader(Paths.get("").toAbsolutePath() + "/src/main/java/assignment2/data/" + filename),
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

        this.edgesIn = new HashMap<>();
        this.edges = new HashMap<>();
        for (int i = 0; i < nodeSize(); i++) {
            this.edges.put(nodes.get(i).getKey(), new HashMap<>());
            this.edgesIn.put(nodes.get(i).getKey(), new HashMap<>());
        }

        for (int i = 0; i < edgeSize(); i++) {
            EdgeDataImpl eg = new EdgeDataImpl(dgj.edges.get(i));
            this.edges.get(eg.getSrc()).put(eg.getDest(), eg);
            this.edgesIn.get(eg.getDest()).put(eg.getSrc(), eg);
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
        this.edgesIn = new HashMap<>();

        for (int i = 0; i < nodeSize(); i++) {
            this.edges.put(nodes.get(i).getKey(), new HashMap<>());
            this.edgesIn.put(nodes.get(i).getKey(), new HashMap<>());
        }

        Iterator<EdgeData> itEg = g.edgeIter();
        while (itEg.hasNext()) {
            EdgeData ed = new EdgeDataImpl(itEg.next());
            this.edges.get(ed.getSrc()).put(ed.getDest(), ed);
            this.edgesIn.get(ed.getDest()).put(ed.getSrc(), ed);
        }
    }


    private void changeHappened() {
        modeCounter++;
    }


    @Override
    public NodeData getNode(int key) {
        return nodes.get(key);
    }


    @Override
    public EdgeData getEdge(int src, int dest) {
        return this.edges.get(src).get(dest);
    }


    @Override
    public void addNode(NodeData n) {
        if (this.nodes.containsKey(n.getKey()))
            return;

        this.nodes.put(n.getKey(), n);
        this.edges.put(n.getKey(), new HashMap<>());
        this.edgesIn.put(n.getKey(), new HashMap<>());
        changeHappened();
    }


    @Override
    public void connect(int src, int dest, double w) {
        EdgeData ed = new EdgeDataImpl(src, dest, w);
        this.edges.get(src).put(dest, ed);
        this.edges.get(dest).put(src, ed);
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

            @Override
            public void remove() {
                it.remove();
            }
        };
    }


    @Override
    public NodeData removeNode(int key) {
        if (!this.nodes.containsKey(key))
            return null;
        numOfEdges -= (this.edges.get(key).size() + this.edgesIn.get(key).size());
        Iterator<EdgeData> it = edgesIn.get(key).values().iterator();

        while (it.hasNext()) {
            EdgeData ed = it.next();
            it.remove();
            removeEdge(ed.getSrc(), ed.getDest());
        }

        changeHappened();
        this.edges.remove(key);
        this.edgesIn.remove(key);
        return nodes.remove(key);
    }


    @Override
    public EdgeData removeEdge(int src, int dest) {
        if (!this.edges.containsKey(src) || !this.edgesIn.containsKey(dest))
            return null;
        EdgeData ed = this.edges.get(src).remove(dest);
        this.edgesIn.get(dest).remove(src);
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
