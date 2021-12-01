package assignment2.ui;


import assignment2.api.GeoLocation;

import java.util.List;

public class GraphEvents {

    public static class NewGraph extends GraphEvents {

    }


    public static class AddEdge extends GraphEvents {
        private int src, dest;
        private double w;


        public AddEdge(int src, int dest, double w) {
            this.src = src;
            this.dest = dest;
            this.w = w;
        }

        public double getW() {
            return w;
        }

        public int getDest() {
            return dest;
        }

        public int getSrc() {
            return src;
        }
    }

    public static class DeleteEdge extends GraphEvents {
        private int src, dest;

        public DeleteEdge(int src, int dest) {
            this.src = src;
            this.dest = dest;
        }

        public int getDest() {
            return dest;
        }

        public int getSrc() {
            return src;
        }
    }


    public static class AddNode extends GraphEvents {
        private GeoLocation location;
        private int key;

        public AddNode(GeoLocation location, int key) {
            this.location = location;
            this.key = key;
        }

        public GeoLocation getLocation() {
            return location;
        }

        public int getKey() {
            return key;
        }
    }


    public static class RemoveNode extends GraphEvents {
        private int key;

        public RemoveNode(int key) {
            this.key = key;
        }

        public int getKey() {
            return key;
        }
    }

    public static class TSP extends GraphEvents {
        private List<Integer> cities;

        public TSP(List<Integer> cities) {
            this.cities = cities;
        }

        public List<Integer> getCities() {
            return cities;
        }
    }


    public static class Center extends GraphEvents {
    }

    public static class IsConnected extends GraphEvents {
    }

    public static class ShortestPath extends GraphEvents {
        int src;
        int dest;

        public ShortestPath(int src, int dest) {
            this.src = src;
            this.dest = dest;
        }
    }


    public static class ShortestPathDist extends GraphEvents {
        int src;
        int dest;

        public ShortestPathDist(int src, int dest) {
            this.src = src;
            this.dest = dest;
        }
    }


    public static class LoadGraph extends GraphEvents {
        String filename;

        public LoadGraph(String filename) {
            this.filename = filename;
        }
    }

    public static class SaveGraph extends GraphEvents {
        String filename;

        public SaveGraph(String filename) {
            this.filename = filename;
        }
    }
}
