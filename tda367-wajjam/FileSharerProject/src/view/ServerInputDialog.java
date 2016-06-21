package view;

import java.awt.event.*;
import javax.swing.*;

import model.login.*;
import model.state.ApplicationState;
import util.*;

import java.awt.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * This class is used to get input from the user
 * @author Albin Bramstång
 */
public class ServerInputDialog extends Observable implements ActionListener, Performable<Server>, DataSupplier<Server> {

	private JFrame mainFrame = new JFrame();
	private JPanel mainPanel = new JPanel();
	private JPanel centralPanel = new JPanel();
	private JPanel messagePanel = new JPanel();
	private JPanel leftPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JLabel hostLabel = new JLabel("Host: ", SwingConstants.RIGHT);
	private JLabel portLabel = new JLabel("Port: ", SwingConstants.RIGHT);
	private JTextField hostTextField = new JTextField(20);
	private JTextField portTextField = new JTextField(5);
	private JButton cancelButton = new JButton("Cancel");
	private JButton confirmButton = new JButton("OK");
	private String host;
	private int port;
	private List<ActionIDCarrier> canPerform = new LinkedList<ActionIDCarrier>();
	private List<ActionIDCarrier> canSupply = new LinkedList<ActionIDCarrier>();
	private JLabel errorLabel = new JLabel();
	
	public ServerInputDialog(Observer o) {
		this.addObserver(o);
		
		leftPanel.setLayout(new GridLayout(2, 1));
		leftPanel.add(hostLabel);
		leftPanel.add(portLabel);
		
		rightPanel.setLayout(new GridLayout(2, 1));
		rightPanel.add(hostTextField);
		rightPanel.add(portTextField);
		
		centralPanel.setLayout(new BorderLayout());
		centralPanel.add(leftPanel, BorderLayout.WEST);
		centralPanel.add(rightPanel, BorderLayout.CENTER);
		centralPanel.setPreferredSize(new Dimension(195, 40));
		centralPanel.setMaximumSize(new Dimension(195, 40));
		centralPanel.setMinimumSize(new Dimension(195, 40));
		
		confirmButton.addActionListener(this);
		cancelButton.addActionListener(this);
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);
		
		messagePanel.add(errorLabel);
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		mainPanel.add(centralPanel);
		mainPanel.add(messagePanel);
		mainPanel.add(buttonPanel);
		
		hostTextField.addActionListener(this);
		portTextField.addActionListener(this);
		
		mainFrame.add(mainPanel);
		mainFrame.setTitle("Enter host and port");
		mainFrame.setIconImage(new ImageIcon("dc.png").getImage());
		mainFrame.setResizable(false);
		mainFrame.setPreferredSize(new Dimension(270, 130));
		mainFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		
		canPerform.add(new ActionIDCarrier(ActionID.SHOW_SERVER_INPUT_WINDOW));
		
		canSupply.add(new ActionIDCarrier(ActionID.ADD_SERVER));
		
		this.setChanged();
		this.notifyObservers(new ActionIDCarrier(ActionID.NEW_PERFOMER_INSTANTIATED));
		this.setChanged();
		this.notifyObservers(new ActionIDCarrier(ActionID.NEW_SUPPLIER_INSTANTIATED));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean commit = true;
		
		if (e.getSource() == confirmButton||e.getSource() == hostTextField|| e.getSource()==portTextField) {
			host = hostTextField.getText();
			errorLabel.setForeground(Color.red);
			try {
				port = Integer.parseInt(portTextField.getText());
				if (0 <= port && port <= 65535) {
					mainFrame.setVisible(false);
					hostTextField.setText("");
					portTextField.setText("");
					errorLabel.setText("");
				} else {
					commit = false;
					errorLabel.setText("Port has to be an integer between 0 - 65535");
				}
			} catch (Exception ex) {
				commit = false;
				errorLabel.setText("Port has to be an integer between 0 - 65535");
			}
			if(host.toLowerCase().equals("localhost")){
				host="127.0.0.1";
			}else if(host.equals(ApplicationState.getInstance().getUser().getLocalIp())){
				host="127.0.0.1";
			}else if(host.startsWith("127.0.0")){
				host="127.0.0.1";
			}
		} else if (e.getSource() == cancelButton) {
			mainFrame.setVisible(false);
			hostTextField.setText("");
			portTextField.setText("");
			errorLabel.setText("");
			commit = false;
		}

		
		if (commit) {
			this.setChanged();
			this.notifyObservers(new ActionIDCarrier(ActionID.ADD_SERVER));
		}
	}

	@Override
	public List<ActionIDCarrier> canPerform() {
		return canPerform;
	}

	@Override
	public void perform(ActionID actionToPerform, Server requiredData) {
		hostTextField.setText(ApplicationState.getInstance().getLastIPandPort()[0]);
		portTextField.setText(ApplicationState.getInstance().getLastIPandPort()[1]);
		hostTextField.requestFocus();
		mainFrame.setVisible(true);
	}

	@Override
	public List<ActionIDCarrier> canSupply() {
		return canSupply;
	}

	@Override
	public Server getData(ActionID action) throws UnsupportedActionException {
		return new Server(host, port);
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
