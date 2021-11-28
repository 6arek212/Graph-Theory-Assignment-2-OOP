package implementation;

import api.GeoLocation;
import api.NodeData;
import json_impl.jsonDataNode;
public class NodeDataImpl implements NodeData {
    private int key;
    private double weight;
    static final int WHITE = 1, GRAY = -1, BLACK = 0;
    private int tag;
    private GeoLocation GeoLoc;
    private String info;

    public NodeDataImpl(int key, GeoLocation GeoLoc) {
        this.key = key;
        this.weight = 0;
        this.tag = WHITE;
        this.GeoLoc = GeoLoc;
        this.info = "";
    }

    public NodeDataImpl( jsonDataNode nd ) {
        this.key = nd.getKey();
        this.tag = NodeDataImpl.WHITE;
        this.GeoLoc = new GeoLocationImpl(nd.getLocationString());

        this.info = "";
    }
    public NodeDataImpl(NodeData node) {
        this.key = node.getKey();
        this.tag = node.getTag();

        this.info = node.getInfo();
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public GeoLocation getLocation() {
        return this.GeoLoc;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.GeoLoc = p;
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

    public  String toString(){

        return "key: " +this.key;
    }
}