package housing.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class SearchGroup extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	String m_command;
	List<ActionListener> m_listeners = new ArrayList<>();
	
	public SearchGroup(String _command) {
		m_command = _command;
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
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
