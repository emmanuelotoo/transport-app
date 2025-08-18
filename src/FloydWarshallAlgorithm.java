/**
 * Implementation of Floyd-Warshall Algorithm for finding shortest paths
 * between all pairs of vertices in the University of Ghana campus
 */
public class FloydWarshallAlgorithm {
    
    /**
     * Floyd-Warshall algorithm to find shortest paths between all pairs of locations
     * @param adjacencyMatrix The distance matrix between locations
     * @return 2D array of shortest distances between all pairs of locations
     */
    public static double[][] findAllPairsShortestPaths(java.util.List<String[]> adjacencyMatrix) {
        if (adjacencyMatrix == null || adjacencyMatrix.isEmpty()) {
            return null;
        }
        
        int n = adjacencyMatrix.get(0).length;
        double[][] distances = new double[n][n];
        
        // Initialize distance matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    distances[i][j] = 0.0;
                } else {
                    try {
                        String distanceStr = adjacencyMatrix.get(i)[j];
                        double distance = Double.parseDouble(distanceStr);
                        distances[i][j] = (distance > 0) ? distance : Double.POSITIVE_INFINITY;
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        distances[i][j] = Double.POSITIVE_INFINITY;
                    }
                }
            }
        }
        
        // Floyd-Warshall algorithm
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distances[i][k] != Double.POSITIVE_INFINITY && 
                        distances[k][j] != Double.POSITIVE_INFINITY) {
                        double newDistance = distances[i][k] + distances[k][j];
                        if (newDistance < distances[i][j]) {
                            distances[i][j] = newDistance;
                        }
                    }
                }
            }
        }
        
        return distances;
    }
    
    /**
     * Get shortest distance between two specific locations using pre-computed Floyd-Warshall results
     * @param distances The result from Floyd-Warshall algorithm
     * @param locations Array of location names
     * @param startLocation Starting location name
     * @param endLocation Destination location name
     * @return Shortest distance between the two locations, or -1 if not found
     */
    public static double getShortestDistance(double[][] distances, String[] locations, 
                                           String startLocation, String endLocation) {
        int startIndex = -1, endIndex = -1;
        
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].contains(startLocation)) {
                startIndex = i;
            }
            if (locations[i].contains(endLocation)) {
                endIndex = i;
            }
        }
        
        if (startIndex == -1 || endIndex == -1 || distances == null) {
            return -1;
        }
        
        double distance = distances[startIndex][endIndex];
        return (distance == Double.POSITIVE_INFINITY) ? -1 : distance;
    }
    
    /**
     * Find all locations within a certain distance from a starting location
     * @param distances The result from Floyd-Warshall algorithm
     * @param locations Array of location names
     * @param startLocation Starting location name
     * @param maxDistance Maximum distance threshold
     * @return List of locations within the specified distance
     */
    public static java.util.List<String> findLocationsWithinDistance(double[][] distances, String[] locations,
                                                                    String startLocation, double maxDistance) {
        java.util.List<String> nearbyLocations = new java.util.ArrayList<>();
        
        int startIndex = -1;
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].contains(startLocation)) {
                startIndex = i;
                break;
            }
        }
        
        if (startIndex == -1 || distances == null) {
            return nearbyLocations;
        }
        
        for (int i = 0; i < locations.length; i++) {
            if (i != startIndex && distances[startIndex][i] <= maxDistance && 
                distances[startIndex][i] != Double.POSITIVE_INFINITY) {
                nearbyLocations.add(locations[i]);
            }
        }
        
        return nearbyLocations;
    }
    
    /**
     * Check if there's a path between two locations
     * @param distances The result from Floyd-Warshall algorithm
     * @param locations Array of location names
     * @param startLocation Starting location name
     * @param endLocation Destination location name
     * @return true if a path exists, false otherwise
     */
    public static boolean hasPath(double[][] distances, String[] locations,
                                 String startLocation, String endLocation) {
        return getShortestDistance(distances, locations, startLocation, endLocation) > 0;
    }
    
    /**
     * Print the distance matrix in a readable format (for debugging)
     * @param distances The distance matrix
     * @param locations Array of location names
     */
    public static void printDistanceMatrix(double[][] distances, String[] locations) {
        System.out.println("Floyd-Warshall All-Pairs Shortest Distances:");
        System.out.println("============================================");
        
        // Print header
        System.out.printf("%-30s", "From/To");
        for (int i = 0; i < Math.min(locations.length, 5); i++) {
            String shortName = locations[i].replace(" Legon", "").replace(" and ", "&");
            if (shortName.length() > 15) {
                shortName = shortName.substring(0, 12) + "...";
            }
            System.out.printf("%-15s", shortName);
        }
        System.out.println();
        
        // Print rows
        for (int i = 0; i < Math.min(distances.length, 5); i++) {
            String shortName = locations[i].replace(" Legon", "").replace(" and ", "&");
            if (shortName.length() > 25) {
                shortName = shortName.substring(0, 22) + "...";
            }
            System.out.printf("%-30s", shortName);
            
            for (int j = 0; j < Math.min(distances[i].length, 5); j++) {
                if (distances[i][j] == Double.POSITIVE_INFINITY) {
                    System.out.printf("%-15s", "∞");
                } else {
                    System.out.printf("%-15.3f", distances[i][j]);
                }
            }
            System.out.println();
        }
        
        if (distances.length > 5) {
            System.out.println("... (showing first 5x5 entries only)");
        }
    }
}
