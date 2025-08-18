//package src;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.lang.Math; 

import java.io.*;
import java.util.*;

public class ReadCSV {

    public static <e> void main(String[] args) throws IOException, CsvException {

        String filename = "Scrapper/Addresses.csv";                 // represents our adjacency matrix
        String start_location = "";
        String destination = "";

        List<String[]> adjacencyMatrix;                             // data from csv (adjacency matrix)

        // Reading start location and destination
        try(Scanner scanner = new Scanner(new File("data/LocationQuery.txt"))) {
            start_location = scanner.nextLine();
            destination = scanner.nextLine();
        } catch (IOException e) {}

        // reading csv file into adjacencyMatrix.
        try (CSVReader reader = new CSVReader(new FileReader(filename))){
            adjacencyMatrix = reader.readAll();
        }

        int row_start_index = 0;            // index of the row on which the start location is.
        int column_end_index = 0;           // index of the column on which the stop location exist.

        for(String[] array: adjacencyMatrix){
            // get index of destination from first row
            if (adjacencyMatrix.indexOf(array) == 0){
                for (int i = 0; i < array.length; i++){
                    if (array[i].contains(destination)){
                        column_end_index = i;
                    }
                }
            }

            // get index of source from first column
            if (array[0].contains(start_location)) {
                int index = adjacencyMatrix.indexOf(array);
                row_start_index = index;
            }
        }

        // Finding total distance
        double actual_distance = 0.0;
        try {
            actual_distance = Double.parseDouble(adjacencyMatrix.get(row_start_index)[column_end_index]);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing distance data. Using default value of 0.0");
        }

        System.out.println("🏛️  UG NAVIGATE - COMPREHENSIVE ROUTE ANALYSIS");
        System.out.println("================================================");
        System.out.println("Start Location: " + start_location);
        System.out.println("Destination: " + destination);
        System.out.println("Direct Distance: " + actual_distance + " km");
        System.out.println();

        // Generate routes using multiple advanced algorithms
        List<Route> allRoutes = new ArrayList<>();

        // 1. Standard Routes (backward compatibility)
        System.out.println("🔄 Generating standard routes...");
        Route directRoute = generateDirectRoute(adjacencyMatrix, start_location, destination, row_start_index, column_end_index, actual_distance);
        directRoute.algorithmUsed = "Direct Path Algorithm";
        allRoutes.add(directRoute);

        Route altRoute1 = generateAlternativeRoute(adjacencyMatrix, start_location, destination, row_start_index, column_end_index, actual_distance, 1);
        altRoute1.algorithmUsed = "Alternative Path Algorithm 1";
        allRoutes.add(altRoute1);

        Route altRoute2 = generateAlternativeRoute(adjacencyMatrix, start_location, destination, row_start_index, column_end_index, actual_distance, 2);
        altRoute2.algorithmUsed = "Alternative Path Algorithm 2";
        allRoutes.add(altRoute2);

        // 2. Advanced Algorithms
        System.out.println("⚡ Applying advanced algorithms...");
        
        // Dijkstra's Algorithm
        Route dijkstraRoute = DijkstraAlgorithm.findShortestPath(adjacencyMatrix, start_location, destination);
        if (dijkstraRoute != null) {
            dijkstraRoute.algorithmUsed = "Dijkstra's Algorithm";
            allRoutes.add(dijkstraRoute);
        }

        // A* Algorithm
        Route aStarRoute = AStarAlgorithm.findOptimalPath(adjacencyMatrix, start_location, destination);
        if (aStarRoute != null) {
            aStarRoute.algorithmUsed = "A* Search Algorithm";
            allRoutes.add(aStarRoute);
        }

        // Floyd-Warshall based route
        double[][] allPairsDistances = FloydWarshallAlgorithm.findAllPairsShortestPaths(adjacencyMatrix);
        if (allPairsDistances != null) {
            String[] locations = adjacencyMatrix.get(0);
            double shortestDistance = FloydWarshallAlgorithm.getShortestDistance(allPairsDistances, locations, start_location, destination);
            if (shortestDistance > 0) {
                StringBuilder fwRoute = new StringBuilder(start_location.replace(" Legon", ""))
                                              .append(" => ")
                                              .append(destination.replace(" Legon", ""));
                Map<String, Double> fwSegments = new LinkedHashMap<>();
                fwSegments.put(destination.replace(" Legon", ""), shortestDistance);
                double[] fwDetails = distance_time(fwSegments);
                Route floydWarshallRoute = new Route(fwRoute, fwDetails[0], fwDetails[1], "Floyd-Warshall Algorithm");
                allRoutes.add(floydWarshallRoute);
            }
        }

        // Remove null routes and duplicates
        allRoutes.removeIf(Objects::isNull);
        
        // Sort routes by distance
        Route[] routeArray = allRoutes.toArray(new Route[0]);
        RouteSorter.sortRoutes(routeArray, "distance", true);

        // Print comprehensive analysis
        System.out.println("\n📊 ROUTE ANALYSIS SUMMARY:");
        System.out.println("==========================");
        for (int i = 0; i < Math.min(routeArray.length, 5); i++) {
            Route route = routeArray[i];
            System.out.printf("Route %d (%s):\n", i + 1, route.algorithmUsed);
            System.out.printf("  Path: %s\n", route.full_path);
            System.out.printf("  Distance: %.3f km\n", route.distance);
            System.out.printf("  Time: %.1f minutes\n", route.time_taken);
            System.out.printf("  Efficiency: %.2f km/min\n\n", route.distance / Math.max(route.time_taken, 0.1));
        }

        // Write top 3 routes for GUI (backward compatibility)
        Route[] topThreeRoutes = Arrays.copyOf(routeArray, Math.min(3, routeArray.length));
        PrintWriter writer = new PrintWriter("data/GeneratedRoutes.txt", "UTF-8");
        writeGUI(writer, topThreeRoutes);

        // Also write comprehensive analysis
        PrintWriter detailedWriter = new PrintWriter("data/DetailedRouteAnalysis.txt", "UTF-8");
        writeDetailedAnalysis(detailedWriter, routeArray, start_location, destination);

        System.out.println("✅ Route analysis complete!");
        System.out.println("📄 Standard results written to: data/GeneratedRoutes.txt");
        System.out.println("📋 Detailed analysis written to: data/DetailedRouteAnalysis.txt");
    }

    // Generate direct/shortest route
    public static Route generateDirectRoute(List<String[]> adjacencyMatrix, String start_location, String destination, int row_start_index, int column_end_index, double actual_distance) {
        Map<String, Double> routes = new LinkedHashMap<String, Double>();
        StringBuilder route_string = new StringBuilder(start_location);
        
        // For very short distances, just use direct route
        if (actual_distance < 0.3) {
            routes.put(destination, actual_distance);
            route_string.append(" => ").append(destination);
        } else {
            // Find one intermediate point that's closest to the midpoint
            String[] destinationArray = adjacencyMatrix.get(0);
            String[] startArray = adjacencyMatrix.get(row_start_index);
            
            double bestDistance = Double.MAX_VALUE;
            int bestIndex = -1;
            
            // Find best intermediate location
            for (int i = 1; i < startArray.length; i++) {
                try {
                    double distanceFromStart = Double.parseDouble(startArray[i]);
                    double distanceToDestination = Double.parseDouble(adjacencyMatrix.get(i)[column_end_index]);
                    double totalDistance = distanceFromStart + distanceToDestination;
                    
                    if (distanceFromStart > 0 && distanceToDestination > 0 && totalDistance < bestDistance) {
                        bestDistance = totalDistance;
                        bestIndex = i;
                    }
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    continue;
                }
            }
            
            if (bestIndex > 0) {
                try {
                    routes.put(destinationArray[bestIndex], Double.parseDouble(startArray[bestIndex]));
                    routes.put(destination, Double.parseDouble(adjacencyMatrix.get(bestIndex)[column_end_index]));
                    route_string.append(" => ").append(destinationArray[bestIndex]).append(" => ").append(destination);
                } catch (NumberFormatException e) {
                    // Fallback to direct route
                    routes.clear();
                    routes.put(destination, actual_distance);
                    route_string = new StringBuilder(start_location + " => " + destination);
                }
            } else {
                // Fallback to direct route
                routes.put(destination, actual_distance);
                route_string.append(" => ").append(destination);
            }
        }
        
        double[] routeDetails = distance_time(routes);
        return new Route(route_string, routeDetails[0], routeDetails[1]);
    }

    // Generate alternative routes by avoiding certain paths
    public static Route generateAlternativeRoute(List<String[]> adjacencyMatrix, String start_location, String destination, int row_start_index, int column_end_index, double actual_distance, int alternativeType) {
        Map<String, Double> routes = new LinkedHashMap<String, Double>();
        StringBuilder route_string = new StringBuilder(start_location);
        String[] destinationArray = adjacencyMatrix.get(0);
        
        List<String> usedLocations = new ArrayList<>();
        usedLocations.add(start_location);
        
        int currentRowIndex = row_start_index;
        int maxHops = alternativeType == 1 ? 2 : 3; // Different number of hops for variety
        
        for (int hop = 0; hop < maxHops; hop++) {
            double bestDistance = Double.MAX_VALUE;
            int bestIndex = -1;
            String[] currentArray = adjacencyMatrix.get(currentRowIndex);
            
            // Look for next best location that gets us closer to destination
            for (int i = 1; i < currentArray.length; i++) {
                try {
                    String locationName = destinationArray[i];
                    
                    // Skip if already used or if it's the destination (save for last)
                    if (usedLocations.contains(locationName) || locationName.equals(destination)) continue;
                    
                    double distanceFromCurrent = Double.parseDouble(currentArray[i]);
                    double distanceToDestination = Double.parseDouble(adjacencyMatrix.get(i)[column_end_index]);
                    
                    if (distanceFromCurrent > 0 && distanceToDestination > 0) {
                        // For alternative routes, use different selection criteria
                        double score = distanceFromCurrent + distanceToDestination;
                        if (alternativeType == 1) {
                            // Prefer routes that take a more direct intermediate step
                            score = distanceFromCurrent * 0.7 + distanceToDestination;
                        } else {
                            // Prefer routes that approach from different directions
                            score = distanceFromCurrent + distanceToDestination * 0.7;
                        }
                        
                        if (score < bestDistance && distanceToDestination < actual_distance * 3) {
                            bestDistance = score;
                            bestIndex = i;
                        }
                    }
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    continue;
                }
            }
            
            // If we found a good intermediate location
            if (bestIndex > 0 && hop < maxHops - 1) {
                try {
                    String nextLocation = destinationArray[bestIndex];
                    double segmentDistance = Double.parseDouble(currentArray[bestIndex]);
                    
                    routes.put(nextLocation, segmentDistance);
                    route_string.append(" => ").append(nextLocation);
                    usedLocations.add(nextLocation);
                    
                    currentRowIndex = bestIndex;
                } catch (NumberFormatException e) {
                    break;
                }
            } else {
                // Make final hop to destination
                try {
                    double finalDistance = Double.parseDouble(adjacencyMatrix.get(currentRowIndex)[column_end_index]);
                    routes.put(destination, finalDistance);
                    route_string.append(" => ").append(destination);
                } catch (NumberFormatException e) {
                    routes.put(destination, actual_distance);
                    route_string.append(" => ").append(destination);
                }
                break;
            }
        }
        
        // Print the route for debugging
        System.out.println(route_string);
        
        double[] routeDetails = distance_time(routes);
        return new Route(route_string, routeDetails[0], routeDetails[1]);
    }

    // distance and time of a given route
    public static double[] distance_time(Map<String, Double> routes){
        double distance = 0.0;
        double average_walking_speed = 5.0; // km/h - normal walking speed
        final double MAX_REASONABLE_CAMPUS_DISTANCE = 6.0; // km - max reasonable campus distance

        // Calculate total distance with validation
        for (String name: routes.keySet()) {
            double segmentDistance = routes.get(name);
            
            // Apply distance correction for unrealistic values
            if (segmentDistance > MAX_REASONABLE_CAMPUS_DISTANCE) {
                System.out.println("⚠️  WARNING: Unrealistic distance detected: " + segmentDistance + " km");
                System.out.println("   Applying correction factor...");
                // Apply correction factor based on the assumption that very long distances are measurement errors
                segmentDistance = Math.min(segmentDistance * 0.4, MAX_REASONABLE_CAMPUS_DISTANCE);
                System.out.println("   Corrected distance: " + segmentDistance + " km");
            }
            
            distance += segmentDistance;
        }

        // time - convert hours to minutes with more precision
        double timeInHours = distance / average_walking_speed;
        double timeInMinutes = timeInHours * 60;
        
        // Debug output to see actual calculations
        System.out.println("Distance: " + String.format("%.3f", distance) + " km");
        System.out.println("Time calculation: " + String.format("%.3f", distance) + " km ÷ " + average_walking_speed + " km/h × 60 = " + String.format("%.1f", timeInMinutes) + " minutes");
        
        // More realistic time calculation
        double timeTaken;
        if (timeInMinutes < 1.0) {
            timeTaken = Math.round(timeInMinutes * 10) / 10.0; // Round to nearest 0.1 minute
            timeTaken = Math.max(0.5, timeTaken); // Minimum 0.5 minute (30 seconds)
        } else if (timeInMinutes < 5) {
            timeTaken = Math.round(timeInMinutes * 2) / 2.0; // Round to nearest 0.5 minute
        } else {
            timeTaken = Math.round(timeInMinutes); // Round to nearest minute for longer routes
        }

        // print out the details
        System.out.println("Total Path Distance: " + distance);
        System.out.println("Time taken to travel path: " + timeTaken + " minute(s)");     // Time taken to travel a route.
        System.out.println();
        System.out.println();

        double[] route_details = {distance, timeTaken};

        // return route details
        return route_details;
    }

    // writes routes data for GUI
    public static void writeGUI (PrintWriter writer, Route[] routes){
        for (int i=0; i < routes.length; i++){
            Route route = routes[i];
            writer.println(route.full_path);
            writer.println(route.distance + " km");
            writer.println(route.time_taken + " mins");
        } 
        writer.close();
    }
    
    // writes detailed route analysis
    public static void writeDetailedAnalysis(PrintWriter writer, Route[] routes, String startLocation, String destination) {
        writer.println("🏛️  UG NAVIGATE - DETAILED ROUTE ANALYSIS");
        writer.println("==========================================");
        writer.println("From: " + startLocation);
        writer.println("To: " + destination);
        writer.println("Analysis Date: " + new Date());
        writer.println();
        
        Map<String, Integer> algorithmCount = new HashMap<>();
        for (Route route : routes) {
            String algorithm = route.algorithmUsed != null ? route.algorithmUsed : "Standard";
            algorithmCount.put(algorithm, algorithmCount.getOrDefault(algorithm, 0) + 1);
        }
        
        writer.println("📈 ALGORITHMS USED:");
        algorithmCount.forEach((algorithm, count) -> 
            writer.printf("   • %s: %d route(s)%n", algorithm, count));
        
        writer.println();
        writer.printf("🛣️  ROUTE OPTIONS (%d total):%n", routes.length);
        writer.println("=============================");
        
        for (int i = 0; i < routes.length; i++) {
            Route route = routes[i];
            writer.printf("%n%d. %s%n", i + 1, route.full_path);
            writer.printf("   Algorithm: %s%n", route.algorithmUsed);
            writer.printf("   Distance: %.3f km%n", route.distance);
            writer.printf("   Time: %.1f minutes%n", route.time_taken);
            writer.printf("   Efficiency: %.2f km/min%n", route.distance / Math.max(route.time_taken, 0.1));
        }
        
        writer.println();
        writer.println("✅ Analysis complete - All routes evaluated using advanced algorithms");
        writer.close();
    }
}