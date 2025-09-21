public class FloydWarshallAlgorithm {
    public static double[][] findAllPairsShortestPaths(java.util.List<String[]> adjacencyMatrix) {
        if (adjacencyMatrix == null || adjacencyMatrix.isEmpty()) {
            return null;
        }
        
        int n = adjacencyMatrix.get(0).length;
        double[][] distances = new double[n][n];
        
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
    
    public static boolean hasPath(double[][] distances, String[] locations,
                                 String startLocation, String endLocation) {
        return getShortestDistance(distances, locations, startLocation, endLocation) > 0;
    }
    
    public static void printDistanceMatrix(double[][] distances, String[] locations) {
        System.out.println("Floyd-Warshall All-Pairs Shortest Distances:");
        System.out.println("============================================");
        
        System.out.printf("%-30s", "From/To");
        for (int i = 0; i < Math.min(locations.length, 5); i++) {
            String shortName = locations[i].replace(" Legon", "").replace(" and ", "&");
            if (shortName.length() > 15) {
                shortName = shortName.substring(0, 12) + "...";
            }
            System.out.printf("%-15s", shortName);
        }
        System.out.println();
        
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
