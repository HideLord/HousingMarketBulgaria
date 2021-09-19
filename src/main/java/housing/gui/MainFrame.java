package housing.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;

import housing.logic.House;

public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private final SearchPanel m_searchPanel = new SearchPanel();
	private final ItemPanel m_itemPanel = new ItemPanel();

	/**
	 * Resets the search parameters and the graph with the new data.
	 */
	public void reset(List<House> _houses) {
		m_searchPanel.reset(_houses);
		m_itemPanel.reset(_houses);
	}
	
	/**
	 * Sets up the initial layout.
	 */
	private void setUpLayout() {
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;
		
		gbc.weightx = 80.0/getWidth();
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(m_searchPanel, gbc);

		gbc.weightx = 1.0-80.0/getWidth();
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(m_itemPanel, gbc);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(m_searchPanel == e.getSource()) {
			try {
				m_itemPanel.updateCriteria(((SearchPanel)e.getSource()).getCriteria());
			} catch (Exception ex) {
				throw new UnsupportedOperationException("Unsupported action: " + ex.getMessage());
			}
			return;
		}

    	throw new UnsupportedOperationException("Unknown source detected: " + e.getSource().toString());
	}
	
	public MainFrame() {
		setTitle("Bulgaria housing");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		setUpLayout();
		m_searchPanel.addActionListener(this);
	}

	
}
