package view;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.data.ChatObject;
import model.login.Server;
import util.ActionID;
import util.ActionIDCarrier;
import util.DataSupplier;
import util.Performable;
import util.UnsupportedActionException;

/**
 * This class represents the GUI for a chat client.
 * @author Albin Bramstång
 */
public class ChatPanel extends Observable implements ActionListener, Performable<ChatObject>, DataSupplier<ChatObject>,MouseListener {

	private JPanel panel = new JPanel();
	private JTextArea textArea = new JTextArea();
	private JScrollPane scrollPane;
	private JTextField textField = new JTextField();
	private Server server;
	private List<ActionIDCarrier> canSupply = new LinkedList<ActionIDCarrier>();
	private ArrayList<ActionIDCarrier> canPerform = new ArrayList<ActionIDCarrier>();
	
	public ChatPanel(Server s, Observer o,int id){
		addObserver(o);
		server = s;
		
		textField.addActionListener(this);
		textArea.addMouseListener(this);
		textArea.setEditable(false);
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scrollPane = new JScrollPane(textArea);
		
		panel.setLayout(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(textField, BorderLayout.SOUTH);

		canSupply.add(new ActionIDCarrier(ActionID.SEND_CHAT));
		canPerform.add(new ActionIDCarrier(ActionID.CHAT_RECEIVED));

		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.NEW_PERFOMER_INSTANTIATED));

	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public JTextField getChatTextField() {
		return textField;
	}
	
	public void remove(){
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.SUPPLIER_DELETED));
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.PERFORMER_DELETED));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.NEW_SUPPLIER_INSTANTIATED));
		if (e.getSource() == textField) {
			if (!textField.getText().equals("")) {
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.SEND_CHAT));
				textField.setText("");
			}
		}
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.SUPPLIER_DELETED));
	}
	
	@Override
	public List<ActionIDCarrier> canPerform() {
		return canPerform;
	}
	
	@Override
	public void perform(ActionID actionToPerform, ChatObject requiredData) {
		if(actionToPerform == ActionID.CHAT_RECEIVED){
			if(requiredData!=null){
				if(server.equals(requiredData.getServer())){
					textArea.append(requiredData.getMessage()+ "\n");
				}				
				if(!scrollIsAtBottom()){
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
				}
			}
		}
	}
	
	/**
	 * Calculates if the scrollbar is at the bottom.
	 * @return true if at bottom
	 */
	private boolean scrollIsAtBottom(){
		Adjustable sb = scrollPane.getVerticalScrollBar();

	    int val = sb.getValue();
	    int lowest = val + sb.getVisibleAmount();
	    int maxVal = sb.getMaximum();

	    boolean atBottom = (maxVal == lowest);
	    return atBottom;
	}

	@Override
	public List<ActionIDCarrier> canSupply() {
		return canSupply;
	}

	@Override
	public ChatObject getData(ActionID action) throws UnsupportedActionException {
		return new ChatObject(server, textField.getText());
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		textField.requestFocus();
	}
	
	@Override
	public int getPerformIDNumber(ActionIDCarrier action) {
		return canPerform.get(canPerform().indexOf(action)).getIDNumber();
	}
	
	@Override
	public int getSupplyIDNumber(ActionIDCarrier action) {
		return canSupply().get(canSupply().indexOf(action)).getIDNumber();
	}
}
