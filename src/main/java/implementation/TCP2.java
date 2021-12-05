package implementation;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;

import java.util.ArrayList;
import java.util.List;

class TSP2 {
    // Distance lookup table
    private static final double[][] distances = {{0, 129, 119, 43.6, 98.6, 98.6, 86.3, 52.2, 85.3, 44.5},
            {129, 0, 88.3, 149, 152, 57.4, 55.4, 141, 93.3, 86.3},
            {119, 88.3, 0, 97.4, 71.6, 72.6, 42.5, 71.6, 35.5, 92.1},
            {43.6, 149, 97.4, 0, 54, 119, 107, 28, 64.2, 60.7},
            {98.6, 152, 71.6, 54, 0, 138, 85.2, 39.9, 48.6, 90.7},
            {98.6, 57.4, 72.6, 119, 138, 0, 34.9, 111, 77.1, 56.3},
            {86.3, 55.4, 42.5, 107, 85.2, 34.9, 0, 80.9, 37.9, 44.7},
            {52.2, 141, 71.6, 28, 39.9, 111, 80.9, 0, 38.8, 52.4},
            {85.3, 93.3, 35.5, 64.2, 48.6, 77.1, 37.9, 38.8, 0, 47.4},
            {44.5, 86.3, 92.1, 60.7, 90.7, 56.3, 44.7, 52.4, 47.4, 0},};


    private List<NodeData> cities;


    private List<Route> BFRoutePerms = new ArrayList<Route>();
    private double[][] dist;
    private double BFcheapestCost = Double.MAX_VALUE;
    private Route BFcheapestRoute;


    private List<Route> BaBRoutePerms = new ArrayList<Route>();
    private double BaBcheapestCost = Double.MAX_VALUE;
    private Route BaBcheapestRoute;

    public TSP2(double[][] dist) {
        this.dist =dist;
    }


    public void HoxFox() {
        long time1 = 0;
        long time2 = 0;
        long time3 = 0;

        int numIterations = 1;

        for (int i = 0; i < numIterations; i++) {
            long time = System.currentTimeMillis();

            System.out.println("\tTime:" + (System.currentTimeMillis() - time) + "ms");
            time1 += System.currentTimeMillis() - time;

            time = System.currentTimeMillis();


            System.out.println("\tTime:" + (System.currentTimeMillis() - time) + "ms");
            time2 += System.currentTimeMillis() - time;

            time = System.currentTimeMillis();
            // Run branch and bound
            this.branchAndBound();
            System.out.println("\tTime:" + (System.currentTimeMillis() - time) + "ms");
            time3 += System.currentTimeMillis() - time;
        }

        // Output average time for functions
        System.out.println("\n\tBF:" + time1 / numIterations + "ms");
        System.out.println("\tNN:" + time2 / numIterations + "ms");
        System.out.println("\tBB:" + time3 / numIterations + "ms");
        // Output rough memory usage (profiler is more accurate)
        System.out.println(
                "KB: " + (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024);
    }


    public void branchAndBound() {
        System.out.println("branchAndBound:");
        // Setup city list
        resetLists();

        // Remove stoke from permutations as always start and end
        List<Integer> cityNums = new ArrayList<Integer>();
        for (int i = 0; i < this.dist.length; i++) {
            cityNums.add(i);
        }

        // Calculate
        permute(new Route(), cityNums, false);
        // Output the number of complete permutations generated NOTE: This is also the
        // number of times the optimal route improved
        System.out.println("\tComplete Permutations: " + BaBRoutePerms.size());
        System.out.println("\t" + BaBcheapestRoute.toString() + "\n\tCost: " + getRouteCost(BaBcheapestRoute));
    }


    public void resetLists() {
        BFRoutePerms = new ArrayList<Route>();
        BaBRoutePerms = new ArrayList<Route>();

        cities = new ArrayList<NodeData>();

        for (int i = 0; i < this.dist.length; i++) {
            cities.add(new NodeDataImpl(i, null));
        }


    }


    public void permute(Route r, List<Integer> notVisited, boolean isBruteForce) {
        if (!notVisited.isEmpty()) {

            for (int i = 0; i < notVisited.size(); i++) {
                // Pointer to first city in list
                int temp = notVisited.remove(0);

                Route newRoute = new Route();
                // Lazy copy
                for (NodeData c1 : r.getRoute()) {
                    newRoute.getRoute().add(c1);
                }

                // Add the first city from notVisited to the route
                newRoute.getRoute().add(cities.get(temp));

                if (isBruteForce) {
                    // Recursive call
                    permute(newRoute, notVisited, true);
                } else {
                    // If a complete route has not yet been created keep permuting
                    if (BaBRoutePerms.isEmpty()) {
                        // Recursive call
                        permute(newRoute, notVisited, false);
                    } else if (getRouteCost(newRoute) < BaBcheapestCost) {
                        // Current route cost is less than the best so far so keep permuting
                        permute(newRoute, notVisited, false);
                    }
                }
                // Add first city back into notVisited list
                notVisited.add(temp);
            }
        } else {
            // Route is complete
            if (isBruteForce) {
                BFRoutePerms.add(r);
            } else {
                // Add stoke to start and end of route
                r.getRoute().add(0, cities.get(this.dist.length - 1));
                r.getRoute().add(cities.get(this.dist.length - 1));

                BaBRoutePerms.add(r);

                // If shorter than best so far, update best cost
                if (getRouteCost(r) < BaBcheapestCost) {
                    BaBcheapestRoute = r;
                    BaBcheapestCost = getRouteCost(r);
                }
            }
        }
    }




    private Double getRouteCost(Route r) {
        double tempCost = 0;
        // Add route costs

        for (int i = 0; i < r.getRoute().size() - 1; i++) {

            tempCost += this.dist[r.getRoute().get(i).getKey()][r.getRoute().get(i + 1).getKey()];


//                tempCost += alg.shortestPathDist(r.getRoute().get(i).getKey(), r.getRoute().get(i + 1).getKey());


        }
        return tempCost;
    }
}