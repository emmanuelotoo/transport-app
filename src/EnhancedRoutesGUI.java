import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Enhanced GUI for UG Navigate with comprehensive algorithm selection,
 * landmark search, and sorting capabilities
 */
public class EnhancedRoutesGUI extends JFrame {
    
    // GUI Components
    private JComboBox<String> startLocationCombo;
    private JComboBox<String> endLocationCombo;
    private JComboBox<String> landmarkCombo;
    private JComboBox<String> sortCriteriaCombo;
    private JCheckBox useAdvancedAlgorithms;
    private JCheckBox useLandmarkSearch;
    private JSpinner maxRoutesSpinner;
    private JSlider maxDetourSlider;
    
    private JTextArea resultsArea;
    private JScrollPane resultsScrollPane;
    private JButton findRoutesButton;
    private JButton clearButton;
    private JButton exportButton;
    
    private UGRouteNavigator navigator;
    private UGRouteNavigator.RouteResults lastResults;
    
    // Campus locations for dropdown
    private String[] campusLocations = {
        "Department of Computer Science",
        "Balme Library",
        "University of Ghana Sports Stadium",
        "Commonwealth Hall",
        "Legon Hall",
        "School of Engineering",
        "Faculty of Arts",
        "University Hospital",
        "Night Market",
        "Volta Hall",
        "Akuafo Hall",
        "School of Business",
        "Central Mosque",
        "UG Banking Square",
        "Bush Canteen",
        "Athletic Oval"
    };
    
    public EnhancedRoutesGUI() {
        navigator = new UGRouteNavigator();
        initializeComponents();
        layoutComponents();
        addEventListeners();
        setFrameProperties();
    }
    
    private void initializeComponents() {
        // Location selection
        startLocationCombo = new JComboBox<>(campusLocations);
        endLocationCombo = new JComboBox<>(campusLocations);
        endLocationCombo.setSelectedIndex(1); // Default to different location
        
        // Landmark selection
        String[] landmarks = {"None", "Bank", "Hospital", "Library", "Sports", "Dining", "Residence", "Academic"};
        landmarkCombo = new JComboBox<>(landmarks);
        
        // Sort criteria
        String[] sortOptions = {"Distance", "Time", "Efficiency", "Multiple Criteria"};
        sortCriteriaCombo = new JComboBox<>(sortOptions);
        
        // Options
        useAdvancedAlgorithms = new JCheckBox("Use Advanced Algorithms", true);
        useLandmarkSearch = new JCheckBox("Include Landmark Routes", false);
        
        // Max routes spinner
        maxRoutesSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        
        // Max detour slider
        maxDetourSlider = new JSlider(0, 200, 100); // 0 to 2.0 km, default 1.0 km
        maxDetourSlider.setMajorTickSpacing(50);
        maxDetourSlider.setMinorTickSpacing(25);
        maxDetourSlider.setPaintTicks(true);
        maxDetourSlider.setPaintLabels(true);
        
        // Results area
        resultsArea = new JTextArea(25, 80);
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        resultsArea.setBackground(new Color(248, 249, 250));
        resultsScrollPane = new JScrollPane(resultsArea);
        resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Buttons
        findRoutesButton = new JButton("🔍 Find Optimal Routes");
        findRoutesButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        findRoutesButton.setBackground(new Color(40, 167, 69));
        findRoutesButton.setForeground(Color.WHITE);
        
        clearButton = new JButton("🗑️ Clear Results");
        clearButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        clearButton.setBackground(new Color(108, 117, 125));
        clearButton.setForeground(Color.WHITE);
        
        exportButton = new JButton("💾 Export Results");
        exportButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        exportButton.setBackground(new Color(23, 162, 184));
        exportButton.setForeground(Color.WHITE);
        exportButton.setEnabled(false);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top panel for route selection
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "🏛️ UG Navigate - Route Planning"));
        topPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Row 1: Start and End locations
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("From:"), gbc);
        gbc.gridx = 1;
        topPanel.add(startLocationCombo, gbc);
        
        gbc.gridx = 2;
        topPanel.add(new JLabel("To:"), gbc);
        gbc.gridx = 3;
        topPanel.add(endLocationCombo, gbc);
        
        // Row 2: Landmark and Sort criteria
        gbc.gridx = 0; gbc.gridy = 1;
        topPanel.add(new JLabel("Via Landmark:"), gbc);
        gbc.gridx = 1;
        topPanel.add(landmarkCombo, gbc);
        
        gbc.gridx = 2;
        topPanel.add(new JLabel("Sort by:"), gbc);
        gbc.gridx = 3;
        topPanel.add(sortCriteriaCombo, gbc);
        
        // Row 3: Options
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        topPanel.add(useAdvancedAlgorithms, gbc);
        
        gbc.gridx = 2;
        topPanel.add(useLandmarkSearch, gbc);
        
        // Row 4: Max routes and detour distance
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        topPanel.add(new JLabel("Max Routes:"), gbc);
        gbc.gridx = 1;
        topPanel.add(maxRoutesSpinner, gbc);
        
        gbc.gridx = 2;
        topPanel.add(new JLabel("Max Detour (km):"), gbc);
        gbc.gridx = 3;
        topPanel.add(maxDetourSlider, gbc);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel for results
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("📊 Route Analysis Results"));
        centerPanel.add(resultsScrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(248, 249, 250));
        bottomPanel.add(findRoutesButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(exportButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void addEventListeners() {
        findRoutesButton.addActionListener(e -> findOptimalRoutes());
        clearButton.addActionListener(e -> clearResults());
        exportButton.addActionListener(e -> exportResults());
        
        // Enable/disable landmark options based on checkbox
        useLandmarkSearch.addActionListener(e -> {
            landmarkCombo.setEnabled(useLandmarkSearch.isSelected());
            maxDetourSlider.setEnabled(useLandmarkSearch.isSelected());
        });
        
        // Update detour label
        maxDetourSlider.addChangeListener(e -> {
            double detourKm = maxDetourSlider.getValue() / 100.0;
            maxDetourSlider.setToolTipText(String.format("%.1f km", detourKm));
        });
        
        // Initial state
        landmarkCombo.setEnabled(false);
        maxDetourSlider.setEnabled(false);
    }
    
    private void setFrameProperties() {
        setTitle("🏛️ UG Navigate - Advanced Campus Route Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Set icon (if available)
        try {
            ImageIcon icon = new ImageIcon("images/ug.jpg");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            // Icon not found, continue without it
        }
    }
    
    private void findOptimalRoutes() {
        try {
            findRoutesButton.setEnabled(false);
            findRoutesButton.setText("🔄 Finding Routes...");
            resultsArea.setText("🔍 Analyzing optimal routes...\n\n");
            
            SwingUtilities.invokeLater(() -> {
                try {
                    // Get user inputs
                    String startLocation = (String) startLocationCombo.getSelectedItem();
                    String endLocation = (String) endLocationCombo.getSelectedItem();
                    
                    if (startLocation.equals(endLocation)) {
                        showError("Start and end locations must be different!");
                        return;
                    }
                    
                    // Set up preferences
                    UGRouteNavigator.RoutePreferences preferences = new UGRouteNavigator.RoutePreferences();
                    preferences.sortCriteria = ((String) sortCriteriaCombo.getSelectedItem()).toLowerCase();
                    preferences.maxRoutes = (Integer) maxRoutesSpinner.getValue();
                    preferences.useOptimizationMethods = useAdvancedAlgorithms.isSelected();
                    
                    if (useLandmarkSearch.isSelected() && !landmarkCombo.getSelectedItem().equals("None")) {
                        preferences.landmark = (String) landmarkCombo.getSelectedItem();
                        preferences.maxDetourDistance = maxDetourSlider.getValue() / 100.0;
                    }
                    
                    // Find routes
                    lastResults = navigator.findOptimalRoutes(startLocation, endLocation, preferences);
                    
                    // Display results
                    displayResults(lastResults, startLocation, endLocation);
                    
                    exportButton.setEnabled(!lastResults.routes.isEmpty());
                    
                } catch (Exception ex) {
                    showError("Error finding routes: " + ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    findRoutesButton.setEnabled(true);
                    findRoutesButton.setText("🔍 Find Optimal Routes");
                }
            });
            
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            findRoutesButton.setEnabled(true);
            findRoutesButton.setText("🔍 Find Optimal Routes");
        }
    }
    
    private void displayResults(UGRouteNavigator.RouteResults results, String start, String end) {
        StringBuilder display = new StringBuilder();
        
        display.append("🏛️  UG NAVIGATE - COMPREHENSIVE ROUTE ANALYSIS\n");
        display.append("================================================\n");
        display.append(String.format("From: %s\n", start));
        display.append(String.format("To: %s\n", end));
        display.append(String.format("Analysis Date: %s\n\n", new Date()));
        
        if (results.routes.isEmpty()) {
            display.append("❌ No routes found!\n");
            display.append("Try adjusting your preferences or selecting different locations.\n");
        } else {
            // Algorithms summary
            display.append("📈 ALGORITHMS USED:\n");
            results.algorithmsSummary.forEach((algorithm, count) -> 
                display.append(String.format("   • %s: %d route(s)\n", algorithm, count)));
            
            display.append(String.format("\n🛣️  TOP %d ROUTE OPTIONS:\n", results.routes.size()));
            display.append("=======================\n");
            
            for (int i = 0; i < results.routes.size(); i++) {
                Route route = results.routes.get(i);
                display.append(String.format("\n%d. %s\n", i + 1, route.full_path));
                display.append(String.format("   Algorithm: %s\n", 
                              route.algorithmUsed != null ? route.algorithmUsed : "Standard"));
                display.append(String.format("   Distance: %.3f km\n", route.distance));
                display.append(String.format("   Time: %.1f minutes\n", route.time_taken));
                display.append(String.format("   Efficiency: %.2f km/min\n", 
                              route.distance / Math.max(route.time_taken, 0.1)));
                
                // Add visual rating
                double rating = calculateRouteRating(route, results.routes);
                display.append(String.format("   Rating: %s (%.1f/5.0)\n", 
                              getStarRating(rating), rating));
            }
            
            display.append(String.format("\n✅ Analysis Complete - %d total routes evaluated\n", 
                          results.totalRoutesFound));
            
            // Add recommendations
            display.append("\n💡 RECOMMENDATIONS:\n");
            display.append("===================\n");
            if (!results.routes.isEmpty()) {
                Route bestRoute = results.routes.get(0);
                display.append(String.format("🥇 Best Overall: Route 1 (%s)\n", bestRoute.algorithmUsed));
                
                Route fastestRoute = results.routes.stream()
                    .min((r1, r2) -> Double.compare(r1.time_taken, r2.time_taken))
                    .orElse(bestRoute);
                
                if (fastestRoute != bestRoute) {
                    int fastestIndex = results.routes.indexOf(fastestRoute) + 1;
                    display.append(String.format("⚡ Fastest: Route %d (%.1f mins)\n", 
                                  fastestIndex, fastestRoute.time_taken));
                }
                
                Route shortestRoute = results.routes.stream()
                    .min((r1, r2) -> Double.compare(r1.distance, r2.distance))
                    .orElse(bestRoute);
                
                if (shortestRoute != bestRoute && shortestRoute != fastestRoute) {
                    int shortestIndex = results.routes.indexOf(shortestRoute) + 1;
                    display.append(String.format("📏 Shortest: Route %d (%.3f km)\n", 
                                  shortestIndex, shortestRoute.distance));
                }
            }
        }
        
        resultsArea.setText(display.toString());
        resultsArea.setCaretPosition(0); // Scroll to top
    }
    
    private double calculateRouteRating(Route route, List<Route> allRoutes) {
        // Calculate rating based on distance and time relative to other routes
        double minDistance = allRoutes.stream().mapToDouble(r -> r.distance).min().orElse(1.0);
        double maxDistance = allRoutes.stream().mapToDouble(r -> r.distance).max().orElse(1.0);
        double minTime = allRoutes.stream().mapToDouble(r -> r.time_taken).min().orElse(1.0);
        double maxTime = allRoutes.stream().mapToDouble(r -> r.time_taken).max().orElse(1.0);
        
        double distanceScore = maxDistance > minDistance ? 
            1.0 - ((route.distance - minDistance) / (maxDistance - minDistance)) : 1.0;
        double timeScore = maxTime > minTime ? 
            1.0 - ((route.time_taken - minTime) / (maxTime - minTime)) : 1.0;
        
        return Math.max(1.0, Math.min(5.0, (distanceScore + timeScore) * 2.5));
    }
    
    private String getStarRating(double rating) {
        int stars = (int) Math.round(rating);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stars; i++) {
            sb.append("⭐");
        }
        for (int i = stars; i < 5; i++) {
            sb.append("☆");
        }
        return sb.toString();
    }
    
    private void clearResults() {
        resultsArea.setText("");
        exportButton.setEnabled(false);
        lastResults = null;
    }
    
    private void exportResults() {
        if (lastResults == null || lastResults.routes.isEmpty()) {
            showError("No results to export!");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Route Analysis");
        fileChooser.setSelectedFile(new File("UG_Route_Analysis.txt"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                try (PrintWriter writer = new PrintWriter(file)) {
                    writer.println(resultsArea.getText());
                }
                JOptionPane.showMessageDialog(this, 
                    "Results exported successfully to " + file.getName(),
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                showError("Error exporting results: " + ex.getMessage());
            }
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        resultsArea.setText("❌ Error: " + message + "\n");
    }
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel
        }
        
        SwingUtilities.invokeLater(() -> {
            new EnhancedRoutesGUI().setVisible(true);
        });
    }
}
