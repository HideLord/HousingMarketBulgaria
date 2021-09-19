package housing.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import housing.logic.House;

public class SearchPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private static final String CITY_COMMAND = "C";
	private static final String NEIGHBORHOOD_COMMAND = "N";
	private static final String FLOOR_COMMAND = "F";
	
	private SearchGroup m_cities = new SearchGroup(CITY_COMMAND);
	private SearchGroup m_neighborhoods = new SearchGroup(NEIGHBORHOOD_COMMAND);
	private SearchGroup m_floors = new SearchGroup(FLOOR_COMMAND);
	
	private List<ActionListener> m_listeners = new ArrayList<>();
	
	public static class Criteria implements Cloneable {
		private Set<String> m_city = new HashSet<>();
		private Set<String> m_neighborhood = new HashSet<>();
		private Set<Integer> m_floor = new HashSet<>();
		
		public boolean matchCity(String city) {
			if(m_city.isEmpty())
				return true;
			return m_city.contains(city);
		}
		
		public boolean matchNeighborhood(String _neighborhood) {
			if(m_neighborhood.isEmpty())
				return true;
			return m_neighborhood.contains(_neighborhood);
		}
		
		public boolean matchFloor(Integer _floor) {
			if(m_floor.isEmpty())
				return true;
			return m_floor.contains(_floor);
		}
		
		public boolean matchesHouse(House _house) {
			return matchCity(_house.city) &&
				   matchNeighborhood(_house.neighborhood) &&
				   matchFloor(_house.floor);
		}
		
		@Override
		protected Object clone() throws CloneNotSupportedException {
			Criteria thisClone = new Criteria();
			thisClone.m_city.addAll(m_city);
			thisClone.m_neighborhood.addAll(m_neighborhood);
			thisClone.m_floor.addAll(m_floor);
			
			return thisClone;
		}
	}
	Criteria m_criteria = new Criteria();
	
	public Criteria getCriteria() {
		return m_criteria;
	}
	
	/**
	 * Adds all the values to the comboBox and additionally appends "Match All" to the list.
	 */
	private static <T> void resetPanel(SearchGroup _panel, Set<T> _values) {
		_panel.removeAll();
		for(T val : _values) {
			JCheckBox checkBox = new JCheckBox(val.toString());
			checkBox.addActionListener(_panel);
			_panel.add(checkBox);
			
		}
		_panel.revalidate();
		_panel.repaint();
	}
	
	/**
	 * Creates the search criteria based on the given houses:
	 * <pre>
	 *   City
	 *   Neighborhood
	 *   Floor
	 * </pre>
	 */
	public void reset(List<House> _houses) {
		Set<String> cities = new TreeSet<>();
		Set<String> neighborhoods = new TreeSet<>();
		Set<Integer> floors = new TreeSet<>();
		
		for(House house : _houses) {
			cities.add(house.city);
			neighborhoods.add(house.neighborhood);
			if(house.floor != null) { // null means that no floor could be parsed from the listing
				floors.add(house.floor);
			}
		}
		
		resetPanel(m_cities, cities);
		resetPanel(m_neighborhoods, neighborhoods);
		resetPanel(m_floors, floors);
	}
	
	private void onUpdate() {
		for(ActionListener listener : m_listeners) {
			listener.actionPerformed(new ActionEvent(this,0,null));
		}
	}
	
	private void manageCity(ActionEvent e) {
		JCheckBox chBox = (JCheckBox) e.getSource();
		if(chBox.isSelected()) {
			m_criteria.m_city.add(chBox.getText());
	    } else {
			m_criteria.m_city.remove(chBox.getText());
		}
		onUpdate();
	}
	private void manageNeighborhood(ActionEvent e) {
		JCheckBox chBox = (JCheckBox) e.getSource();
		if(chBox.isSelected()) {
			m_criteria.m_neighborhood.add(chBox.getText());
	    } else {
			m_criteria.m_neighborhood.remove(chBox.getText());
		}
		onUpdate();
	}
	private void manageFloor(ActionEvent e) {
		JCheckBox chBox = (JCheckBox) e.getSource();
		if(chBox.isSelected()) {
			m_criteria.m_floor.add(Integer.valueOf(chBox.getText()));
	    } else {
			m_criteria.m_floor.remove(Integer.valueOf(chBox.getText()));
		}
		onUpdate();
	}
	
	/** 
	 * Listens to the combo box.  
	 */
	@Override
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() instanceof JCheckBox) {
    		switch(e.getActionCommand()) {
    		case CITY_COMMAND:
    			manageCity(e);
    			break;
    		case NEIGHBORHOOD_COMMAND:
    			manageNeighborhood(e);
    			break;
    		case FLOOR_COMMAND:
    			manageFloor(e);
    			break;
    		}
    	} else {
    		throw new UnsupportedOperationException("Unknown source detected: " + e.getSource().toString());
    	}
    }
	
	public void addActionListener(ActionListener _listener) {
		m_listeners.add(_listener);
	}

	SearchPanel() {
		setLayout(new GridBagLayout());
		
		m_cities.addActionListener(this);
		m_neighborhoods.addActionListener(this);
		m_floors.addActionListener(this);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridy = 0;
		gbc.weighty = 0.1;
		
		JScrollPane pane = new JScrollPane(m_cities);
		pane.getVerticalScrollBar().setUnitIncrement(16);
		add(pane, gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 0.7;
		pane = new JScrollPane(m_neighborhoods);
		pane.getVerticalScrollBar().setUnitIncrement(16);
		add(pane, gbc);
		
		gbc.gridy = 2;
		gbc.weighty = 0.2;
		pane = new JScrollPane(m_floors);
		pane.getVerticalScrollBar().setUnitIncrement(16);
		add(pane, gbc);
	}
}
