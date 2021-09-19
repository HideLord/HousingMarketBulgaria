package housing.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import housing.gui.SearchPanel.Criteria;
import housing.logic.House;

public class ItemPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	Criteria m_criteria = new Criteria();
	List<House> m_houses = new ArrayList<>();
	boolean m_isDisplayed[];
	
	private void updateDisplayedItems() {
		for(int i = 0; i < m_houses.size(); ++i) {
			m_isDisplayed[i] = m_criteria.matchesHouse(m_houses.get(i));
		}
	}
	
	public void reset(List<House> _houses) {
		m_houses = _houses;
		m_isDisplayed = new boolean[_houses.size()];
		updateDisplayedItems();
	}

	public void updateCriteria(Criteria criteria) throws CloneNotSupportedException {
		m_criteria = (Criteria) criteria.clone();
		updateDisplayedItems();
	}
	
	ItemPanel() {
		setBackground(new Color(255, 0, 0));
	}
}
