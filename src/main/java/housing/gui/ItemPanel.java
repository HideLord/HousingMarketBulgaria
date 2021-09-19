package housing.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import housing.gui.SearchPanel.Criteria;
import housing.logic.House;

public class ItemPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	Criteria m_criteria = new Criteria();
	List<House> m_houses = new ArrayList<>();
	boolean m_isDisplayed[];
	
	List<HouseButton> m_houseButtons = new ArrayList<>();
	
	/**
	 * Contains the House class and serves as the printed item on the screen
	 */
	public static class HouseButton extends JPanel implements MouseListener {
		private static final long serialVersionUID = 1L;
		private House m_house;
		public HouseButton(House _house) {
			enableInputMethods(true);   
			addMouseListener(this);
			
			m_house = _house;
			
			//add(new JLabel(String.valueOf((int)m_house.getPricePerSqM())));
			Random r = new Random();
			setBackground(new Color(Math.abs(r.nextInt()%255),Math.abs(r.nextInt()%255),Math.abs(r.nextInt()%255)));
		}
		
		@Override
		public void paintComponent(Graphics g) {
	        super.paintComponent(g);       

	        // Draw Text
	        //g.drawString("This is my custom Panel!",10,20);
	    } 
		
		public void openListing() {
			JEditorPane jep = new JEditorPane();
			jep.setEditable(false);

			try {
				System.out.println("Trying to open: " + m_house.fullListingUrl);
				jep.setPage(m_house.fullListingUrl);
			} catch (IOException e) {
				jep.setContentType("text/html");
				jep.setText("<html>Listing no longer exists or could not be loaded.</html>");
			}

			JFrame listingFrame = new JFrame("Open listing");
			listingFrame.getContentPane().add(new JScrollPane(jep));
			listingFrame.setSize(1600, 1000);
			listingFrame.setVisible(true);
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			openListing();
		}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}
	
	private void updateDisplayedItems() {
		removeAll();
		for(int i = 0; i < m_houses.size(); ++i) {
			m_isDisplayed[i] = m_criteria.matchesHouse(m_houses.get(i));
			if(m_isDisplayed[i]) {
				HouseButton button = new HouseButton(m_houses.get(i));
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = m_houses.get(i).priceEuro/1000;
				gbc.gridy = m_houses.get(i).sqM;
				add(button, gbc);
			}
		}
		revalidate();
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
		setLayout(new GridBagLayout());
	}
}
