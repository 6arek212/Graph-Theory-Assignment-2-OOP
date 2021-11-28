package implementation;

import api.EdgeData;
import json_impl.jsonEdgeData;

public class EdgeDataImpl implements EdgeData {

    private int src;
    private double w;
    private int dest;

    public EdgeDataImpl(int src, int dest, double w) {
        this.src = src;
        this.w = w;
        this.dest = dest;

    }

    //deep copy for edge
    public EdgeDataImpl(EdgeData other) {
        this.dest = other.getDest();
        this.src = other.getSrc();
        this.w = other.getWeight();


    }

    public EdgeDataImpl(jsonEdgeData ed) {
        this.src = ed.getSrc();
        this.dest = ed.getDest();
        this.w = ed.getWeight();

    }

    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.w;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void setInfo(String s) {

    }

    //no need
    @Override
    public int getTag() {
        return 0;
    }

    //no need
    @Override
    public void setTag(int t) {

    }
    @Override
    public String toString() {
        return "EdgeDataImpl{" +
                "src=" + src +
                ", dest=" + dest +
                ", weight=" + w +
                '}';
    }
}
