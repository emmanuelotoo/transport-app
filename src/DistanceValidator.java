import java.io.*;
import com.opencsv.CSVReader;
import java.util.*;

/**
 * Utility to validate and fix unrealistic distances in campus routing
 */
public class DistanceValidator {
    
    private static final double MAX_CAMPUS_DISTANCE = 5.0; // km - reasonable max for campus
    private static final double NORMAL_WALKING_SPEED = 5.0; // km/h
    private static final double SLOW_WALKING_SPEED = 4.0; // km/h
    
    public static void main(String[] args) {
        System.out.println("🔍 DISTANCE VALIDATION REPORT");
        System.out.println("==============================");
        
        try {
            List<String[]> adjacencyMatrix;
            try (CSVReader reader = new CSVReader(new FileReader("Scrapper/Addresses.csv"))) {
                adjacencyMatrix = reader.readAll();
            }
            
            String[] locations = adjacencyMatrix.get(0);
            System.out.println("📊 Analyzing " + locations.length + " locations...\n");
            
            int totalRoutes = 0;
            int suspiciousRoutes = 0;
            List<String> suspiciousResults = new ArrayList<>();
            
            // Check some random routes
            Random random = new Random();
            for (int test = 0; test < 10; test++) {
                int startIdx = random.nextInt(locations.length);
                int endIdx = random.nextInt(locations.length);
                
                if (startIdx != endIdx) {
                    String startLoc = locations[startIdx];
                    String endLoc = locations[endIdx];
                    
                    try {
                        double distance = Double.parseDouble(adjacencyMatrix.get(startIdx)[endIdx]);
                        totalRoutes++;
                        
                        double timeAtNormalSpeed = (distance / NORMAL_WALKING_SPEED) * 60;
                        double timeAtSlowSpeed = (distance / SLOW_WALKING_SPEED) * 60;
                        
                        System.out.printf("Route %d: %s\n", test + 1, startLoc);
                        System.out.printf("      to: %s\n", endLoc);
                        System.out.printf("Distance: %.3f km\n", distance);
                        System.out.printf("Time @ 5km/h: %.1f min\n", timeAtNormalSpeed);
                        System.out.printf("Time @ 4km/h: %.1f min\n", timeAtSlowSpeed);
                        
                        if (distance > MAX_CAMPUS_DISTANCE) {
                            suspiciousRoutes++;
                            suspiciousResults.add(String.format("⚠️  %.3f km: %s → %s (%.0f min)", 
                                distance, startLoc, endLoc, timeAtSlowSpeed));
                            System.out.println("🚨 SUSPICIOUS: Distance too long for campus!");
                        } else if (timeAtNormalSpeed > 60) {
                            suspiciousRoutes++;
                            suspiciousResults.add(String.format("⚠️  %.3f km: %s → %s (%.0f min)", 
                                distance, startLoc, endLoc, timeAtSlowSpeed));
                            System.out.println("⚠️  WARNING: Takes over 1 hour to walk");
                        } else {
                            System.out.println("✅ Reasonable distance");
                        }
                        System.out.println();
                        
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid distance data");
                    }
                }
            }
            
            System.out.println("\n📋 SUMMARY REPORT:");
            System.out.println("==================");
            System.out.printf("Total routes tested: %d\n", totalRoutes);
            System.out.printf("Suspicious routes: %d (%.1f%%)\n", 
                suspiciousRoutes, (suspiciousRoutes * 100.0) / totalRoutes);
            
            if (!suspiciousResults.isEmpty()) {
                System.out.println("\n🚨 SUSPICIOUS ROUTES:");
                for (String result : suspiciousResults) {
                    System.out.println(result);
                }
                
                System.out.println("\n💡 RECOMMENDATIONS:");
                System.out.println("1. Verify CSV distance measurements");
                System.out.println("2. Consider using Google Maps API for accurate distances");
                System.out.println("3. Implement distance caps for campus routes");
                System.out.println("4. Use 5-6 km/h walking speed for normal pace");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Suggest corrected walking time based on realistic campus distances
     */
    public static double getCorrectedWalkingTime(double csvDistance) {
        // Cap unrealistic distances
        double correctedDistance = Math.min(csvDistance, MAX_CAMPUS_DISTANCE);
        
        // Use realistic walking speed
        return (correctedDistance / NORMAL_WALKING_SPEED) * 60; // minutes
    }
}
