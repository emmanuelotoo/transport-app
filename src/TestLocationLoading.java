import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.List;

/**
 * Test class to verify CSV location loading for EnhancedRoutesGUI
 */
public class TestLocationLoading {
    public static void main(String[] args) {
        System.out.println("🧪 Testing CSV location loading...");
        
        try {
            // Load locations from CSV
            List<String[]> adjacencyMatrix;
            try (CSVReader reader = new CSVReader(new FileReader("Scrapper/Addresses.csv"))) {
                adjacencyMatrix = reader.readAll();
            }
            
            if (adjacencyMatrix != null && !adjacencyMatrix.isEmpty()) {
                String[] locations = adjacencyMatrix.get(0);
                
                System.out.println("✅ Successfully loaded " + locations.length + " locations!");
                System.out.println("\n📍 All available campus locations:");
                System.out.println("=====================================");
                
                for (int i = 0; i < locations.length; i++) {
                    System.out.printf("%3d. %s%n", i+1, locations[i]);
                }
                
                System.out.println("\n🎯 Perfect! Your GUI will now have " + locations.length + " locations to choose from!");
            } else {
                System.out.println("❌ Failed to load locations");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
