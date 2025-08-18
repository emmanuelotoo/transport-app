import java.util.*;

/**
 * Route sorting utilities implementing Quick Sort and Merge Sort algorithms
 * for organizing routes by distance, time, and other criteria
 */
public class RouteSorter {
    
    /**
     * Sort routes by distance using Quick Sort algorithm
     * @param routes Array of routes to sort
     * @param low Starting index
     * @param high Ending index
     */
    public static void quickSortByDistance(Route[] routes, int low, int high) {
        if (low < high) {
            int pivotIndex = partitionByDistance(routes, low, high);
            quickSortByDistance(routes, low, pivotIndex - 1);
            quickSortByDistance(routes, pivotIndex + 1, high);
        }
    }
    
    /**
     * Partition function for Quick Sort by distance
     */
    private static int partitionByDistance(Route[] routes, int low, int high) {
        double pivot = routes[high].distance;
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (routes[j].distance <= pivot) {
                i++;
                swap(routes, i, j);
            }
        }
        swap(routes, i + 1, high);
        return i + 1;
    }
    
    /**
     * Sort routes by time using Quick Sort algorithm
     * @param routes Array of routes to sort
     * @param low Starting index
     * @param high Ending index
     */
    public static void quickSortByTime(Route[] routes, int low, int high) {
        if (low < high) {
            int pivotIndex = partitionByTime(routes, low, high);
            quickSortByTime(routes, low, pivotIndex - 1);
            quickSortByTime(routes, pivotIndex + 1, high);
        }
    }
    
    /**
     * Partition function for Quick Sort by time
     */
    private static int partitionByTime(Route[] routes, int low, int high) {
        double pivot = routes[high].time_taken;
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (routes[j].time_taken <= pivot) {
                i++;
                swap(routes, i, j);
            }
        }
        swap(routes, i + 1, high);
        return i + 1;
    }
    
    /**
     * Swap two routes in the array
     */
    private static void swap(Route[] routes, int i, int j) {
        Route temp = routes[i];
        routes[i] = routes[j];
        routes[j] = temp;
    }
    
    /**
     * Sort routes by distance using Merge Sort algorithm
     * @param routes Array of routes to sort
     */
    public static void mergeSortByDistance(Route[] routes) {
        if (routes.length <= 1) return;
        mergeSortByDistance(routes, 0, routes.length - 1);
    }
    
    /**
     * Recursive Merge Sort by distance
     */
    private static void mergeSortByDistance(Route[] routes, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortByDistance(routes, left, mid);
            mergeSortByDistance(routes, mid + 1, right);
            mergeByDistance(routes, left, mid, right);
        }
    }
    
    /**
     * Merge function for Merge Sort by distance
     */
    private static void mergeByDistance(Route[] routes, int left, int mid, int right) {
        Route[] leftArray = new Route[mid - left + 1];
        Route[] rightArray = new Route[right - mid];
        
        System.arraycopy(routes, left, leftArray, 0, leftArray.length);
        System.arraycopy(routes, mid + 1, rightArray, 0, rightArray.length);
        
        int i = 0, j = 0, k = left;
        
        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i].distance <= rightArray[j].distance) {
                routes[k++] = leftArray[i++];
            } else {
                routes[k++] = rightArray[j++];
            }
        }
        
        while (i < leftArray.length) {
            routes[k++] = leftArray[i++];
        }
        
        while (j < rightArray.length) {
            routes[k++] = rightArray[j++];
        }
    }
    
    /**
     * Sort routes by time using Merge Sort algorithm
     * @param routes Array of routes to sort
     */
    public static void mergeSortByTime(Route[] routes) {
        if (routes.length <= 1) return;
        mergeSortByTime(routes, 0, routes.length - 1);
    }
    
    /**
     * Recursive Merge Sort by time
     */
    private static void mergeSortByTime(Route[] routes, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortByTime(routes, left, mid);
            mergeSortByTime(routes, mid + 1, right);
            mergeByTime(routes, left, mid, right);
        }
    }
    
    /**
     * Merge function for Merge Sort by time
     */
    private static void mergeByTime(Route[] routes, int left, int mid, int right) {
        Route[] leftArray = new Route[mid - left + 1];
        Route[] rightArray = new Route[right - mid];
        
        System.arraycopy(routes, left, leftArray, 0, leftArray.length);
        System.arraycopy(routes, mid + 1, rightArray, 0, rightArray.length);
        
        int i = 0, j = 0, k = left;
        
        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i].time_taken <= rightArray[j].time_taken) {
                routes[k++] = leftArray[i++];
            } else {
                routes[k++] = rightArray[j++];
            }
        }
        
        while (i < leftArray.length) {
            routes[k++] = leftArray[i++];
        }
        
        while (j < rightArray.length) {
            routes[k++] = rightArray[j++];
        }
    }
    
    /**
     * Sort routes using a custom comparator (for more complex sorting criteria)
     * @param routes Array of routes to sort
     * @param sortCriteria Sorting criteria: "distance", "time", "efficiency"
     * @param ascending True for ascending order, false for descending
     */
    public static void sortRoutes(Route[] routes, String sortCriteria, boolean ascending) {
        Arrays.sort(routes, (r1, r2) -> {
            double comparison = 0;
            
            switch (sortCriteria.toLowerCase()) {
                case "distance":
                    comparison = Double.compare(r1.distance, r2.distance);
                    break;
                case "time":
                    comparison = Double.compare(r1.time_taken, r2.time_taken);
                    break;
                case "efficiency":
                    // Efficiency = distance/time ratio (lower is better)
                    double efficiency1 = r1.distance / Math.max(r1.time_taken, 0.1);
                    double efficiency2 = r2.distance / Math.max(r2.time_taken, 0.1);
                    comparison = Double.compare(efficiency1, efficiency2);
                    break;
                default:
                    comparison = Double.compare(r1.distance, r2.distance);
            }
            
            return ascending ? (int) comparison : (int) -comparison;
        });
    }
    
    /**
     * Sort routes by multiple criteria (distance first, then time)
     * @param routes Array of routes to sort
     */
    public static void sortByMultipleCriteria(Route[] routes) {
        Arrays.sort(routes, (r1, r2) -> {
            // First compare by distance
            int distanceComparison = Double.compare(r1.distance, r2.distance);
            if (distanceComparison != 0) {
                return distanceComparison;
            }
            // If distances are equal, compare by time
            return Double.compare(r1.time_taken, r2.time_taken);
        });
    }
    
    /**
     * Find top N routes by a specific criteria
     * @param routes Array of routes
     * @param criteria Sorting criteria
     * @param n Number of top routes to return
     * @return Array of top N routes
     */
    public static Route[] getTopNRoutes(Route[] routes, String criteria, int n) {
        if (routes == null || routes.length == 0) {
            return new Route[0];
        }
        
        Route[] sortedRoutes = Arrays.copyOf(routes, routes.length);
        sortRoutes(sortedRoutes, criteria, true); // Sort in ascending order (best first)
        
        int resultSize = Math.min(n, sortedRoutes.length);
        return Arrays.copyOf(sortedRoutes, resultSize);
    }
    
    /**
     * Print sorted routes with their rankings
     * @param routes Array of sorted routes
     * @param criteria The criteria used for sorting
     */
    public static void printSortedRoutes(Route[] routes, String criteria) {
        System.out.println("\\nRoutes sorted by " + criteria + ":");
        System.out.println("==========================================");
        
        for (int i = 0; i < routes.length; i++) {
            System.out.printf("Rank %d: %s\\n", i + 1, routes[i].full_path);
            System.out.printf("        Distance: %.3f km, Time: %.1f mins\\n", 
                            routes[i].distance, routes[i].time_taken);
            System.out.println();
        }
    }
    
    /**
     * Validate that routes are sorted correctly
     * @param routes Array of routes to validate
     * @param criteria Sorting criteria used
     * @return true if properly sorted, false otherwise
     */
    public static boolean isSorted(Route[] routes, String criteria) {
        if (routes.length <= 1) return true;
        
        for (int i = 1; i < routes.length; i++) {
            boolean isCorrectOrder = false;
            
            switch (criteria.toLowerCase()) {
                case "distance":
                    isCorrectOrder = routes[i-1].distance <= routes[i].distance;
                    break;
                case "time":
                    isCorrectOrder = routes[i-1].time_taken <= routes[i].time_taken;
                    break;
                case "efficiency":
                    double eff1 = routes[i-1].distance / Math.max(routes[i-1].time_taken, 0.1);
                    double eff2 = routes[i].distance / Math.max(routes[i].time_taken, 0.1);
                    isCorrectOrder = eff1 <= eff2;
                    break;
            }
            
            if (!isCorrectOrder) {
                return false;
            }
        }
        return true;
    }
}
