package implementation;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import json_impl.fromJsonToGraph;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.List;

public class DirectedWeightedGraphImpl implements DirectedWeightedGraph {

    private HashMap<Integer, NodeData> Nodes = new HashMap<Integer, NodeData>();
    private HashMap<Integer, HashMap<Integer, EdgeData>> Edges = new HashMap<Integer, HashMap<Integer, EdgeData>>();

    //count the operations on graph object
    private int ModeCounter;
    private int EdgeCounter;

    public DirectedWeightedGraphImpl() {

    }


    @Override
    public NodeData getNode(int key) {

        return Nodes.get(key);
    }

    @Override
    public EdgeData getEdge(int src, int dest) {

        if (!(Edges.size() != 0 && Edges.containsKey(src) && Edges.get(src).containsKey(dest))) {
            return null;


        }

        return Edges.get(src).get(dest);

    }

    @Override
    public void addNode(NodeData n) {
        if (n == null)
            return;

     if(getNode(n.getKey()) == null) {
         Nodes.put(n.getKey(), n);

         HashMap<Integer, EdgeData> new_EdgeNode = new HashMap<>();
         Edges.put(n.getKey(), new_EdgeNode);
         ModeCounter++;
     }

    }

    @Override
    public void connect(int src, int dest, double w) {
        if (src == dest || getEdge(src, dest) != null)
            return;

        if (getEdge(src, dest) == null) {
            EdgeData srcToDestEdge = new EdgeDataImpl(src, dest, w);
            Edges.get(src).put(dest, srcToDestEdge);
            ModeCounter++;
            EdgeCounter++;

        }

    }
//
//    @Override
//    public Iterator<NodeData> nodeIter() {
//        Iterator<NodeData> iterNodes = this.Nodes.values().iterator();
//        return iterNodes;
//
//    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return new Iterator<NodeData>() {
            Iterator<NodeData> it = Nodes.values().iterator();
            private int mc = ModeCounter;

            @Override
            public boolean hasNext() {
                if (mc != ModeCounter)
                    throw new RuntimeException("object has been changed");
                return it.hasNext();
            }

            @Override
            public NodeData next() {
                if (mc != ModeCounter)
                    throw new RuntimeException("object has been changed");
                return it.next();
            }
        };
    }

    public static DirectedWeightedGraphImpl load(String filename) {
        try {
            fromJsonToGraph dgj = new Gson()
                    .fromJson(new FileReader(Paths.get("").toAbsolutePath() + "/src/main/java/data/" + filename),
                            fromJsonToGraph.class);
            return new DirectedWeightedGraphImpl(dgj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DirectedWeightedGraphImpl(fromJsonToGraph dgj) {
        this.Nodes = new HashMap<>();
        EdgeCounter = dgj.edges.size();
        ModeCounter = 0;


        for (int i = 0; i < dgj.nodes.size(); i++) {
            NodeDataImpl node = new NodeDataImpl(dgj.nodes.get(i));
            this.Nodes.put(node.getKey(), node);
        }

        this.Edges = new HashMap<>();
        for (int i = 0; i < nodeSize(); i++) {
            this.Edges.put(Nodes.get(i).getKey(), new HashMap<>());
        }

        for (int i = 0; i < edgeSize(); i++) {
            EdgeDataImpl eg = new EdgeDataImpl(dgj.edges.get(i));
            this.Edges.get(eg.getSrc()).put(eg.getDest(), eg);
        }
    }


    //    @Override
//    public Iterator<EdgeData> edgeIter() {
//        ArrayList<EdgeData> allEdges = new ArrayList<EdgeData>();
//        for (int i = 0; i < this.Edges.size(); i++) {
//            allEdges.addAll(this.Edges.get(i).values());
//
//        }
//        Iterator<EdgeData> iterEdges = allEdges.iterator();
//
//
//        return iterEdges;
//    }
    private List<EdgeData> getEdges() {
        List<EdgeData> edgeData = new ArrayList<>();
        for (HashMap<Integer, EdgeData> value : Edges.values()) {
            edgeData.addAll(value.values());
        }
        return edgeData;
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return new Iterator<EdgeData>() {
            private int mc = ModeCounter;
            private int i = 0;
            private List<EdgeData> ed = getEdges();


            @Override
            public boolean hasNext() {
                if (mc != ModeCounter)
                    throw new RuntimeException("object has been changed");
                return i < ed.size();
            }

            @Override
            public EdgeData next() {
                if (mc != ModeCounter)
                    throw new RuntimeException("object has been changed");
                return ed.get(i++);
            }
        };
    }

//    @Override
//    public Iterator<EdgeData> edgeIter(int node_id) {
//        if (Edges.size() == 0) {
//            return null;
//        }
//
//
//        Iterator<EdgeData> iterEdgeId = this.Edges.get(node_id).values().iterator();
//        return iterEdgeId;
//    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        if (!Nodes.containsKey(node_id))
            throw new RuntimeException("node id not exists");
        return new Iterator<EdgeData>() {
            int mc = ModeCounter;
            Iterator<EdgeData> it = Edges.get(node_id).values().iterator();

            @Override
            public boolean hasNext() {
                if (mc != ModeCounter)
                    throw new RuntimeException("object has been changed");
                return it.hasNext();
            }

            @Override
            public EdgeData next() {
                if (mc != ModeCounter)
                    throw new RuntimeException("object has been changed");
                return it.next();
            }
        };
    }

    @Override
    public NodeData removeNode(int key) {
        if (Nodes.get(key) == null)
            return null;

        NodeData removedNode = Nodes.get(key);

        Nodes.remove(key);
        Edges.remove(key);
        EdgeCounter--;
        ModeCounter++;


        return removedNode;


    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        if (Edges.size() == 0 || getEdge(src, dest) == null) {
            return null;
        }
        EdgeData removedEdge = Edges.get(src).get(dest);
        Edges.get(src).remove(dest);
        EdgeCounter--;
        ModeCounter++;


        return removedEdge;
    }

    @Override
    public int nodeSize() {
        return Nodes.size();

    }

    @Override
    public int edgeSize() {

        return this.EdgeCounter;
    }

    @Override
    public int getMC() {

        return this.ModeCounter;
    }

    @Override
    public String toString() {
        return "DirectedWeightedGraphImpl2 {" +
                "nodes=" + Nodes +
                ", edges=" + Edges +
                ", numOfEdges=" + EdgeCounter +
                ", modeCounter=" + ModeCounter +
                '}';
    }

}
