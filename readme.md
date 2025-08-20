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

### 📊 **Enhanced GUI Features (EnhancedRoutesGUI.java)**
- **Interactive Algorithm Selection** - Choose specific pathfinding algorithms
- **Landmark-Based Routing** - Route through specific campus landmarks with customizable detour limits
- **Dynamic Route Analysis** - Real-time efficiency scoring and 5-star rating system
- **Multi-Criteria Sorting** - Distance, time, efficiency, or combined optimization
- **Advanced Controls** - Sliders, spinners, and checkboxes for fine-tuning
- **Comprehensive Results** - Algorithm performance metrics and route comparisons
- **Export Capabilities** - Save detailed analysis reports to files
- **Visual Feedback** - Progress indicators and interactive route rating

### 🎨 **Basic GUI Features (Welcome → Activity → Routes)**
- **Simple Navigation Flow** - Three-screen guided experience
- **Campus Image Integration** - University of Ghana imagery throughout
- **Dropdown Location Selection** - Easy start/end point selection from all 121 locations
- **Automatic Route Calculation** - Background processing using core algorithms
- **Clean Results Display** - Two optimized routes with distance and time metrics
- **Try Again Functionality** - Easy navigation back to location selection

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
│   ├── ReadCSV.java             # Core routing engine with all algorithms
│   ├── EnhancedRoutesGUI.java   # 🌟 Advanced GUI with comprehensive features
│   │                            #    • Algorithm selection & optimization
│   │                            #    • Interactive landmark routing  
│   │                            #    • 5-star rating system
│   │                            #    • Export functionality
│   │                            #    • Real-time route analysis
│   │
│   ├── Welcome.java             # 🎨 Basic GUI entry point (splash screen)
│   ├── Activity.java            # Location selection interface (basic GUI)
│   ├── Routes.java              # Route display interface (basic GUI)
│   ├── Route.java               # Route data model
│   │
│   ├── DijkstraAlgorithm.java   # Shortest path implementation
│   ├── AStarAlgorithm.java      # Heuristic pathfinding
│   ├── FloydWarshallAlgorithm.java # All-pairs shortest paths
│   ├── RouteSorter.java         # Quick Sort & Merge Sort implementations
│   ├── LandmarkSearch.java      # Landmark-based routing algorithms
│   ├── OptimizationAlgorithms.java # Vogel, Critical Path, Northwest Corner
│   ├── UGRouteNavigator.java    # Comprehensive route navigation system
│   ├── UGNavigateTestSuite.java # Algorithm testing and validation
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

## 🖥️ User Interface Options

UG Navigate provides **two distinct user experiences** with different levels of functionality:

### 🌟 **Enhanced GUI (Recommended) - Advanced Route Planner**
```bash
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" EnhancedRoutesGUI
```

**Advanced Features:**
- **Algorithm Selection**: Choose between Dijkstra, A*, Greedy, Dynamic Programming
- **Interactive Controls**: Sliders for detour distance, spinners for max routes
- **Landmark Routing**: Route through specific campus landmarks (Bank, Library, Sports, etc.)
- **Multi-Criteria Sorting**: Distance, time, efficiency, or combined metrics
- **5-Star Rating System**: Visual route quality assessment
- **Real-time Analysis**: Comprehensive algorithm performance metrics
- **Export Functionality**: Save detailed route analysis to text files
- **121 Campus Locations**: Full dropdown selection from CSV data

### 🎨 **Basic GUI - Classic Interface**
```bash
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" Welcome
```

**User Flow:**
1. **Welcome Screen** (`Welcome.java`): Campus splash screen with "Let's begin" button
2. **Location Selection** (`Activity.java`): Simple dropdown menus for start/end locations
3. **Route Results** (`Routes.java`): Displays 2 calculated routes with distance and time

**Features:**
- Simple point-to-point routing
- Automatic route calculation using core algorithms
- Clean, minimalist interface with campus imagery
- Results saved to `data/GeneratedRoutes.txt`

### 💻 **Command Line Route Calculation**
```bash
# Edit locations in data/LocationQuery.txt, then run:
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" ReadCSV
```

### 🧪 **Algorithm Testing**
```bash
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" UGNavigateTestSuite
```

## 📊 Interface Comparison

| Feature | Basic GUI | Enhanced GUI |
|---------|-----------|--------------|
| **Ease of Use** | ⭐⭐⭐⭐⭐ Simple | ⭐⭐⭐⭐ Advanced |
| **Algorithm Choice** | ❌ Automatic only | ✅ Full selection |
| **Landmark Routing** | ❌ Not available | ✅ Interactive |
| **Route Analysis** | ⭐⭐ Basic | ⭐⭐⭐⭐⭐ Comprehensive |
| **Export Options** | ❌ File only | ✅ Interactive export |
| **Visual Feedback** | ⭐⭐ Minimal | ⭐⭐⭐⭐⭐ Rich |
| **Customization** | ❌ Limited | ✅ Extensive |

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

### **Enhanced GUI Algorithm Integration**
- **Real-time Algorithm Comparison**: All algorithms run simultaneously for comparison
- **Greedy Route Generation**: Custom implementation in `UGRouteNavigator.java`
- **Dynamic Programming Routes**: Memoization-based optimal substructure solutions
- **Multi-Algorithm Analysis**: Statistical comparison and efficiency scoring
- **Route Rating System**: Mathematical scoring based on distance/time optimization ratios

## 📊 Data Format & Validation

### Campus Data
- **Addresses.csv**: 121×121 adjacency matrix with real distances (km)
- **Distance Validation**: Automatic correction for unrealistic measurements
- **Walking Speed**: 5.0 km/h (normal pace) with smart time calculations

### Route Output & File Generation
- **Enhanced GUI**: Results displayed in comprehensive interface with export options
- **Basic GUI**: Automatic file generation during route calculation
  - **LocationQuery.txt**: Stores selected start/end locations
  - **GeneratedRoutes.txt**: Contains calculated route data for basic GUI display
- **Detailed Analysis**: `data/DetailedRouteAnalysis.txt` (generated by enhanced features)
- **Algorithm Metrics**: Distance, time, efficiency, algorithm used, and star ratings

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

## � User Experience Guide

### 🌟 **Enhanced GUI Walkthrough**

1. **Launch Application**: Run `EnhancedRoutesGUI`
2. **Select Locations**: Choose from 121+ campus locations in dropdown menus
3. **Configure Options**:
   - ✅ Enable "Advanced Algorithms" for comprehensive analysis
   - ✅ Enable "Landmark Routes" to route through specific campus features
   - 🎚️ Adjust "Max Detour" slider (0-2.0 km) for landmark routing flexibility
   - 🔢 Set "Max Routes" (1-10) to control number of results
4. **Choose Algorithm Priority**: Select sorting by Distance, Time, Efficiency, or Multiple Criteria
5. **Find Routes**: Click "🔍 Find Optimal Routes" for comprehensive analysis
6. **Review Results**:
   - 📊 Algorithm summary showing which methods were used
   - ⭐ Star ratings (1-5 stars) for route quality assessment
   - 💡 Personalized recommendations for best overall, fastest, and shortest routes
7. **Export Analysis**: Save detailed report using "💾 Export Results"

### 🎨 **Basic GUI Walkthrough**

1. **Welcome Screen**: Click "Let's begin" to start
2. **Location Selection**: 
   - Choose start location from dropdown (121+ options)
   - Choose destination from dropdown
   - Click "Search" to calculate routes
3. **View Results**:
   - See 2 optimized route options
   - View distance (km) and time (minutes) for each route
   - Use "Try Again" to select different locations
   - Use "Exit" to close application

## �🔧 Customization & Extension

### Adding New Locations
1. Update `Scrapper/Addresses.csv` with new location and distances
2. Both GUI interfaces automatically load all locations from CSV header row
3. Enhanced GUI provides immediate access to new locations in dropdowns

### Algorithm Tuning
- **Walking Speed**: Modify `average_walking_speed` in `ReadCSV.java`
- **Distance Validation**: Adjust `MAX_REASONABLE_CAMPUS_DISTANCE`
- **Algorithm Parameters**: Tune heuristics in individual algorithm files
- **Enhanced GUI Settings**: Modify default values in `EnhancedRoutesGUI.java` constructor

### Interface Customization
- **Enhanced GUI**: Modify colors, fonts, and layout in `EnhancedRoutesGUI.java`
- **Basic GUI**: Update campus images in `images/` directory
- **Rating System**: Adjust star rating calculation in `calculateRouteRating()` method

## 🤝 Contributors

**DCIT 204 Group Project - University of Ghana**
- Advanced algorithm implementation and optimization
- Dual-interface design (Basic + Enhanced GUI)
- Campus data collection and validation
- Comprehensive user experience design
- Real-time route analysis and rating systems

## 📄 License

Educational and research use. University of Ghana Computer Science Department.

---

**🎯 Navigate the University of Ghana campus with confidence using advanced computer science algorithms!**

*Choose your experience: Simple and fast with the Basic GUI, or comprehensive and feature-rich with the Enhanced GUI.*
