package gui;

import java.util.List;

import javax.swing.JFrame;

import housing.calc.House;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Resets the search parameters and the graph with the new data.
	 * @param houses
	 */
	public void reset(List<House> houses) {
		
	}
	
	public MainFrame() {
		setTitle("Bulgaria housing");
		setSize(600, 800);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
