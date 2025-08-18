import java.util.*;
import java.io.*;

/**
 * Comprehensive Route Planner implementing all required algorithms and techniques
 * for the University of Ghana campus navigation system
 */
public class UGRouteNavigator {
    
    private List<String[]> adjacencyMatrix;
    private String[] locations;
    private double[][] floydWarshallDistances;
    
    /**
     * Constructor - loads the campus data
     */
    public UGRouteNavigator() {
        loadCampusData();
        if (adjacencyMatrix != null && !adjacencyMatrix.isEmpty()) {
            locations = adjacencyMatrix.get(0);
            // Pre-compute Floyd-Warshall distances for efficiency
            floydWarshallDistances = FloydWarshallAlgorithm.findAllPairsShortestPaths(adjacencyMatrix);
        }
    }
    
    /**
     * Load campus adjacency matrix from CSV file
     */
    private void loadCampusData() {
        try {
            com.opencsv.CSVReader reader = new com.opencsv.CSVReader(new FileReader("Scrapper/Addresses.csv"));
            adjacencyMatrix = reader.readAll();
            reader.close();
            System.out.println("Campus data loaded successfully. Total locations: " + 
                             (adjacencyMatrix.size() - 1));
        } catch (Exception e) {
            System.err.println("Error loading campus data: " + e.getMessage());
            adjacencyMatrix = new ArrayList<>();
        }
    }
    
    /**
     * Find optimal routes using multiple algorithms and techniques
     * @param startLocation Starting point
     * @param endLocation Destination
     * @param preferences User preferences for route selection
     * @return RouteResults containing multiple route options
     */
    public RouteResults findOptimalRoutes(String startLocation, String endLocation, RoutePreferences preferences) {
        RouteResults results = new RouteResults();
        List<Route> allRoutes = new ArrayList<>();
        
        System.out.println("\\n🔍 Finding optimal routes from " + startLocation + " to " + endLocation);
        System.out.println("===============================================================");
        
        // 1. Dijkstra's Algorithm - Shortest Path
        System.out.println("1. Applying Dijkstra's Algorithm...");
        Route dijkstraRoute = DijkstraAlgorithm.findShortestPath(adjacencyMatrix, startLocation, endLocation);
        if (dijkstraRoute != null) {
            dijkstraRoute.algorithmUsed = "Dijkstra's Algorithm";
            allRoutes.add(dijkstraRoute);
            System.out.println("   ✓ Shortest path found: " + dijkstraRoute.distance + " km");
        }
        
        // 2. A* Algorithm - Optimal with Heuristics
        System.out.println("2. Applying A* Search Algorithm...");
        Route aStarRoute = AStarAlgorithm.findOptimalPath(adjacencyMatrix, startLocation, endLocation);
        if (aStarRoute != null) {
            aStarRoute.algorithmUsed = "A* Search Algorithm";
            allRoutes.add(aStarRoute);
            System.out.println("   ✓ Heuristic-optimized path found: " + aStarRoute.distance + " km");
        }
        
        // 3. Greedy Algorithm Approach
        System.out.println("3. Applying Greedy Algorithm...");
        Route greedyRoute = generateGreedyRoute(startLocation, endLocation);
        if (greedyRoute != null) {
            greedyRoute.algorithmUsed = "Greedy Algorithm";
            allRoutes.add(greedyRoute);
            System.out.println("   ✓ Greedy path found: " + greedyRoute.distance + " km");
        }
        
        // 4. Dynamic Programming Approach
        System.out.println("4. Applying Dynamic Programming...");
        Route dpRoute = generateDynamicProgrammingRoute(startLocation, endLocation);
        if (dpRoute != null) {
            dpRoute.algorithmUsed = "Dynamic Programming";
            allRoutes.add(dpRoute);
            System.out.println("   ✓ DP-optimized path found: " + dpRoute.distance + " km");
        }
        
        // 5. Optimization Methods
        if (preferences.useOptimizationMethods) {
            System.out.println("5. Applying Optimization Methods...");
            
            Route vogelRoute = OptimizationAlgorithms.applyOptimization(adjacencyMatrix, 
                                                                       startLocation, endLocation, "vogel");
            if (vogelRoute != null) {
                vogelRoute.algorithmUsed = "Vogel Approximation Method";
                allRoutes.add(vogelRoute);
            }
            
            Route criticalPathRoute = OptimizationAlgorithms.applyOptimization(adjacencyMatrix, 
                                                                              startLocation, endLocation, "critical_path");
            if (criticalPathRoute != null) {
                criticalPathRoute.algorithmUsed = "Critical Path Method";
                allRoutes.add(criticalPathRoute);
            }
        }
        
        // 6. Landmark-based routes if specified
        if (preferences.landmark != null && !preferences.landmark.isEmpty()) {
            System.out.println("6. Finding routes through landmark: " + preferences.landmark);
            List<Route> landmarkRoutes = LandmarkSearch.searchRoutesWithLandmark(
                adjacencyMatrix, startLocation, endLocation, preferences.landmark, preferences.maxDetourDistance);
            
            for (Route route : landmarkRoutes) {
                route.algorithmUsed = "Landmark-based Search";
            }
            allRoutes.addAll(landmarkRoutes);
        }
        
        // Remove duplicates and null routes
        allRoutes.removeIf(Objects::isNull);
        allRoutes = removeDuplicateRoutes(allRoutes);
        
        // Sort routes based on preferences
        sortRoutesByPreferences(allRoutes, preferences);
        
        // Select top routes
        int maxRoutes = Math.min(preferences.maxRoutes, allRoutes.size());
        results.routes = new ArrayList<>(allRoutes.subList(0, maxRoutes));
        results.totalRoutesFound = allRoutes.size();
        results.algorithmsSummary = generateAlgorithmsSummary(results.routes);
        
        System.out.println("\\n📊 Route Analysis Complete:");
        System.out.println("   Total routes found: " + results.totalRoutesFound);
        System.out.println("   Top " + maxRoutes + " routes selected based on preferences");
        
        return results;
    }
    
    /**
     * Generate route using Greedy Algorithm approach
     */
    private Route generateGreedyRoute(String startLocation, String endLocation) {
        // Greedy approach: always choose the nearest unvisited location that gets us closer to destination
        
        Set<String> visited = new HashSet<>();
        StringBuilder routeString = new StringBuilder(startLocation.replace(" Legon", ""));
        Map<String, Double> routeSegments = new LinkedHashMap<>();
        
        String currentLocation = startLocation;
        visited.add(currentLocation);
        
        while (!currentLocation.equals(endLocation)) {
            String nextLocation = findGreedyNextLocation(currentLocation, endLocation, visited);
            
            if (nextLocation == null || nextLocation.equals(currentLocation)) {
                // Direct to destination if no good intermediate found
                if (!currentLocation.equals(endLocation)) {
                    double directDistance = getDirectDistance(currentLocation, endLocation);
                    if (directDistance > 0) {
                        routeString.append(" => ").append(endLocation.replace(" Legon", ""));
                        routeSegments.put(endLocation.replace(" Legon", ""), directDistance);
                    }
                }
                break;
            }
            
            double segmentDistance = getDirectDistance(currentLocation, nextLocation);
            if (segmentDistance > 0) {
                routeString.append(" => ").append(nextLocation.replace(" Legon", ""));
                routeSegments.put(nextLocation.replace(" Legon", ""), segmentDistance);
                visited.add(nextLocation);
                currentLocation = nextLocation;
            } else {
                break;
            }
            
            // Safety check to prevent infinite loops
            if (visited.size() > 10) break;
        }
        
        if (routeSegments.isEmpty()) {
            return null;
        }
        
        double[] routeDetails = ReadCSV.distance_time(routeSegments);
        return new Route(routeString, routeDetails[0], routeDetails[1]);
    }
    
    /**
     * Find next location using greedy approach
     */
    private String findGreedyNextLocation(String currentLocation, String endLocation, Set<String> visited) {
        double bestScore = Double.MAX_VALUE;
        String bestLocation = null;
        
        int currentIndex = getLocationIndex(currentLocation);
        int endIndex = getLocationIndex(endLocation);
        
        if (currentIndex == -1 || endIndex == -1) return null;
        
        String[] currentRow = adjacencyMatrix.get(currentIndex);
        
        for (int i = 0; i < locations.length; i++) {
            String location = locations[i];
            
            if (!visited.contains(location) && !location.equals(currentLocation)) {
                try {
                    double distanceFromCurrent = Double.parseDouble(currentRow[i]);
                    double distanceToEnd = Double.parseDouble(adjacencyMatrix.get(i)[endIndex]);
                    
                    if (distanceFromCurrent > 0 && distanceToEnd > 0) {
                        // Greedy score: prefer locations closer to current and closer to destination
                        double greedyScore = distanceFromCurrent + distanceToEnd * 0.8;
                        
                        if (greedyScore < bestScore) {
                            bestScore = greedyScore;
                            bestLocation = location;
                        }
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        
        return bestLocation;
    }
    
    /**
     * Generate route using Dynamic Programming approach
     */
    private Route generateDynamicProgrammingRoute(String startLocation, String endLocation) {
        // DP approach: find optimal substructure solution
        // Use memoization to store optimal paths to intermediate points
        
        Map<String, Route> dpTable = new HashMap<>();
        return generateDPRouteRecursive(startLocation, endLocation, dpTable, new HashSet<>());
    }
    
    /**
     * Recursive DP route generation with memoization
     */
    private Route generateDPRouteRecursive(String currentLocation, String endLocation, 
                                         Map<String, Route> memo, Set<String> visited) {
        
        String key = currentLocation + "->" + endLocation;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        
        // Base case: direct route to destination
        double directDistance = getDirectDistance(currentLocation, endLocation);
        if (directDistance > 0) {
            StringBuilder directRoute = new StringBuilder(currentLocation.replace(" Legon", ""))
                                                  .append(" => ")
                                                  .append(endLocation.replace(" Legon", ""));
            Map<String, Double> segments = new LinkedHashMap<>();
            segments.put(endLocation.replace(" Legon", ""), directDistance);
            double[] details = ReadCSV.distance_time(segments);
            Route directRouteObj = new Route(directRoute, details[0], details[1]);
            memo.put(key, directRouteObj);
            return directRouteObj;
        }
        
        // Recursive case: try intermediate locations
        Route bestRoute = null;
        double bestDistance = Double.MAX_VALUE;
        
        visited.add(currentLocation);
        
        int currentIndex = getLocationIndex(currentLocation);
        if (currentIndex != -1) {
            String[] currentRow = adjacencyMatrix.get(currentIndex);
            
            for (int i = 0; i < Math.min(locations.length, 20); i++) { // Limit search space
                String intermediate = locations[i];
                
                if (!visited.contains(intermediate) && !intermediate.equals(endLocation)) {
                    try {
                        double segmentDistance = Double.parseDouble(currentRow[i]);
                        if (segmentDistance > 0 && segmentDistance < 2.0) { // Reasonable intermediate distance
                            
                            Route restOfRoute = generateDPRouteRecursive(intermediate, endLocation, memo, new HashSet<>(visited));
                            if (restOfRoute != null) {
                                double totalDistance = segmentDistance + restOfRoute.distance;
                                
                                if (totalDistance < bestDistance) {
                                    bestDistance = totalDistance;
                                    
                                    // Construct combined route
                                    StringBuilder combinedRoute = new StringBuilder(currentLocation.replace(" Legon", ""))
                                                                          .append(" => ")
                                                                          .append(restOfRoute.full_path);
                                    
                                    Map<String, Double> combinedSegments = new LinkedHashMap<>();
                                    combinedSegments.put(intermediate.replace(" Legon", ""), segmentDistance);
                                    // Add remaining segments from restOfRoute would require parsing, simplified here
                                    
                                    double[] combinedDetails = ReadCSV.distance_time(combinedSegments);
                                    bestRoute = new Route(combinedRoute, totalDistance, 
                                                        combinedDetails[1] + restOfRoute.time_taken);
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
        }
        
        visited.remove(currentLocation);
        
        if (bestRoute == null) {
            // Fallback to direct route
            return generateDPRouteRecursive(currentLocation, endLocation, memo, visited);
        }
        
        memo.put(key, bestRoute);
        return bestRoute;
    }
    
    /**
     * Remove duplicate routes based on path similarity
     */
    private List<Route> removeDuplicateRoutes(List<Route> routes) {
        List<Route> uniqueRoutes = new ArrayList<>();
        Set<String> seenPaths = new HashSet<>();
        
        for (Route route : routes) {
            String pathKey = route.full_path.toString();
            if (!seenPaths.contains(pathKey)) {
                seenPaths.add(pathKey);
                uniqueRoutes.add(route);
            }
        }
        
        return uniqueRoutes;
    }
    
    /**
     * Sort routes based on user preferences
     */
    private void sortRoutesByPreferences(List<Route> routes, RoutePreferences preferences) {
        switch (preferences.sortCriteria.toLowerCase()) {
            case "distance":
                RouteSorter.sortRoutes(routes.toArray(new Route[0]), "distance", true);
                break;
            case "time":
                RouteSorter.sortRoutes(routes.toArray(new Route[0]), "time", true);
                break;
            case "efficiency":
                RouteSorter.sortRoutes(routes.toArray(new Route[0]), "efficiency", true);
                break;
            default:
                RouteSorter.sortByMultipleCriteria(routes.toArray(new Route[0]));
        }
    }
    
    /**
     * Generate summary of algorithms used
     */
    private Map<String, Integer> generateAlgorithmsSummary(List<Route> routes) {
        Map<String, Integer> summary = new HashMap<>();
        
        for (Route route : routes) {
            String algorithm = route.algorithmUsed != null ? route.algorithmUsed : "Standard";
            summary.put(algorithm, summary.getOrDefault(algorithm, 0) + 1);
        }
        
        return summary;
    }
    
    /**
     * Get direct distance between two locations
     */
    private double getDirectDistance(String from, String to) {
        int fromIndex = getLocationIndex(from);
        int toIndex = getLocationIndex(to);
        
        if (fromIndex == -1 || toIndex == -1) return -1;
        
        try {
            return Double.parseDouble(adjacencyMatrix.get(fromIndex)[toIndex]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Get location index in the adjacency matrix
     */
    private int getLocationIndex(String location) {
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].contains(location)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Get available landmarks on campus
     */
    public List<String> getAvailableLandmarks() {
        return LandmarkSearch.getAvailableLandmarkCategories();
    }
    
    /**
     * Find nearby landmarks from a location
     */
    public List<String> findNearbyLandmarks(String location, String landmarkType, double maxDistance) {
        return LandmarkSearch.findNearbyLandmarks(adjacencyMatrix, location, maxDistance, landmarkType);
    }
    
    /**
     * Print comprehensive route analysis
     */
    public void printRouteAnalysis(RouteResults results) {
        System.out.println("\\n🏛️  UG NAVIGATE - COMPREHENSIVE ROUTE ANALYSIS");
        System.out.println("================================================");
        
        if (results.routes.isEmpty()) {
            System.out.println("❌ No routes found!");
            return;
        }
        
        System.out.println("📈 ALGORITHMS USED:");
        results.algorithmsSummary.forEach((algorithm, count) -> 
            System.out.println("   • " + algorithm + ": " + count + " route(s)"));
        
        System.out.println("\\n🛣️  TOP ROUTE OPTIONS:");
        System.out.println("=======================");
        
        for (int i = 0; i < results.routes.size(); i++) {
            Route route = results.routes.get(i);
            System.out.println("\\n" + (i + 1) + ". " + route.full_path);
            System.out.println("   Algorithm: " + (route.algorithmUsed != null ? route.algorithmUsed : "Standard"));
            System.out.printf("   Distance: %.3f km\\n", route.distance);
            System.out.printf("   Time: %.1f minutes\\n", route.time_taken);
            System.out.printf("   Efficiency: %.2f km/min\\n", route.distance / Math.max(route.time_taken, 0.1));
        }
        
        System.out.println("\\n✅ Analysis Complete - " + results.totalRoutesFound + " total routes evaluated");
    }
    
    /**
     * Route preferences class
     */
    public static class RoutePreferences {
        public String sortCriteria = "distance"; // "distance", "time", "efficiency"
        public int maxRoutes = 5;
        public String landmark = null;
        public double maxDetourDistance = 1.0;
        public boolean useOptimizationMethods = true;
        
        public RoutePreferences() {}
        
        public RoutePreferences(String sortCriteria, int maxRoutes) {
            this.sortCriteria = sortCriteria;
            this.maxRoutes = maxRoutes;
        }
    }
    
    /**
     * Route results class
     */
    public static class RouteResults {
        public List<Route> routes = new ArrayList<>();
        public int totalRoutesFound = 0;
        public Map<String, Integer> algorithmsSummary = new HashMap<>();
    }
}
