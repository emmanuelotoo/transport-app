import java.util.*;

public class DijkstraAlgorithm {
    
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
    
    public static Route findShortestPath(List<String[]> adjacencyMatrix, String startLocation, String endLocation) {
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
            
            String[] currentRow = adjacencyMatrix.get(currentVertex);
            for (int neighbor = 0; neighbor < n; neighbor++) {
                if (!visited[neighbor] && neighbor != currentVertex) {
                    try {
                        double weight = Double.parseDouble(currentRow[neighbor]);
                        if (weight > 0) {
                            double newDistance = distances[currentVertex] + weight;
                            if (newDistance < distances[neighbor]) {
                                distances[neighbor] = newDistance;
                                previous[neighbor] = currentVertex;
                                pq.offer(new Node(neighbor, newDistance));
                            }
                        }
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
        }
        
        if (distances[endIndex] == Double.MAX_VALUE) {
            return null;
        }
        
        List<Integer> path = new ArrayList<>();
        int current = endIndex;
        while (current != -1) {
            path.add(current);
            current = previous[current];
        }
        Collections.reverse(path);
        
        StringBuilder routeString = new StringBuilder();
        Map<String, Double> routeSegments = new LinkedHashMap<>();
        
        for (int i = 0; i < path.size(); i++) {
            int nodeIndex = path.get(i);
            String locationName = locations[nodeIndex].replace(" Legon", "").trim();
            
            if (i == 0) {
                routeString.append(locationName);
            } else {
                routeString.append(" => ").append(locationName);
                
                int prevIndex = path.get(i - 1);
                try {
                    double segmentDistance = Double.parseDouble(adjacencyMatrix.get(prevIndex)[nodeIndex]);
                    routeSegments.put(locationName, segmentDistance);
                } catch (NumberFormatException e) {
                    routeSegments.put(locationName, 0.1);
                }
            }
        }
        
        double[] routeDetails = ReadCSV.distance_time(routeSegments);
        return new Route(routeString, routeDetails[0], routeDetails[1]);
    }
    
    public static Map<String, Double> findAllShortestPaths(List<String[]> adjacencyMatrix, String sourceLocation) {
        Map<String, Double> result = new HashMap<>();
        
        if (adjacencyMatrix == null || adjacencyMatrix.isEmpty()) {
            return result;
        }
        
        String[] locations = adjacencyMatrix.get(0);
        int n = locations.length;
        
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
            
            String[] currentRow = adjacencyMatrix.get(currentVertex);
            for (int neighbor = 0; neighbor < n; neighbor++) {
                if (!visited[neighbor] && neighbor != currentVertex) {
                    try {
                        double weight = Double.parseDouble(currentRow[neighbor]);
                        if (weight > 0) {
                            double newDistance = distances[currentVertex] + weight;
                            if (newDistance < distances[neighbor]) {
                                distances[neighbor] = newDistance;
                                pq.offer(new Node(neighbor, newDistance));
                            }
                        }
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
        }
        
        for (int i = 0; i < n; i++) {
            if (distances[i] != Double.MAX_VALUE) {
                result.put(locations[i], distances[i]);
            }
        }
        
        return result;
    }
}
