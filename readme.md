# Transportation App

A Java desktop application for finding the shortest and alternative walking routes between locations on the University of Ghana campus. The app uses real campus data and provides accurate distance and time estimates for each route.

## Features
- Find shortest and alternative walking routes between any two campus locations
- Accurate distance and time calculations based on real data
- Modern, easy-to-use graphical interface (Java Swing)
- Visual route display with campus images
- CSV-based data for easy updates and customization

## How It Works
- Select your start and destination locations from dropdown menus
- The app calculates the shortest and alternative routes using campus adjacency data
- Each route displays the path, total distance, and estimated walking time

## Project Structure
```
Transportation_App/
├── src/                  # Java source code
│   ├── Activity.java     # Main UI for selecting locations
│   ├── Welcome.java      # Welcome screen
│   ├── ReadCSV.java      # Route calculation logic
│   ├── Route.java        # Route data model
│   ├── Routes.java       # Route results display
│   └── ...
├── Scrapper/             # Campus data files
│   ├── Addresses.csv     # Adjacency matrix of campus locations
│   └── ...
├── data/                 # App-generated data
│   ├── LocationQuery.txt # Last queried locations
│   ├── GeneratedRoutes.txt # Last generated routes
├── images/               # Campus images for UI backgrounds
├── readme.md             # This file
└── ...
```

## Setup & Running
1. **Requirements:**
   - Java 8 or newer
   - OpenCSV library (included as JAR)
2. **Compile:**
   - Open a terminal in the project folder
   - Run:
     ```
     javac -cp "opencsv-5.8.jar;commons-lang3-3.12.0.jar;commons-text-1.10.0.jar;src;." src/*.java
     ```
3. **Run:**
   - Start the app with:
     ```
     java -cp "opencsv-5.8.jar;commons-lang3-3.12.0.jar;commons-text-1.10.0.jar;src;." Welcome
     ```

## Data Format
- **Addresses.csv**: Adjacency matrix where each cell is the distance (in km) between two locations
- **LocationQuery.txt**: Stores the last start and destination selected
- **GeneratedRoutes.txt**: Stores the last calculated routes

## Customization
- Update `Scrapper/Addresses.csv` to add or edit campus locations and distances
- Add new images to the `images/` folder for custom backgrounds



## License
This project is for educational and research purposes. Contact the author for other uses.

---

Enjoy finding your way around campus with the Transportation App!
