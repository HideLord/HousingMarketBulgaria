package housing.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SearchGroup extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	int currY = 0;
	
	String m_command;
	List<ActionListener> m_listeners = new ArrayList<>();
	
	JPanel m_checkboxHolder = new JPanel();
	
	private Component leftJustify( JLabel panel )  {
	    Box  b = Box.createHorizontalBox();
	    b.add( panel );
	    b.add( Box.createHorizontalGlue() );
	    return b;
	}
	
	public SearchGroup(String _command, JLabel _label) {
		m_command = _command;
		setAlignmentX(LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		m_checkboxHolder.setLayout(new BoxLayout(m_checkboxHolder, BoxLayout.Y_AXIS));
		
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(leftJustify(_label));
		add(Box.createRigidArea(new Dimension(0, 5)));
		
		JScrollPane pane = new JScrollPane(m_checkboxHolder);
		pane.getVerticalScrollBar().setUnitIncrement(16);
		add(pane);
	}
	
	public void add(JCheckBox _checkBox) {
		m_checkboxHolder.add(_checkBox);
		m_checkboxHolder.revalidate();
		m_checkboxHolder.repaint();
		
		_checkBox.addActionListener(this);
	}
	
	void removeAllCheckBoxes() {
		m_checkboxHolder.removeAll();
		m_checkboxHolder.revalidate();
		m_checkboxHolder.repaint();
	}
	
	void addActionListener(ActionListener _listener) {
		m_listeners.add(_listener);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ActionEvent event = new ActionEvent(e.getSource(), 0, m_command);
		
		for(ActionListener listener : m_listeners) {
			listener.actionPerformed(event);
		}
	}
}
