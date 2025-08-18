# Quick Reference Guide - UG Navigate

## 🚀 Common Commands

### Compilation
```bash
# Compile all source files
javac -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" src/*.java
```

### Running Applications

#### Enhanced GUI (Recommended)
```bash
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" EnhancedRoutesGUI
```

#### Basic GUI
```bash
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" Welcome
```

#### Command Line Route Planning
```bash
# 1. Edit data/LocationQuery.txt with start and end locations
# 2. Run route calculation
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" ReadCSV
```

#### Testing & Validation
```bash
# Run comprehensive test suite
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" UGNavigateTestSuite

# Test CSV location loading
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" TestLocationLoading

# Validate distance data
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" DistanceValidator
```

### Clean Build
```bash
# Remove compiled files
rm -f src/*.class

# Recompile
javac -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" src/*.java
```

## 📁 Important Files

### Source Code (src/)
- **ReadCSV.java** - Main routing engine
- **EnhancedRoutesGUI.java** - Advanced user interface
- **DijkstraAlgorithm.java** - Shortest path algorithm
- **AStarAlgorithm.java** - Heuristic pathfinding
- **RouteSorter.java** - Sorting algorithms

### Data Files
- **Scrapper/Addresses.csv** - Campus location adjacency matrix
- **data/LocationQuery.txt** - Current route query
- **data/GeneratedRoutes.txt** - Last calculated routes
- **data/DetailedRouteAnalysis.txt** - Comprehensive analysis

## 🔧 Troubleshooting

### Common Issues
1. **ClassNotFoundException**: Ensure all JAR files are in classpath
2. **GUI won't start**: Verify display environment (desktop/X11)
3. **Distance seems wrong**: Check CSV data validation warnings
4. **File not found**: Verify working directory and file paths

### Performance Tips
- Use EnhancedRoutesGUI for best user experience
- Run UGNavigateTestSuite to verify all algorithms
- Check DistanceValidator for data quality issues

## 📊 Algorithm Performance

| Algorithm | Time Complexity | Use Case |
|-----------|----------------|----------|
| Dijkstra's | O(V²) | Single-source shortest path |
| A* Search | O(b^d) | Heuristic pathfinding |
| Floyd-Warshall | O(V³) | All-pairs shortest path |
| Quick Sort | O(n log n) | Fast route sorting |
| Merge Sort | O(n log n) | Stable sorting |

## 🎯 DCIT 204 Requirements Checklist

- ✅ Greedy Algorithms (route selection)
- ✅ Dynamic Programming (Floyd-Warshall, optimization)
- ✅ Divide and Conquer (Merge Sort, recursive algorithms)
- ✅ Graph Algorithms (Dijkstra's, A*)
- ✅ Sorting Algorithms (Quick Sort, Merge Sort)
- ✅ Search Algorithms (A*, landmark search)
- ✅ Optimization Techniques (Vogel, Northwest Corner, Critical Path)
