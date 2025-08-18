# UG Navigate: Optimal Routing Solution for University of Ghana Campus

A comprehensive Java desktop application implementing advanced algorithms for finding optimal walking routes between locations on the University of Ghana campus. This project fulfills DCIT 204 requirements with complete implementation of pathfinding, sorting, searching, and optimization algorithms.

## 🎯 Project Overview

**UG Navigate** provides intelligent route planning across the University of Ghana campus using real geographical data from 121+ campus locations. The system implements multiple advanced algorithms to find the most efficient routes while considering different optimization criteria.

## ✨ Key Features

### 🔍 **Advanced Pathfinding Algorithms**
- **Dijkstra's Algorithm** - Guaranteed shortest path calculation
- **A* Search Algorithm** - Heuristic-based optimal pathfinding
- **Floyd-Warshall Algorithm** - All-pairs shortest path optimization
- **Greedy Algorithm** - Fast approximate routing

### 📊 **Sorting & Analysis**
- **Quick Sort** - Efficient route sorting by distance/time
- **Merge Sort** - Stable multi-criteria route organization  
- **Multi-criteria sorting** - Distance, time, and efficiency metrics
- **Top-N route selection** - Find best routes by any criteria

### 🏛️ **Campus-Specific Features**
- **121+ campus locations** - Complete UG campus coverage
- **Landmark-based routing** - Routes through specific buildings
- **Distance validation** - Realistic campus walking times
- **Multiple route options** - Compare different path strategies

### 🚀 **Optimization Techniques**
- **Dynamic Programming** - Optimal substructure solutions
- **Divide and Conquer** - Efficient algorithm design
- **Vogel Approximation Method** - Transportation optimization
- **Northwest Corner Method** - Initial solution generation
- **Critical Path Method** - Project scheduling optimization

## 🏗️ Project Structure

```
transport-app/
├── src/                          # Java source code
│   ├── ReadCSV.java             # Main routing engine with all algorithms
│   ├── EnhancedRoutesGUI.java   # Advanced GUI with algorithm selection
│   ├── Routes.java              # Basic route display interface
│   ├── Activity.java            # Location selection interface
│   ├── Welcome.java             # Application welcome screen
│   ├── Route.java               # Route data model
│   │
│   ├── DijkstraAlgorithm.java   # Shortest path implementation
│   ├── AStarAlgorithm.java      # Heuristic pathfinding
│   ├── FloydWarshallAlgorithm.java # All-pairs shortest paths
│   ├── RouteSorter.java         # Quick Sort & Merge Sort
│   ├── LandmarkSearch.java      # Landmark-based routing
│   ├── OptimizationAlgorithms.java # Advanced optimization methods
│   ├── UGRouteNavigator.java    # Route navigation system
│   ├── UGNavigateTestSuite.java # Comprehensive testing
│   ├── DistanceValidator.java   # Data validation utilities
│   └── TestLocationLoading.java # CSV loading verification
│
├── Scrapper/                    # Campus geographical data
│   ├── Addresses.csv           # 121x121 adjacency matrix
│   ├── Addresses.xlsx          # Excel format data
│   └── dummy_data.csv          # Test data
│
├── data/                       # Runtime generated files
│   ├── LocationQuery.txt       # Current route query
│   ├── GeneratedRoutes.txt     # Standard route results
│   └── DetailedRouteAnalysis.txt # Comprehensive analysis
│
├── images/                     # Campus images
├── IMPLEMENTATION_DOCUMENTATION.md # Technical documentation
└── External Libraries:
    ├── opencsv-5.8.jar        # CSV processing
    ├── commons-lang3-3.12.0.jar # Apache Commons utilities
    └── commons-text-1.10.0.jar  # Text processing utilities
```

## 🚀 Quick Start

### Prerequisites
- **Java 8+** (JDK/JRE)
- All required JAR files are included in the project

### Compilation
```bash
# Compile all source files
javac -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" src/*.java
```

### Running the Application

#### 🖥️ **Enhanced GUI (Recommended)**
```bash
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" EnhancedRoutesGUI
```
*Features: Algorithm selection, landmark search, sorting options, 121 campus locations*

#### 🎨 **Basic GUI**
```bash
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" Welcome
```
*Features: Simple route planning interface*

#### 💻 **Command Line Route Calculation**
```bash
# Edit locations in data/LocationQuery.txt, then run:
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" ReadCSV
```

#### 🧪 **Algorithm Testing**
```bash
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" UGNavigateTestSuite
```

## 🔬 Algorithm Implementation Details

### **Pathfinding Algorithms**
- **Dijkstra's Algorithm** (`DijkstraAlgorithm.java`): O(V²) shortest path with priority queue optimization
- **A* Search** (`AStarAlgorithm.java`): Heuristic-guided search with Manhattan distance
- **Floyd-Warshall** (`FloydWarshallAlgorithm.java`): O(V³) all-pairs shortest paths with dynamic programming

### **Sorting Algorithms**
- **Quick Sort** (`RouteSorter.java`): O(n log n) average case with pivot partitioning
- **Merge Sort** (`RouteSorter.java`): O(n log n) guaranteed with divide-and-conquer

### **Optimization Methods**
- **Dynamic Programming**: Optimal route caching and subproblem solutions
- **Greedy Algorithms**: Fast approximate solutions for route selection
- **Divide and Conquer**: Recursive problem decomposition

## 📊 Data Format & Validation

### Campus Data
- **Addresses.csv**: 121×121 adjacency matrix with real distances (km)
- **Distance Validation**: Automatic correction for unrealistic measurements
- **Walking Speed**: 5.0 km/h (normal pace) with smart time calculations

### Route Output
- **Standard Results**: `data/GeneratedRoutes.txt`
- **Detailed Analysis**: `data/DetailedRouteAnalysis.txt`
- **Algorithm Metrics**: Distance, time, efficiency, and algorithm used

## 🎓 Educational Objectives (DCIT 204)

This project demonstrates mastery of:

### ✅ **Algorithm Design Paradigms**
- **Greedy Algorithms**: Local optimization strategies
- **Dynamic Programming**: Optimal substructure and memoization
- **Divide and Conquer**: Recursive problem decomposition

### ✅ **Data Structures & Algorithms**
- **Graph Algorithms**: Shortest path and traversal
- **Sorting Algorithms**: Comparison-based and efficient sorting
- **Search Algorithms**: Linear, binary, and heuristic search

### ✅ **Optimization Techniques**
- **Transportation Problems**: Vogel Approximation, Northwest Corner
- **Project Management**: Critical Path Method
- **Multi-criteria Optimization**: Route efficiency analysis

## 🔧 Customization & Extension

### Adding New Locations
1. Update `Scrapper/Addresses.csv` with new location and distances
2. System automatically loads all locations from CSV header row

### Algorithm Tuning
- **Walking Speed**: Modify `average_walking_speed` in `ReadCSV.java`
- **Distance Validation**: Adjust `MAX_REASONABLE_CAMPUS_DISTANCE`
- **Algorithm Parameters**: Tune heuristics in individual algorithm files

## 🤝 Contributors

**DCIT 204 Group Project - University of Ghana**
- Advanced algorithm implementation and optimization
- Campus data collection and validation
- User interface design and testing

## 📄 License

Educational and research use. University of Ghana Computer Science Department.

---

**🎯 Navigate the University of Ghana campus with confidence using advanced computer science algorithms!**
