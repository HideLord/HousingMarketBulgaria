package housing.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import housing.logic.House;

public class SearchPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JComboBox<String> m_cityCombo = new JComboBox<>();
	private JComboBox<String> m_neighborCombo = new JComboBox<>();
	private JComboBox<Integer> m_floorCombo = new JComboBox<>();
	
	private List<ActionListener> m_listeners = new ArrayList<>();
	
	public static class Criteria implements Cloneable {
		private String m_city;
		private String m_neighborhood;
		private Integer m_floor;
		
		public boolean matchCity(String city) {
			if(m_city == null)
				return true;
			return m_city.equals(city);
		}
		
		public boolean matchNeighborhood(String _neighborhood) {
			if(m_neighborhood == null)
				return true;
			return m_neighborhood.equals(_neighborhood);
		}
		
		public boolean matchFloor(Integer _floor) {
			if(m_floor == null)
				return true;
			return m_floor.equals(_floor);
		}
		
		public boolean matchesHouse(House _house) {
			return matchCity(_house.city) &&
				   matchNeighborhood(_house.neighborhood) &&
				   matchFloor(_house.floor);
		}
		
		@Override
		protected Object clone() throws CloneNotSupportedException {
			Criteria thisClone = new Criteria();
			thisClone.m_city = m_city;
			thisClone.m_neighborhood = m_neighborhood;
			thisClone.m_floor = m_floor;
			
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
	private static <T> void resetComboBox(JComboBox<T> _comboBox, Set<T> _values) {
		_comboBox.removeAllItems();

		_comboBox.addItem(null);
		for(T value : _values) {
			_comboBox.addItem(value);
		}
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
		Set<String> cities = new HashSet<>();
		Set<String> neighborhoods = new HashSet<>();
		Set<Integer> floors = new HashSet<>();
		
		for(House house : _houses) {
			cities.add(house.city);
			neighborhoods.add(house.neighborhood);
			if(house.floor != null) { // null means that no floor could be parsed from the listing
				floors.add(house.floor);
			}
		}
		
		resetComboBox(m_cityCombo, cities);
		resetComboBox(m_neighborCombo, neighborhoods);
		resetComboBox(m_floorCombo, floors);
	}
	
	private void onUpdate() {
		for(ActionListener listener : m_listeners) {
			listener.actionPerformed(new ActionEvent(this,0,null));
		}
	}
	private void manageCity(ActionEvent e) {
		m_criteria.m_city = (String)m_cityCombo.getSelectedItem();
		onUpdate();
	}
	private void manageNeighborhood(ActionEvent e) {
		m_criteria.m_neighborhood = (String)m_neighborCombo.getSelectedItem();
		onUpdate();
	}
	private void manageFloor(ActionEvent e) {
		m_criteria.m_floor = (Integer)m_floorCombo.getSelectedItem();
		onUpdate();
	}
	
	/** 
	 * Listens to the combo box.  
	 */
	@Override
    public void actionPerformed(ActionEvent e) {
    	if(m_cityCombo == e.getSource()) {
    		manageCity(e);
    	} else if(m_neighborCombo == e.getSource()) {
    		manageNeighborhood(e);
    	} else if(m_floorCombo == e.getSource()) {
    		manageFloor(e);
    	} else {
    		throw new UnsupportedOperationException("Unknown source detected: " + e.getSource().toString());
    	}
    }
	
	public void addActionListener(ActionListener _listener) {
		m_listeners.add(_listener);
	}

	SearchPanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		m_cityCombo.addActionListener(this);
		m_neighborCombo.addActionListener(this);
		m_floorCombo.addActionListener(this);
		
		add(m_cityCombo);
		add(m_neighborCombo);
		add(m_floorCombo);
	}
}
