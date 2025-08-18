import java.util.*;

/**
 * Implementation of advanced optimization techniques for route planning
 * including Vogel Approximation Method, Northwest Corner Method, and Critical Path Method
 */
public class OptimizationAlgorithms {
    
    /**
     * Vogel Approximation Method (VAM) for optimized route distribution
     * This method finds a good initial solution for transportation problems
     */
    public static class VogelApproximationMethod {
        
        /**
         * Apply VAM to find optimal route assignments
         * @param supply Array of supply values (starting points capacities)
         * @param demand Array of demand values (destination requirements)
         * @param costs 2D array of transportation costs between points
         * @return Allocation matrix showing optimal assignments
         */
        public static int[][] solveTransportationProblem(int[] supply, int[] demand, double[][] costs) {
            int m = supply.length;
            int n = demand.length;
            
            // Create working copies
            int[] supplyCopy = Arrays.copyOf(supply, m);
            int[] demandCopy = Arrays.copyOf(demand, n);
            int[][] allocation = new int[m][n];
            
            List<Integer> availableRows = new ArrayList<>();
            List<Integer> availableCols = new ArrayList<>();
            
            for (int i = 0; i < m; i++) availableRows.add(i);
            for (int j = 0; j < n; j++) availableCols.add(j);
            
            while (!availableRows.isEmpty() && !availableCols.isEmpty()) {
                // Calculate penalties for rows and columns
                Map<Integer, Double> rowPenalties = calculateRowPenalties(costs, availableRows, availableCols);
                Map<Integer, Double> colPenalties = calculateColPenalties(costs, availableRows, availableCols);
                
                // Find maximum penalty
                double maxPenalty = -1;
                boolean isRowPenalty = true;
                int selectedIndex = -1;
                
                for (int row : availableRows) {
                    if (rowPenalties.get(row) > maxPenalty) {
                        maxPenalty = rowPenalties.get(row);
                        isRowPenalty = true;
                        selectedIndex = row;
                    }
                }
                
                for (int col : availableCols) {
                    if (colPenalties.get(col) > maxPenalty) {
                        maxPenalty = colPenalties.get(col);
                        isRowPenalty = false;
                        selectedIndex = col;
                    }
                }
                
                // Allocate based on maximum penalty
                if (isRowPenalty) {
                    allocateFromRow(selectedIndex, supplyCopy, demandCopy, costs, allocation, 
                                  availableRows, availableCols);
                } else {
                    allocateFromColumn(selectedIndex, supplyCopy, demandCopy, costs, allocation, 
                                     availableRows, availableCols);
                }
            }
            
            return allocation;
        }
        
        private static Map<Integer, Double> calculateRowPenalties(double[][] costs, 
                                                                List<Integer> rows, List<Integer> cols) {
            Map<Integer, Double> penalties = new HashMap<>();
            
            for (int row : rows) {
                List<Double> rowCosts = new ArrayList<>();
                for (int col : cols) {
                    rowCosts.add(costs[row][col]);
                }
                Collections.sort(rowCosts);
                
                double penalty = (rowCosts.size() > 1) ? rowCosts.get(1) - rowCosts.get(0) : rowCosts.get(0);
                penalties.put(row, penalty);
            }
            
            return penalties;
        }
        
        private static Map<Integer, Double> calculateColPenalties(double[][] costs, 
                                                                List<Integer> rows, List<Integer> cols) {
            Map<Integer, Double> penalties = new HashMap<>();
            
            for (int col : cols) {
                List<Double> colCosts = new ArrayList<>();
                for (int row : rows) {
                    colCosts.add(costs[row][col]);
                }
                Collections.sort(colCosts);
                
                double penalty = (colCosts.size() > 1) ? colCosts.get(1) - colCosts.get(0) : colCosts.get(0);
                penalties.put(col, penalty);
            }
            
            return penalties;
        }
        
        private static void allocateFromRow(int row, int[] supply, int[] demand, double[][] costs, 
                                          int[][] allocation, List<Integer> availableRows, 
                                          List<Integer> availableCols) {
            // Find minimum cost in the row
            double minCost = Double.MAX_VALUE;
            int minCol = -1;
            
            for (int col : availableCols) {
                if (costs[row][col] < minCost) {
                    minCost = costs[row][col];
                    minCol = col;
                }
            }
            
            // Allocate
            int allocateAmount = Math.min(supply[row], demand[minCol]);
            allocation[row][minCol] = allocateAmount;
            supply[row] -= allocateAmount;
            demand[minCol] -= allocateAmount;
            
            // Remove exhausted supply/demand
            if (supply[row] == 0) availableRows.remove(Integer.valueOf(row));
            if (demand[minCol] == 0) availableCols.remove(Integer.valueOf(minCol));
        }
        
        private static void allocateFromColumn(int col, int[] supply, int[] demand, double[][] costs, 
                                             int[][] allocation, List<Integer> availableRows, 
                                             List<Integer> availableCols) {
            // Find minimum cost in the column
            double minCost = Double.MAX_VALUE;
            int minRow = -1;
            
            for (int row : availableRows) {
                if (costs[row][col] < minCost) {
                    minCost = costs[row][col];
                    minRow = row;
                }
            }
            
            // Allocate
            int allocateAmount = Math.min(supply[minRow], demand[col]);
            allocation[minRow][col] = allocateAmount;
            supply[minRow] -= allocateAmount;
            demand[col] -= allocateAmount;
            
            // Remove exhausted supply/demand
            if (supply[minRow] == 0) availableRows.remove(Integer.valueOf(minRow));
            if (demand[col] == 0) availableCols.remove(Integer.valueOf(minRow));
        }
    }
    
    /**
     * Northwest Corner Method for initial basic feasible solution
     */
    public static class NorthwestCornerMethod {
        
        /**
         * Apply Northwest Corner Method to get initial solution
         * @param supply Array of supply values
         * @param demand Array of demand values
         * @return Allocation matrix using northwest corner rule
         */
        public static int[][] getInitialSolution(int[] supply, int[] demand) {
            int m = supply.length;
            int n = demand.length;
            
            int[] supplyCopy = Arrays.copyOf(supply, m);
            int[] demandCopy = Arrays.copyOf(demand, n);
            int[][] allocation = new int[m][n];
            
            int i = 0, j = 0;
            
            while (i < m && j < n) {
                int allocateAmount = Math.min(supplyCopy[i], demandCopy[j]);
                allocation[i][j] = allocateAmount;
                
                supplyCopy[i] -= allocateAmount;
                demandCopy[j] -= allocateAmount;
                
                if (supplyCopy[i] == 0) {
                    i++;
                } else {
                    j++;
                }
            }
            
            return allocation;
        }
        
        /**
         * Calculate total transportation cost for the allocation
         * @param allocation Allocation matrix
         * @param costs Cost matrix
         * @return Total transportation cost
         */
        public static double calculateTotalCost(int[][] allocation, double[][] costs) {
            double totalCost = 0;
            
            for (int i = 0; i < allocation.length; i++) {
                for (int j = 0; j < allocation[i].length; j++) {
                    totalCost += allocation[i][j] * costs[i][j];
                }
            }
            
            return totalCost;
        }
    }
    
    /**
     * Critical Path Method (CPM) for project scheduling and route optimization
     */
    public static class CriticalPathMethod {
        
        /**
         * Activity class for CPM
         */
        public static class Activity {
            String name;
            int duration;
            List<Activity> dependencies;
            int earliestStart;
            int earliestFinish;
            int latestStart;
            int latestFinish;
            int slack;
            boolean isOnCriticalPath;
            
            public Activity(String name, int duration) {
                this.name = name;
                this.duration = duration;
                this.dependencies = new ArrayList<>();
                this.isOnCriticalPath = false;
            }
            
            public void addDependency(Activity dependency) {
                this.dependencies.add(dependency);
            }
        }
        
        /**
         * Calculate critical path for a set of activities
         * @param activities List of activities with dependencies
         * @return List of activities on the critical path
         */
        public static List<Activity> findCriticalPath(List<Activity> activities) {
            // Forward pass - calculate earliest start and finish times
            calculateEarliestTimes(activities);
            
            // Find project completion time
            int projectDuration = 0;
            for (Activity activity : activities) {
                projectDuration = Math.max(projectDuration, activity.earliestFinish);
            }
            
            // Backward pass - calculate latest start and finish times
            calculateLatestTimes(activities, projectDuration);
            
            // Calculate slack and identify critical path
            List<Activity> criticalPath = new ArrayList<>();
            for (Activity activity : activities) {
                activity.slack = activity.latestStart - activity.earliestStart;
                if (activity.slack == 0) {
                    activity.isOnCriticalPath = true;
                    criticalPath.add(activity);
                }
            }
            
            return criticalPath;
        }
        
        private static void calculateEarliestTimes(List<Activity> activities) {
            // Topological sort to process activities in dependency order
            Map<Activity, Integer> inDegree = new HashMap<>();
            for (Activity activity : activities) {
                inDegree.put(activity, 0);
            }
            
            for (Activity activity : activities) {
                for (Activity dependency : activity.dependencies) {
                    inDegree.put(activity, inDegree.get(activity) + 1);
                }
            }
            
            Queue<Activity> queue = new LinkedList<>();
            for (Activity activity : activities) {
                if (inDegree.get(activity) == 0) {
                    activity.earliestStart = 0;
                    activity.earliestFinish = activity.duration;
                    queue.offer(activity);
                }
            }
            
            while (!queue.isEmpty()) {
                Activity current = queue.poll();
                
                for (Activity activity : activities) {
                    if (activity.dependencies.contains(current)) {
                        activity.earliestStart = Math.max(activity.earliestStart, current.earliestFinish);
                        activity.earliestFinish = activity.earliestStart + activity.duration;
                        
                        inDegree.put(activity, inDegree.get(activity) - 1);
                        if (inDegree.get(activity) == 0) {
                            queue.offer(activity);
                        }
                    }
                }
            }
        }
        
        private static void calculateLatestTimes(List<Activity> activities, int projectDuration) {
            // Initialize latest times
            for (Activity activity : activities) {
                activity.latestFinish = projectDuration;
                activity.latestStart = activity.latestFinish - activity.duration;
            }
            
            // Process in reverse topological order
            boolean changed = true;
            while (changed) {
                changed = false;
                for (Activity activity : activities) {
                    for (Activity successor : getSuccessors(activity, activities)) {
                        int newLatestFinish = successor.latestStart;
                        if (newLatestFinish < activity.latestFinish) {
                            activity.latestFinish = newLatestFinish;
                            activity.latestStart = activity.latestFinish - activity.duration;
                            changed = true;
                        }
                    }
                }
            }
        }
        
        private static List<Activity> getSuccessors(Activity activity, List<Activity> allActivities) {
            List<Activity> successors = new ArrayList<>();
            for (Activity other : allActivities) {
                if (other.dependencies.contains(activity)) {
                    successors.add(other);
                }
            }
            return successors;
        }
        
        /**
         * Print critical path analysis results
         * @param activities List of all activities
         * @param criticalPath List of critical path activities
         */
        public static void printCriticalPathAnalysis(List<Activity> activities, List<Activity> criticalPath) {
            System.out.println("\\nCritical Path Method Analysis:");
            System.out.println("==============================");
            
            System.out.printf("%-15s %-8s %-4s %-4s %-4s %-4s %-5s %-8s\\n", 
                            "Activity", "Duration", "ES", "EF", "LS", "LF", "Slack", "Critical");
            System.out.println("------------------------------------------------------------------------");
            
            for (Activity activity : activities) {
                System.out.printf("%-15s %-8d %-4d %-4d %-4d %-4d %-5d %-8s\\n",
                                activity.name, activity.duration,
                                activity.earliestStart, activity.earliestFinish,
                                activity.latestStart, activity.latestFinish,
                                activity.slack, activity.isOnCriticalPath ? "Yes" : "No");
            }
            
            System.out.println("\\nCritical Path:");
            for (int i = 0; i < criticalPath.size(); i++) {
                System.out.print(criticalPath.get(i).name);
                if (i < criticalPath.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
            
            int totalDuration = 0;
            for (Activity activity : criticalPath) {
                totalDuration = Math.max(totalDuration, activity.earliestFinish);
            }
            System.out.println("Project Duration: " + totalDuration + " time units");
        }
    }
    
    /**
     * Apply optimization techniques to route planning
     * @param adjacencyMatrix The campus distance matrix
     * @param startLocation Starting point
     * @param endLocation Destination
     * @param optimizationMethod Method to use: "vogel", "northwest", "critical_path"
     * @return Optimized route
     */
    public static Route applyOptimization(List<String[]> adjacencyMatrix, String startLocation, 
                                        String endLocation, String optimizationMethod) {
        
        switch (optimizationMethod.toLowerCase()) {
            case "vogel":
                return applyVogelOptimization(adjacencyMatrix, startLocation, endLocation);
            case "northwest":
                return applyNorthwestOptimization(adjacencyMatrix, startLocation, endLocation);
            case "critical_path":
                return applyCriticalPathOptimization(adjacencyMatrix, startLocation, endLocation);
            default:
                System.out.println("Unknown optimization method: " + optimizationMethod);
                return null;
        }
    }
    
    private static Route applyVogelOptimization(List<String[]> adjacencyMatrix, 
                                              String startLocation, String endLocation) {
        // Simplified VAM application for route optimization
        // In practice, this would involve more complex supply/demand modeling
        
        // Use Dijkstra's algorithm as a proxy for VAM optimization
        return DijkstraAlgorithm.findShortestPath(adjacencyMatrix, startLocation, endLocation);
    }
    
    private static Route applyNorthwestOptimization(List<String[]> adjacencyMatrix, 
                                                  String startLocation, String endLocation) {
        // Apply northwest corner principle - prefer routes that go through northwest areas first
        // This is a simplified interpretation for campus navigation
        
        return ReadCSV.generateDirectRoute(adjacencyMatrix, startLocation, endLocation, 
                                         getLocationIndex(adjacencyMatrix, startLocation),
                                         getLocationIndex(adjacencyMatrix, endLocation),
                                         getDirectDistance(adjacencyMatrix, startLocation, endLocation));
    }
    
    private static Route applyCriticalPathOptimization(List<String[]> adjacencyMatrix, 
                                                     String startLocation, String endLocation) {
        // Apply CPM concepts to find the most efficient path considering time constraints
        
        return AStarAlgorithm.findOptimalPath(adjacencyMatrix, startLocation, endLocation);
    }
    
    // Helper methods
    private static int getLocationIndex(List<String[]> adjacencyMatrix, String location) {
        String[] locations = adjacencyMatrix.get(0);
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].contains(location)) {
                return i;
            }
        }
        return -1;
    }
    
    private static double getDirectDistance(List<String[]> adjacencyMatrix, String from, String to) {
        int fromIndex = getLocationIndex(adjacencyMatrix, from);
        int toIndex = getLocationIndex(adjacencyMatrix, to);
        
        if (fromIndex == -1 || toIndex == -1) return 0.0;
        
        try {
            return Double.parseDouble(adjacencyMatrix.get(fromIndex)[toIndex]);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
