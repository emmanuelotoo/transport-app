import java.util.*;

/**
 * Landmark-based route searching and filtering system
 * Implements binary search and linear search algorithms for landmark-based routing
 */
public class LandmarkSearch {
    
    /**
     * Search for routes that pass through or near a specific landmark
     * @param adjacencyMatrix The distance matrix between locations
     * @param startLocation Starting location
     * @param endLocation Destination location
     * @param landmark Landmark to pass through or near
     * @param maxDetourDistance Maximum additional distance allowed for detour
     * @return List of routes passing through the landmark
     */
    public static List<Route> searchRoutesWithLandmark(List<String[]> adjacencyMatrix, 
                                                      String startLocation, String endLocation, 
                                                      String landmark, double maxDetourDistance) {
        List<Route> landmarkRoutes = new ArrayList<>();
        
        if (adjacencyMatrix == null || adjacencyMatrix.isEmpty()) {
            return landmarkRoutes;
        }
        
        String[] locations = adjacencyMatrix.get(0);
        
        // Find all locations that match the landmark criteria
        List<Integer> landmarkIndices = findLandmarkLocations(locations, landmark);
        
        if (landmarkIndices.isEmpty()) {
            System.out.println("No locations found matching landmark: " + landmark);
            return landmarkRoutes;
        }
        
        // Generate routes through each landmark location
        for (int landmarkIndex : landmarkIndices) {
            String landmarkLocation = locations[landmarkIndex];
            
            try {
                // Route: Start -> Landmark -> End
                Route routeViaLandmark = generateRouteViaLandmark(adjacencyMatrix, 
                                                                startLocation, endLocation, 
                                                                landmarkLocation, maxDetourDistance);
                if (routeViaLandmark != null) {
                    landmarkRoutes.add(routeViaLandmark);
                }
            } catch (Exception e) {
                System.out.println("Error generating route via " + landmarkLocation + ": " + e.getMessage());
            }
        }
        
        // Sort routes by distance and return top 3
        if (!landmarkRoutes.isEmpty()) {
            landmarkRoutes.sort((r1, r2) -> Double.compare(r1.distance, r2.distance));
            
            // Return at most 3 routes
            int maxRoutes = Math.min(3, landmarkRoutes.size());
            return new ArrayList<>(landmarkRoutes.subList(0, maxRoutes));
        }
        
        return landmarkRoutes;
    }
    
    /**
     * Find all location indices that match the landmark criteria using binary search
     * @param locations Array of all location names
     * @param landmark Landmark keyword to search for
     * @return List of indices of matching locations
     */
    private static List<Integer> findLandmarkLocations(String[] locations, String landmark) {
        List<Integer> indices = new ArrayList<>();
        String searchTerm = landmark.toLowerCase();
        
        // Create sorted array for binary search
        LocationIndex[] sortedLocations = new LocationIndex[locations.length];
        for (int i = 0; i < locations.length; i++) {
            sortedLocations[i] = new LocationIndex(i, locations[i].toLowerCase());
        }
        Arrays.sort(sortedLocations, (a, b) -> a.name.compareTo(b.name));
        
        // Binary search for exact matches first
        for (LocationIndex loc : sortedLocations) {
            if (loc.name.contains(searchTerm)) {
                indices.add(loc.index);
            }
        }
        
        // If no exact matches, try partial matches
        if (indices.isEmpty()) {
            indices = findPartialMatches(locations, searchTerm);
        }
        
        return indices;
    }
    
    /**
     * Find partial matches using linear search
     */
    private static List<Integer> findPartialMatches(String[] locations, String searchTerm) {
        List<Integer> indices = new ArrayList<>();
        
        for (int i = 0; i < locations.length; i++) {
            String location = locations[i].toLowerCase();
            if (isPartialMatch(location, searchTerm)) {
                indices.add(i);
            }
        }
        
        return indices;
    }
    
    /**
     * Check if location is a partial match for the search term
     */
    private static boolean isPartialMatch(String location, String searchTerm) {
        // Check for various landmark categories
        String[] bankKeywords = {"bank", "atm", "gcb", "financial"};
        String[] hospitalKeywords = {"hospital", "clinic", "health", "medical"};
        String[] libraryKeywords = {"library", "balme", "reading"};
        String[] sportsKeywords = {"sports", "stadium", "field", "court", "pool", "gym"};
        String[] diningKeywords = {"canteen", "dining", "restaurant", "food", "market"};
        String[] residenceKeywords = {"hall", "hostel", "residence", "accommodation"};
        String[] academicKeywords = {"department", "school", "faculty", "college", "institute"};
        
        if (searchTerm.contains("bank")) {
            return containsAny(location, bankKeywords);
        } else if (searchTerm.contains("hospital") || searchTerm.contains("medical")) {
            return containsAny(location, hospitalKeywords);
        } else if (searchTerm.contains("library")) {
            return containsAny(location, libraryKeywords);
        } else if (searchTerm.contains("sports") || searchTerm.contains("gym") || searchTerm.contains("stadium")) {
            return containsAny(location, sportsKeywords);
        } else if (searchTerm.contains("food") || searchTerm.contains("dining") || searchTerm.contains("canteen")) {
            return containsAny(location, diningKeywords);
        } else if (searchTerm.contains("hall") || searchTerm.contains("hostel") || searchTerm.contains("residence")) {
            return containsAny(location, residenceKeywords);
        } else if (searchTerm.contains("department") || searchTerm.contains("school") || searchTerm.contains("academic")) {
            return containsAny(location, academicKeywords);
        } else {
            return location.contains(searchTerm);
        }
    }
    
    /**
     * Check if location contains any of the keywords
     */
    private static boolean containsAny(String location, String[] keywords) {
        for (String keyword : keywords) {
            if (location.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Generate a route that passes through a specific landmark
     */
    private static Route generateRouteViaLandmark(List<String[]> adjacencyMatrix, 
                                                String startLocation, String endLocation, 
                                                String landmarkLocation, double maxDetourDistance) {
        
        // Get direct route distance for comparison
        double directDistance = getDirectDistance(adjacencyMatrix, startLocation, endLocation);
        if (directDistance < 0) return null;
        
        // Calculate route via landmark
        double distanceToLandmark = getDirectDistance(adjacencyMatrix, startLocation, landmarkLocation);
        double distanceFromLandmark = getDirectDistance(adjacencyMatrix, landmarkLocation, endLocation);
        
        if (distanceToLandmark < 0 || distanceFromLandmark < 0) return null;
        
        double totalDistance = distanceToLandmark + distanceFromLandmark;
        double detourDistance = totalDistance - directDistance;
        
        // Check if detour is acceptable
        if (detourDistance > maxDetourDistance) {
            return null;
        }
        
        // Build route
        StringBuilder routeString = new StringBuilder();
        routeString.append(startLocation.replace(" Legon", ""))
                  .append(" => ")
                  .append(landmarkLocation.replace(" Legon", ""))
                  .append(" => ")
                  .append(endLocation.replace(" Legon", ""));
        
        Map<String, Double> routeSegments = new LinkedHashMap<>();
        routeSegments.put(landmarkLocation.replace(" Legon", ""), distanceToLandmark);
        routeSegments.put(endLocation.replace(" Legon", ""), distanceFromLandmark);
        
        double[] routeDetails = ReadCSV.distance_time(routeSegments);
        return new Route(routeString, routeDetails[0], routeDetails[1]);
    }
    
    /**
     * Get direct distance between two locations
     */
    private static double getDirectDistance(List<String[]> adjacencyMatrix, String from, String to) {
        String[] locations = adjacencyMatrix.get(0);
        
        int fromIndex = -1, toIndex = -1;
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].contains(from)) fromIndex = i;
            if (locations[i].contains(to)) toIndex = i;
        }
        
        if (fromIndex == -1 || toIndex == -1) return -1;
        
        try {
            return Double.parseDouble(adjacencyMatrix.get(fromIndex)[toIndex]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Search for all landmarks within a certain distance from a location
     * @param adjacencyMatrix The distance matrix
     * @param location Starting location
     * @param maxDistance Maximum search distance
     * @param landmarkType Type of landmark to search for
     * @return List of nearby landmarks
     */
    public static List<String> findNearbyLandmarks(List<String[]> adjacencyMatrix, String location, 
                                                  double maxDistance, String landmarkType) {
        List<String> nearbyLandmarks = new ArrayList<>();
        
        if (adjacencyMatrix == null || adjacencyMatrix.isEmpty()) {
            return nearbyLandmarks;
        }
        
        String[] locations = adjacencyMatrix.get(0);
        
        // Find location index
        int locationIndex = -1;
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].contains(location)) {
                locationIndex = i;
                break;
            }
        }
        
        if (locationIndex == -1) return nearbyLandmarks;
        
        String[] distanceRow = adjacencyMatrix.get(locationIndex);
        
        for (int i = 0; i < locations.length; i++) {
            if (i != locationIndex) {
                try {
                    double distance = Double.parseDouble(distanceRow[i]);
                    if (distance > 0 && distance <= maxDistance) {
                        String locationName = locations[i].toLowerCase();
                        if (isLandmarkType(locationName, landmarkType)) {
                            nearbyLandmarks.add(locations[i]);
                        }
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        
        return nearbyLandmarks;
    }
    
    /**
     * Check if a location matches the specified landmark type
     */
    private static boolean isLandmarkType(String location, String landmarkType) {
        String type = landmarkType.toLowerCase();
        
        switch (type) {
            case "bank":
                return location.contains("bank") || location.contains("atm") || location.contains("gcb");
            case "hospital":
                return location.contains("hospital") || location.contains("clinic") || location.contains("medical");
            case "library":
                return location.contains("library") || location.contains("balme");
            case "sports":
                return location.contains("sports") || location.contains("stadium") || location.contains("field") || 
                       location.contains("court") || location.contains("pool");
            case "dining":
                return location.contains("canteen") || location.contains("dining") || location.contains("market");
            case "residence":
                return location.contains("hall") || location.contains("hostel");
            case "academic":
                return location.contains("department") || location.contains("school") || location.contains("faculty");
            default:
                return location.contains(type);
        }
    }
    
    /**
     * Helper class for binary search operations
     */
    private static class LocationIndex {
        int index;
        String name;
        
        LocationIndex(int index, String name) {
            this.index = index;
            this.name = name;
        }
    }
    
    /**
     * Get available landmark categories
     * @return List of landmark categories that can be searched
     */
    public static List<String> getAvailableLandmarkCategories() {
        return Arrays.asList("bank", "hospital", "library", "sports", "dining", "residence", "academic");
    }
    
    /**
     * Print landmark search results
     * @param routes List of routes found
     * @param landmark The landmark that was searched for
     */
    public static void printLandmarkRoutes(List<Route> routes, String landmark) {
        if (routes.isEmpty()) {
            System.out.println("No routes found passing through landmark: " + landmark);
            return;
        }
        
        System.out.println("\\nRoutes passing through landmark '" + landmark + "':");
        System.out.println("================================================");
        
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            System.out.printf("Option %d: %s\\n", i + 1, route.full_path);
            System.out.printf("         Distance: %.3f km, Time: %.1f mins\\n", 
                            route.distance, route.time_taken);
            System.out.println();
        }
    }
}
