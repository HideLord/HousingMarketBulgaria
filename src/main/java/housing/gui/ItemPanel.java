package housing.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang3.math.NumberUtils;

import housing.gui.SearchPanel.Criteria;
import housing.logic.Calculator;
import housing.logic.House;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class ItemPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	Criteria m_criteria = new Criteria();
	List<House> m_houses = new ArrayList<>();
	
	/**
	 * Contains the House class and serves as the printed item on the screen
	 */
	public static class HouseButton extends JPanel implements MouseListener {
		private static final long serialVersionUID = 1L;
		private House m_house;
		
		private Color getColor(double percent) {
			final Color[] colors = new Color[] {
				new Color(0x00FF00),
				new Color(0x00A0F0),
				new Color(0x000FFF),
				new Color(0xAF00F0),
				new Color(0xAF00A0),
				new Color(0xFF00A0),
				new Color(0xFF0000)
			};
			if(percent >= 99.9999)
				return colors[colors.length-1];
			
			int numRanges = colors.length - 1;
			int btwIdx = (int) Math.floor(percent * numRanges / 100.0);
			double k = percent / (100.0 / numRanges) - btwIdx;
			
			int red = (int) Math.round(colors[btwIdx].getRed() * (1.0-k) + colors[btwIdx+1].getRed() * k);
			int green = (int) Math.round(colors[btwIdx].getGreen() * (1.0-k) + colors[btwIdx+1].getGreen() * k);
			int blue = (int) Math.round(colors[btwIdx].getBlue() * (1.0-k) + colors[btwIdx+1].getBlue() * k);
			
			return new Color(red,green,blue);
		}
		
		public HouseButton(House _house, int layerNumber, int maxLayer) {
			enableInputMethods(true);   
			addMouseListener(this);
			
			m_house = _house;
			
			JLabel label = new JLabel(String.valueOf(layerNumber));
			label.setForeground(Color.WHITE);
			add(label);
			
			setBackground(getColor(100*((double)layerNumber/(double)maxLayer)));
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
		
		public void openListing() {
			final JFrame frame = new JFrame();
			frame.setSize(1600, 1000);
			final JFXPanel fxpanel = new JFXPanel();
			frame.add(fxpanel);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					WebEngine engine;
					WebView wv = new WebView();
					engine = wv.getEngine();
					fxpanel.setScene(new Scene(wv));
					
					System.out.println("Trying to open: " + m_house.fullListingUrl);
					engine.load(m_house.fullListingUrl);
				}
			});
			frame.setVisible(true);
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
		
		List<House> houses = new ArrayList<House>();
		
		for(int i = 0; i < m_houses.size(); ++i) {
			if(m_criteria.matchesHouse(m_houses.get(i)))
				houses.add(m_houses.get(i));
		}
		
		int[] paretoLayer = Calculator.getParetoLayers(houses);
		int maxLayer = NumberUtils.max(paretoLayer);
		
		for(int i = 0; i < houses.size(); ++i) {
			HouseButton button = new HouseButton(houses.get(i), paretoLayer[i], maxLayer);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = houses.get(i).priceEuro/1000;
			gbc.gridy = houses.get(i).sqM;
			gbc.fill = GridBagConstraints.BOTH;
			add(button, gbc);
		}
		
		revalidate();
		repaint();
	}
	
	public void reset(List<House> _houses) {
		m_houses = _houses;
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
