package assignment2.json_models;

import assignment2.api.NodeData;
import com.google.gson.annotations.SerializedName;

public class NodeDataJson {
    @SerializedName("id")
    private int key;

    @SerializedName("pos")
    private String locationString;

    public NodeDataJson(NodeData node) {
        this.key = node.getKey();
        this.locationString = node.getLocation().x() + "," + node.getLocation().y() + "," + node.getLocation().z();
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getLocationString() {
        return locationString;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }
}
