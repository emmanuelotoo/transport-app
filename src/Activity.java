import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Activity extends JFrame{
	private JLabel startL, stopL, myLabel;
	private JButton searchB;
	private JComboBox<String> stop_locations, start_locations;
	private SearchButtonHandler sbHandler;
	public String SelectedStopLoc, SelectedStartLoc;
	private ImageIcon bgimg;

	public Activity(){

		String[] all_locations = {};
		try (CSVReader reader = new CSVReader(new FileReader("Scrapper/Addresses.csv"))){
			List<String[]> adjacencyMatrix = reader.readAll();
			all_locations = adjacencyMatrix.get(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvException e) {
			e.printStackTrace();
		}

		bgimg = new ImageIcon("images/enoch-appiah-jr-JQltZRIHaMQ-unsplash.jpg");
      	myLabel = new JLabel(bgimg);
      	myLabel.setSize(600, 400);

      	startL = new JLabel("Start: ", SwingConstants.LEFT);
		stopL = new JLabel("Stop: ", SwingConstants.LEFT);

		searchB = new JButton("Search");
		sbHandler = new SearchButtonHandler();
		searchB.addActionListener(sbHandler);

		start_locations = new JComboBox<String>();

		for(int i = 1; i < all_locations.length; i++){
			start_locations.addItem(all_locations[i]);
		}

		

		stop_locations = new JComboBox<String>();

		for(int i = 1; i < all_locations.length; i++){
			stop_locations.addItem(all_locations[i]);
		}


		//Title of window
		setTitle("Main page");

		//Getting container
		Container pane = getContentPane();
		pane.setLayout(null);
		startL.setLocation(90,20);
		startL.setSize(300,60);
		start_locations.setLocation(150,20);
		start_locations.setSize(300,60);
		stopL.setLocation(90,80);
		stopL.setSize(300,70);
		stop_locations.setLocation(150,90);
		stop_locations.setSize(300,60);
		searchB.setLocation(250, 200);
		searchB.setSize(100, 60);

		//placing components
		myLabel.add(startL);
		myLabel.add(start_locations);
		myLabel.add(stopL);
		myLabel.add(stop_locations);
		myLabel.add(searchB);

		pane.add(myLabel);

		//Size and display
		setSize(600, 400);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	private class SearchButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			dispose();
			SelectedStartLoc = (String) start_locations.getSelectedItem();
			SelectedStopLoc = (String) stop_locations.getSelectedItem();
			
			System.out.println("Selected Start Location: " + SelectedStartLoc);
			System.out.println("Selected Stop Location: " + SelectedStopLoc);
			
			try {
				FileWriter myWriter = new FileWriter("data/LocationQuery.txt");
				myWriter.write(SelectedStartLoc + "\n");
				myWriter.write(SelectedStopLoc);
				myWriter.flush();
				myWriter.close();
				System.out.println("LocationQuery.txt file created successfully");
			}	catch (IOException k){
				System.out.println("Error writing to LocationQuery.txt: " + k.getMessage());
				k.printStackTrace();
			}

			String[] n = {""};
			try {
				System.out.println("Calling ReadCSV.main()...");
				ReadCSV.main(n);
				System.out.println("ReadCSV.main() completed successfully");
			} catch (IOException | CsvException ioException) {
				System.out.println("Error in ReadCSV.main(): " + ioException.getMessage());
				ioException.printStackTrace();
			}

			System.out.println("Opening Routes window...");
			new Routes();

		}
	}

	public static void main(String[] args){
		new Activity();
	}


}