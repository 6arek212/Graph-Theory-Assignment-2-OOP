package assignment2.models;

import assignment2.api.GeoLocation;
import assignment2.api.NodeData;
import assignment2.json_models.NodeDataJson;


public class NodeDataImpl implements NodeData {
    public final static int WHITE = 1;
    public final static int GRAY = 2;
    public final static int BLACK = 3;

    private int key;
    private int tag;
    private GeoLocation location;
    private double weight;
    private String info;

    public NodeDataImpl(NodeDataJson nd) {
        this.key = nd.getKey();
        this.tag = NodeDataImpl.WHITE;
        this.location = new GeoLocationImpl(nd.getLocationString());
        this.info = null;
    }

    public NodeDataImpl(NodeData node) {
        this.key = node.getKey();
        this.tag = node.getTag();
        this.location = new GeoLocationImpl(node.getLocation());
        this.info = node.getInfo();
    }

    public NodeDataImpl(int key, GeoLocation g) {
        this.key = key;
        this.tag = NodeDataImpl.WHITE;
        this.location = g;
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public GeoLocation getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.location = p;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
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
        return "NodeDataImpl{" +
                "key=" + key +
                ", tag=" + tag +
                ", location=" + location +
                ", weight=" + weight +
                ", info='" + info + '\'' +
                '}';
    }
}
