A Java desktop application for finding optimal walking routes between locations on the University of Ghana campus using advanced pathfinding algorithms.

## Features

- Multiple pathfinding algorithms (Dijkstra, A*, Floyd-Warshall, Greedy)
- Route sorting and optimization
- Two user interfaces: Basic GUI and Enhanced GUI
- 121+ campus locations
- Real-time route analysis and rating system

## Project Structure

```
src/                          # Java source code
├── ReadCSV.java             # Core routing engine
├── EnhancedRoutesGUI.java   # Advanced GUI
├── Welcome.java             # Basic GUI entry point
├── Activity.java            # Location selection
├── Routes.java              # Route display
├── Route.java               # Route data model
├── DijkstraAlgorithm.java   # Shortest path implementation
├── AStarAlgorithm.java      # Heuristic pathfinding
├── FloydWarshallAlgorithm.java # All-pairs shortest paths
├── RouteSorter.java         # Quick Sort & Merge Sort
├── LandmarkSearch.java      # Landmark-based routing
└── OptimizationAlgorithms.java # Additional optimization methods

Scrapper/                    # Campus data
├── Addresses.csv           # 121x121 adjacency matrix
└── Addresses.xlsx          # Excel format data

data/                       # Generated files
├── LocationQuery.txt       # Current route query
├── GeneratedRoutes.txt     # Route results
└── DetailedRouteAnalysis.txt # Analysis reports
```

## Quick Start

### Prerequisites
- Java 8+ (JDK/JRE)
- All required JAR files included

### Compilation
```bash
javac -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" src/*.java
```

## Usage

### Enhanced GUI (Recommended)
```bash
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" EnhancedRoutesGUI
```

### Basic GUI
```bash
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" Welcome
```

### Command Line
```bash
java -cp "src:opencsv-5.8.jar:commons-lang3-3.12.0.jar:commons-text-1.10.0.jar" ReadCSV
```

## Data

### Campus Data
- 121×121 adjacency matrix with real distances
- Walking speed: 5.0 km/h with time calculations

### Output Files
- LocationQuery.txt - Selected start/end locations
- GeneratedRoutes.txt - Calculated route data
- DetailedRouteAnalysis.txt - Comprehensive analysis reports

## License

Educational use - University of Ghana Computer Science Department
