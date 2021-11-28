package json_models;

import api.EdgeData;
import com.google.gson.annotations.SerializedName;

public class EdgeDataJson {
    @SerializedName("src")
    private int src;
    @SerializedName("dest")
    private int dest;
    @SerializedName("w")
    private double weight;

    public EdgeDataJson(EdgeData ed) {
        this.src = ed.getSrc();
        this.dest = ed.getDest();
        this.weight = ed.getWeight();
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
