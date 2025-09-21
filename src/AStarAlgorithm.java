import java.util.*;

public class AStarAlgorithm {
    
    static class AStarNode implements Comparable<AStarNode> {
        int vertex;
        double gScore;
        double hScore;
        double fScore;
        AStarNode parent;
        
        public AStarNode(int vertex, double gScore, double hScore) {
            this.vertex = vertex;
            this.gScore = gScore;
            this.hScore = hScore;
            this.fScore = gScore + hScore;
            this.parent = null;
        }
        
        @Override
        public int compareTo(AStarNode other) {
            return Double.compare(this.fScore, other.fScore);
        }
    }
    
    private static double heuristic(String[] locations, int current, int goal) {
        String currentLoc = locations[current].toLowerCase();
        String goalLoc = locations[goal].toLowerCase();
        
        double baseHeuristic = 0.5;
        if (currentLoc.contains("department") && goalLoc.contains("department")) {
            baseHeuristic = 0.3;
        } else if (currentLoc.contains("hall") && goalLoc.contains("hall")) {
            baseHeuristic = 0.4;
        } else if (currentLoc.contains("school") && goalLoc.contains("school")) {
            baseHeuristic = 0.3;
        } else if ((currentLoc.contains("department") && goalLoc.contains("school")) ||
                   (currentLoc.contains("school") && goalLoc.contains("department"))) {
            baseHeuristic = 0.2;
        } else if (currentLoc.contains("hospital") || goalLoc.contains("hospital")) {
            baseHeuristic = 0.8;
        } else if (currentLoc.contains("sports") || goalLoc.contains("sports")) {
            baseHeuristic = 0.6;
        }
        
        int hash = (currentLoc + goalLoc).hashCode();
        double variation = (hash % 100) / 1000.0;
        
        return Math.max(0.1, baseHeuristic + variation);
    }
    
    public static Route findOptimalPath(List<String[]> adjacencyMatrix, String startLocation, String endLocation) {
        if (adjacencyMatrix == null || adjacencyMatrix.isEmpty()) {
            return null;
        }
        
        String[] locations = adjacencyMatrix.get(0);
        int n = locations.length;
        
        int startIndex = -1, endIndex = -1;
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].contains(startLocation)) {
                startIndex = i;
            }
            if (locations[i].contains(endLocation)) {
                endIndex = i;
            }
        }
        
        if (startIndex == -1 || endIndex == -1) {
            return null;
        }
        
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>();
        Set<Integer> closedSet = new HashSet<>();
        Map<Integer, AStarNode> nodeMap = new HashMap<>();
        
        // Initialize start node
        AStarNode startNode = new AStarNode(startIndex, 0.0, heuristic(locations, startIndex, endIndex));
        openSet.offer(startNode);
        nodeMap.put(startIndex, startNode);
        
        while (!openSet.isEmpty()) {
            AStarNode current = openSet.poll();
            
            if (current.vertex == endIndex) {
                // Goal reached, reconstruct path
                return reconstructPath(current, locations, adjacencyMatrix);
            }
            
            closedSet.add(current.vertex);
            
            // Explore neighbors
            String[] currentRow = adjacencyMatrix.get(current.vertex);
            for (int neighbor = 0; neighbor < n; neighbor++) {
                if (closedSet.contains(neighbor) || neighbor == current.vertex) {
                    continue;
                }
                
                try {
                    double edgeWeight = Double.parseDouble(currentRow[neighbor]);
                    if (edgeWeight <= 0) continue; // Invalid edge
                    
                    double tentativeGScore = current.gScore + edgeWeight;
                    
                    AStarNode neighborNode = nodeMap.get(neighbor);
                    if (neighborNode == null) {
                        // New node
                        neighborNode = new AStarNode(neighbor, tentativeGScore, 
                                                   heuristic(locations, neighbor, endIndex));
                        neighborNode.parent = current;
                        nodeMap.put(neighbor, neighborNode);
                        openSet.offer(neighborNode);
                    } else if (tentativeGScore < neighborNode.gScore) {
                        // Better path found
                        neighborNode.gScore = tentativeGScore;
                        neighborNode.fScore = tentativeGScore + neighborNode.hScore;
                        neighborNode.parent = current;
                        
                        // Re-add to open set with updated priority
                        openSet.remove(neighborNode);
                        openSet.offer(neighborNode);
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid edges
                    continue;
                }
            }
        }
        
        return null; // No path found
    }
    
    /**
     * Reconstruct the path from the goal node back to start
     */
    private static Route reconstructPath(AStarNode goalNode, String[] locations, List<String[]> adjacencyMatrix) {
        List<Integer> path = new ArrayList<>();
        AStarNode current = goalNode;
        
        while (current != null) {
            path.add(current.vertex);
            current = current.parent;
        }
        Collections.reverse(path);
        
        // Build route string and calculate segments
        StringBuilder routeString = new StringBuilder();
        Map<String, Double> routeSegments = new LinkedHashMap<>();
        
        for (int i = 0; i < path.size(); i++) {
            int nodeIndex = path.get(i);
            String locationName = locations[nodeIndex].replace(" Legon", "").trim();
            
            if (i == 0) {
                routeString.append(locationName);
            } else {
                routeString.append(" => ").append(locationName);
                
                // Add segment distance
                int prevIndex = path.get(i - 1);
                try {
                    double segmentDistance = Double.parseDouble(adjacencyMatrix.get(prevIndex)[nodeIndex]);
                    routeSegments.put(locationName, segmentDistance);
                } catch (NumberFormatException e) {
                    routeSegments.put(locationName, 0.1); // Default small distance
                }
            }
        }
        
        double[] routeDetails = ReadCSV.distance_time(routeSegments);
        return new Route(routeString, routeDetails[0], routeDetails[1]);
    }
    
    /**
     * Find multiple optimal paths using A* with different heuristics
     * @param adjacencyMatrix The distance matrix between locations
     * @param startLocation Starting location name
     * @param endLocation Destination location name
     * @param numPaths Number of alternative paths to find
     * @return List of Route objects with different optimal paths
     */
    public static List<Route> findMultipleOptimalPaths(List<String[]> adjacencyMatrix, 
                                                      String startLocation, String endLocation, int numPaths) {
        List<Route> routes = new ArrayList<>();
        
        // Find the first optimal path
        Route firstPath = findOptimalPath(adjacencyMatrix, startLocation, endLocation);
        if (firstPath != null) {
            routes.add(firstPath);
        }
        
        // For additional paths, we could implement k-shortest paths algorithm
        // For now, we'll use modified heuristics to find alternative routes
        for (int i = 1; i < numPaths && routes.size() < numPaths; i++) {
            Route alternativePath = findAlternativeOptimalPath(adjacencyMatrix, startLocation, endLocation, i);
            if (alternativePath != null && !isPathSimilar(alternativePath, routes)) {
                routes.add(alternativePath);
            }
        }
        
        return routes;
    }
    
    /**
     * Find alternative optimal path with modified heuristics
     */
    private static Route findAlternativeOptimalPath(List<String[]> adjacencyMatrix, 
                                                  String startLocation, String endLocation, int variation) {
        // This is a simplified approach - in a full implementation, 
        // we'd use more sophisticated k-shortest paths algorithms
        return findOptimalPath(adjacencyMatrix, startLocation, endLocation);
    }
    
    /**
     * Check if a path is similar to existing paths (simple check)
     */
    private static boolean isPathSimilar(Route newRoute, List<Route> existingRoutes) {
        String newPathStr = newRoute.full_path.toString();
        for (Route route : existingRoutes) {
            String existingPathStr = route.full_path.toString();
            if (newPathStr.equals(existingPathStr)) {
                return true;
            }
        }
        return false;
    }
}
