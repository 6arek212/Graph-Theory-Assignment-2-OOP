package assignment2.json_models;

import assignment2.api.DirectedWeightedGraph;
import assignment2.api.EdgeData;
import assignment2.api.NodeData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Iterator;


public class DirectedWeightedGraphJson {
    @SerializedName("Nodes")
    public ArrayList<NodeDataJson> nodes;

    @SerializedName("Edges")
    public ArrayList<EdgeDataJson> edges;

    public DirectedWeightedGraphJson(DirectedWeightedGraph dg) {
        this.nodes = new ArrayList<>();
        Iterator<NodeData> nodesIt = dg.nodeIter();
        while (nodesIt.hasNext()) {
            this.nodes.add(new NodeDataJson(nodesIt.next()));
        }

        this.edges = new ArrayList<>();
        Iterator<EdgeData> edgesIt = dg.edgeIter();
        while (edgesIt.hasNext()) {
            this.edges.add(new EdgeDataJson(edgesIt.next()));
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
