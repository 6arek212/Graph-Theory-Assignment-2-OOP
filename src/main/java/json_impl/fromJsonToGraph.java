package json_impl;

import api.DirectedWeightedGraph;
import api.EdgeData;
import com.google.gson.annotations.SerializedName;
import api.NodeData;

import java.util.*;
import java.util.ArrayList;

public class fromJsonToGraph {
    @SerializedName("Nodes")
    public ArrayList<jsonDataNode> nodes;
    @SerializedName("Edges")
    public ArrayList<jsonEdgeData> edges;


    public fromJsonToGraph(DirectedWeightedGraph g) {

        this.nodes = new ArrayList<>();
        Iterator<NodeData> nodesIt = g.nodeIter();
        while (nodesIt.hasNext()) {
            this.nodes.add(new jsonDataNode(nodesIt.next()));
        }

        this.edges = new ArrayList<>();
        Iterator<EdgeData> edgesIt = g.edgeIter();
        while (edgesIt.hasNext()) {
            this.edges.add(new jsonEdgeData(edgesIt.next()));
        }


    }

    @Override
    public String toString() {
        return "DirectedWeightedGraphJson{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }





}
