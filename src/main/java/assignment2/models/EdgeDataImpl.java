package assignment2.models;

import assignment2.api.EdgeData;
import assignment2.json_models.EdgeDataJson;

public class EdgeDataImpl implements EdgeData {

    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;


    public EdgeDataImpl(EdgeDataJson ed) {
        this.src = ed.getSrc();
        this.dest = ed.getDest();
        this.weight = ed.getWeight();
        this.info = null;
    }


    public EdgeDataImpl(int src, int dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.info = null;
    }

    public EdgeDataImpl(EdgeData e) {
        this.src = e.getSrc();
        this.dest = e.getDest();
        this.weight = e.getWeight();
        this.info = e.getInfo();
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
        return this.weight;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    @Override
    public String toString() {
        return "EdgeDataImpl{" +
                "src=" + src +
                ", dest=" + dest +
                ", weight=" + weight +
                ", info='" + info + '\'' +
                ", tag=" + tag +
                '}';
    }
}
