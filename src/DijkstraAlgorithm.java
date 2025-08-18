import java.util.*;

/**
 * Implementation of Dijkstra's Algorithm for finding shortest paths
 * in the University of Ghana campus navigation system
 */
public class DijkstraAlgorithm {
    
    /**
     * Represents a graph node with distance for priority queue
     */
    static class Node implements Comparable<Node> {
        int vertex;
        double distance;
        
        public Node(int vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
        
        @Override
        public int compareTo(Node other) {
            return Double.compare(this.distance, other.distance);
        }
    }
    
    /**
     * Find shortest path using Dijkstra's algorithm
     * @param adjacencyMatrix The distance matrix between locations
     * @param startLocation Starting location name
     * @param endLocation Destination location name
     * @return Route object with the shortest path
     */
    public static Route findShortestPath(List<String[]> adjacencyMatrix, String startLocation, String endLocation) {
        if (adjacencyMatrix == null || adjacencyMatrix.isEmpty()) {
            return null;
        }
        
        String[] locations = adjacencyMatrix.get(0);
        int n = locations.length;
        
        // Find indices of start and end locations
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
        
        // Initialize distances and previous nodes
        double[] distances = new double[n];
        int[] previous = new int[n];
        boolean[] visited = new boolean[n];
        
        Arrays.fill(distances, Double.MAX_VALUE);
        Arrays.fill(previous, -1);
        distances[startIndex] = 0.0;
        
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(startIndex, 0.0));
        
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int currentVertex = current.vertex;
            
            if (visited[currentVertex]) continue;
            visited[currentVertex] = true;
            
            if (currentVertex == endIndex) break;
            
            // Check all neighbors
            String[] currentRow = adjacencyMatrix.get(currentVertex);
            for (int neighbor = 0; neighbor < n; neighbor++) {
                if (!visited[neighbor] && neighbor != currentVertex) {
                    try {
                        double weight = Double.parseDouble(currentRow[neighbor]);
                        if (weight > 0) { // Valid edge
                            double newDistance = distances[currentVertex] + weight;
                            if (newDistance < distances[neighbor]) {
                                distances[neighbor] = newDistance;
                                previous[neighbor] = currentVertex;
                                pq.offer(new Node(neighbor, newDistance));
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid edges
                        continue;
                    }
                }
            }
        }
        
        // Reconstruct path
        if (distances[endIndex] == Double.MAX_VALUE) {
            return null; // No path found
        }
        
        List<Integer> path = new ArrayList<>();
        int current = endIndex;
        while (current != -1) {
            path.add(current);
            current = previous[current];
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
     * Find all shortest paths from a source to all other nodes
     * @param adjacencyMatrix The distance matrix between locations
     * @param sourceLocation Starting location name
     * @return Map of location names to their shortest distances from source
     */
    public static Map<String, Double> findAllShortestPaths(List<String[]> adjacencyMatrix, String sourceLocation) {
        Map<String, Double> result = new HashMap<>();
        
        if (adjacencyMatrix == null || adjacencyMatrix.isEmpty()) {
            return result;
        }
        
        String[] locations = adjacencyMatrix.get(0);
        int n = locations.length;
        
        // Find source index
        int sourceIndex = -1;
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].contains(sourceLocation)) {
                sourceIndex = i;
                break;
            }
        }
        
        if (sourceIndex == -1) {
            return result;
        }
        
        // Initialize distances
        double[] distances = new double[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(distances, Double.MAX_VALUE);
        distances[sourceIndex] = 0.0;
        
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(sourceIndex, 0.0));
        
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int currentVertex = current.vertex;
            
            if (visited[currentVertex]) continue;
            visited[currentVertex] = true;
            
            // Check all neighbors
            String[] currentRow = adjacencyMatrix.get(currentVertex);
            for (int neighbor = 0; neighbor < n; neighbor++) {
                if (!visited[neighbor] && neighbor != currentVertex) {
                    try {
                        double weight = Double.parseDouble(currentRow[neighbor]);
                        if (weight > 0) { // Valid edge
                            double newDistance = distances[currentVertex] + weight;
                            if (newDistance < distances[neighbor]) {
                                distances[neighbor] = newDistance;
                                pq.offer(new Node(neighbor, newDistance));
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid edges
                        continue;
                    }
                }
            }
        }
        
        // Store results
        for (int i = 0; i < n; i++) {
            if (distances[i] != Double.MAX_VALUE) {
                result.put(locations[i], distances[i]);
            }
        }
        
        return result;
    }
}
