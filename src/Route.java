//package src;

public class Route {
    StringBuilder full_path;
    double distance;
    double time_taken;
    String algorithmUsed; // Track which algorithm was used to generate this route

    Route(StringBuilder full_path, double distance, double time_taken){
        this.full_path = full_path;
        this.distance = distance;
        this.time_taken = time_taken;
        this.algorithmUsed = "Standard";
    }
    
    Route(StringBuilder full_path, double distance, double time_taken, String algorithmUsed){
        this.full_path = full_path;
        this.distance = distance;
        this.time_taken = time_taken;
        this.algorithmUsed = algorithmUsed;
    }
}
