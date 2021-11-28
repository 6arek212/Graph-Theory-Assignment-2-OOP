package json_impl;

import api.NodeData;
import com.google.gson.annotations.SerializedName;

public class jsonDataNode {
    @SerializedName("id")
    private int key;

    @SerializedName("pos")
    private String locationString;

    public jsonDataNode(NodeData node) {
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
