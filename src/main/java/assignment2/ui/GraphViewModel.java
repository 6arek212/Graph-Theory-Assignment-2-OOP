package assignment2.ui;

import assignment2.DirectedWeightedGraphImpl;
import assignment2.api.DirectedWeightedGraphAlgorithms;
import assignment2.api.EdgeData;
import assignment2.api.GeoLocation;
import assignment2.api.NodeData;
import assignment2.models.NodeDataImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Controller class that handles the view data
 */
public class GraphViewModel {
    private final static int N_THREADS = 1;

    private DirectedWeightedGraphAlgorithms algo;
    private List<NodeData> nodes;
    private List<EdgeData> edges;
    private ActionListener actionListener;
    private ExecutorService pool;

    public GraphViewModel(DirectedWeightedGraphAlgorithms graphAlg, ActionListener actionListener) {
        this.actionListener = actionListener;
        this.algo = graphAlg;
        pool = Executors.newFixedThreadPool(N_THREADS);
        initNodeEdges();
        setTextUi();
    }


    /**
     * Fires the View events on a background thread
     * <p>
     * Only one task will be executed at a time
     *
     * @param event
     */
    public void onTriggerEvent(GraphEvents event) {
        pool.execute(
                () -> {
                    initTags();
                    actionListener.actionEvent(new UIEvents.UpdateUi());
                    if (event instanceof GraphEvents.LoadGraph) {
                        if (algo.load(((GraphEvents.LoadGraph) event).filename))
                            initNodeEdges();
                        else
                            actionListener.actionEvent(new UIEvents.ShowMessage("Error file was not found"));
                    }

                    if (event instanceof GraphEvents.SaveGraph) {
                        algo.save(((GraphEvents.SaveGraph) event).filename);
                    }

                    if (event instanceof GraphEvents.ShortestPath) {
                        shortestPath(((GraphEvents.ShortestPath) event).src, ((GraphEvents.ShortestPath) event).dest);
                    }

                    if (event instanceof GraphEvents.ShortestPathDist) {
                        shortestPathDist(((GraphEvents.ShortestPathDist) event).src, ((GraphEvents.ShortestPathDist) event).dest);
                    }

                    if (event instanceof GraphEvents.IsConnected) {
                        isConnected();
                    }

                    if (event instanceof GraphEvents.Center) {
                        center();
                    }

                    if (event instanceof GraphEvents.TSP) {
                        tsp(((GraphEvents.TSP) event).getCities());
                    }

                    if (event instanceof GraphEvents.RemoveNode) {
                        removeNode(((GraphEvents.RemoveNode) event).getKey());
                    }

                    if (event instanceof GraphEvents.AddNode) {
                        addNode(((GraphEvents.AddNode) event).getKey(), ((GraphEvents.AddNode) event).getLocation());
                    }

                    if (event instanceof GraphEvents.DeleteEdge) {
                        deleteEdge(((GraphEvents.DeleteEdge) event).getSrc(), ((GraphEvents.DeleteEdge) event).getDest());
                    }

                    if (event instanceof GraphEvents.AddEdge) {
                        addEdge(((GraphEvents.AddEdge) event).getSrc(), ((GraphEvents.AddEdge) event).getDest(), ((GraphEvents.AddEdge) event).getW());
                    }

                    if (event instanceof GraphEvents.NewGraph) {
                        newGraph();
                    }

                    setTextUi();
                    actionListener.actionEvent(new UIEvents.UpdateUi());
                }
        );
    }

    private void setTextUi() {
        actionListener.actionEvent(new UIEvents.Labels(nodes.size(), edges.size()));
    }

    private void newGraph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.algo.init(new DirectedWeightedGraphImpl());
    }

    private void addEdge(int src, int dest, double w) {
        algo.getGraph().connect(src, dest, w);
        initNodeEdges();
    }

    private void deleteEdge(int src, int dest) {
        if (algo.getGraph().removeEdge(src, dest) != null) {
            initNodeEdges();
        } else {
            actionListener.actionEvent(new UIEvents.ShowMessage("Edge not found"));
        }
    }


    private void addNode(int key, GeoLocation g) {
        if (algo.getGraph().getNode(key) != null) {
            actionListener.actionEvent(new UIEvents.ShowMessage("Node already exists !"));
            return;
        }
        NodeData node = new NodeDataImpl(key, g);
        algo.getGraph().addNode(node);
        this.nodes.add(node);
    }

    private void removeNode(int key) {
        algo.getGraph().removeNode(key);
        initNodeEdges();
    }

    private void tsp(List<Integer> cities) {
        List<NodeData> citiesAsNode = new ArrayList<>();

        for (int i = 0; i < cities.size(); i++) {
            NodeData node = algo.getGraph().getNode(cities.get(i));
            if (node == null) {
                actionListener.actionEvent(new UIEvents.ShowMessage(cities.get(i) + " is not a valid node key !"));
                return;
            }
            citiesAsNode.add(node);
        }
        List<NodeData> path = algo.tsp(citiesAsNode);
        if (path == null) {
            actionListener.actionEvent(new UIEvents.ShowMessage("There is no path as requested"));
            return;
        }
        this.nodes = path;
        this.edges = new ArrayList<>();
        for (int i = 0; i < this.nodes.size() - 1; i++) {
            this.edges.add(algo.getGraph().getEdge(this.nodes.get(i).getKey(), this.nodes.get(i + 1).getKey()));
        }
    }

    private void center() {
        NodeData centerNode = algo.center();
        if (centerNode != null) {
            centerNode.setTag(NodeDataImpl.BLACK);
            actionListener.actionEvent(new UIEvents.UpdateUi());
            return;
        }
        actionListener.actionEvent(new UIEvents.ShowMessage("The graph is not strongly connected !"));


    }


    private void isConnected() {
        String msg;

        if (algo.isConnected())
            msg = "The graph is connected";
        else
            msg = "The graph is not connected";

        actionListener.actionEvent(new UIEvents.ShowMessage(msg));
    }


    private void initNodeEdges() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();

        Iterator<EdgeData> it = this.algo.getGraph().edgeIter();
        while (it.hasNext()) {
            this.edges.add(it.next());
        }
        Iterator<NodeData> it1 = this.algo.getGraph().nodeIter();
        while (it1.hasNext()) {
            this.nodes.add(it1.next());
        }
    }


    private void shortestPath(int src, int dest) {
        List<NodeData> path = algo.shortestPath(src, dest);

        if (path == null) {
            actionListener.actionEvent(new UIEvents.ShowMessage("There is no path from " + src + " to " + dest));
            return;
        }

        for (NodeData n : path) {
            n.setTag(NodeDataImpl.BLACK);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            actionListener.actionEvent(new UIEvents.UpdateUi());
        }
        shortestPathDist(src, dest);
    }

    private void initTags() {
        Iterator<NodeData> it = algo.getGraph().nodeIter();
        while (it.hasNext()) {
            it.next().setTag(NodeDataImpl.WHITE);
        }
    }

    private void shortestPathDist(int src, int dest) {
        double dist = algo.shortestPathDist(src, dest);
        actionListener.actionEvent(new UIEvents.ShowMessage("The shortest path between " + src + " and " + dest + " : " + dist));
    }

    public void clear() {
        pool.shutdown();
    }

    public List<NodeData> getNodes() {
        return this.nodes;
    }

    public List<EdgeData> getEdges() {
        return this.edges;
    }

    public NodeData getNode(int key) {
        return algo.getGraph().getNode(key);
    }

    public DirectedWeightedGraphAlgorithms getAlgo() {
        return algo;
    }
}
