package view;

import javax.swing.*;

import model.login.Server;
import model.state.ApplicationState;
import util.ActionID;
import util.ActionIDCarrier;
import util.Performable;
import java.awt.event.*;
import java.util.*;

/**
 * This class represents a toolbar for the File Sharer application.
 * @author Albin Bramstång
 */

public class ToolBar extends Observable implements ActionListener, Performable<Object> {

	private JToolBar toolBar = new JToolBar();
	private JButton addServerButton = new JButton();
	private JButton toggleChatSharedButton = new JButton();
	private JButton toggleConnectDisconnectButton = new JButton();
	private JButton settingsButton = new JButton();
	private Server s = null;
	private MediaButtonsPanel mediaButtonsPanel;
	private List<ActionIDCarrier> canPerform = new ArrayList<ActionIDCarrier>();
	private boolean chatVisible = false;
	private boolean connected = false;

	public ToolBar(Observer o) {
		this.addObserver(o);

		toolBar.setFloatable(false);

		mediaButtonsPanel = new MediaButtonsPanel(o);

		addServerButton.setToolTipText("Add server");
		addServerButton.setIcon(new ImageIcon("icons/add.png"));
		addServerButton.addActionListener(this);
		toolBar.add(addServerButton);
		
		toggleConnectDisconnectButton.setToolTipText("Connect");
		toggleConnectDisconnectButton.setIcon(new ImageIcon("icons/connect.png"));
		toggleConnectDisconnectButton.addActionListener(this);
		toggleConnectDisconnectButton.setEnabled(false);
		toolBar.add(toggleConnectDisconnectButton);
		toolBar.addSeparator();

		toolBar.add(mediaButtonsPanel.getStartDownloadButton());
		toolBar.add(mediaButtonsPanel.getStopDownloadButton());
		toolBar.add(mediaButtonsPanel.getDeleteDownloadButton());
		toolBar.addSeparator();
		
		toggleChatSharedButton.setToolTipText("View chat");
		toggleChatSharedButton.setIcon(new ImageIcon("icons/chat.png"));
		toggleChatSharedButton.addActionListener(this);
		toggleChatSharedButton.setEnabled(false);
		toolBar.add(toggleChatSharedButton);
		toolBar.addSeparator();
		
		settingsButton.setToolTipText("Settings");
		settingsButton.setIcon(new ImageIcon("icons/configure.png"));
		settingsButton.addActionListener(this);
		toolBar.add(settingsButton);

		canPerform.add(new ActionIDCarrier(ActionID.UPDATE_SELECTED_SERVER_NAME));
		canPerform.add(new ActionIDCarrier(ActionID.SERVER_UPDATED));
		canPerform.add(new ActionIDCarrier(ActionID.REMOVE));
		canPerform.add(new ActionIDCarrier(ActionID.IS_CHATVIEW));
		canPerform.add(new ActionIDCarrier(ActionID.VIEW_CHAT));
		canPerform.add(new ActionIDCarrier(ActionID.VIEW_SHARED_CONTENT));
		
		this.setChanged();
		this.notifyObservers(new ActionIDCarrier(ActionID.NEW_PERFOMER_INSTANTIATED));
	}

	/**
	 * Identifies from which component an action was triggered, and initiates
	 * the correct action.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addServerButton) {
			this.setChanged();
			this.notifyObservers(new ActionIDCarrier(ActionID.SHOW_SERVER_INPUT_WINDOW));
		} else if (e.getSource() == toggleChatSharedButton) {
			if (!chatVisible) {
				chatVisible = true;
				toggleChatSharedButton.setToolTipText("View shared content");
				toggleChatSharedButton.setIcon(new ImageIcon("icons/folder.png"));
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.VIEW_CHAT));
			} else {
				chatVisible = false;
				toggleChatSharedButton.setToolTipText("View chat");
				toggleChatSharedButton.setIcon(new ImageIcon("icons/chat.png"));
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.VIEW_SHARED_CONTENT));
			}
		} else if (e.getSource() == toggleConnectDisconnectButton) {
			if (connected) {
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.DISCONNECT));
			} else {
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.CONNECT));
			}
		} else if (e.getSource() == settingsButton) {
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.SHOW_SETTINGS_WINDOW));
		}
	}

	/**
	 * Returns the main graphical component of this class, containing all
	 * subcomponents.
	 * 
	 * @return JToolBar
	 */
	public JToolBar getToolBar() {
		return toolBar;
	}

	@Override
	public List<ActionIDCarrier> canPerform() {
		return canPerform;
	}

	@Override
	public void perform(ActionID actionToPerform, Object requiredData) {
		if (requiredData != null) {
			if (actionToPerform == ActionID.UPDATE_SELECTED_SERVER_NAME) {
				s = (Server)requiredData;
				setToggleConnectDisconnectButton(s.isConnected());	
				toggleChatSharedButton.setEnabled(true);
				toggleConnectDisconnectButton.setEnabled(true);
			} else if (actionToPerform == ActionID.SERVER_UPDATED && requiredData.equals(s)) {	
				if (ApplicationState.getInstance().getServerList().contains(requiredData)) {
					setToggleConnectDisconnectButton(((Server)requiredData).isConnected());
				}
				if (ApplicationState.getInstance().getServerList().size() > 0) {
					toggleChatSharedButton.setEnabled(true);
					toggleConnectDisconnectButton.setEnabled(true);
				}
			} else if (actionToPerform == ActionID.REMOVE) {
				toggleConnectDisconnectButton.setEnabled(false);
				
				if (ApplicationState.getInstance().getServerList().size() == 0) {
					toggleChatSharedButton.setEnabled(false);
				}
			} else if (actionToPerform == ActionID.IS_CHATVIEW) {
				boolean chat = (Boolean)requiredData;
				if (chat) {
					toggleChatSharedButton.setToolTipText("View shared content");
					toggleChatSharedButton.setIcon(new ImageIcon("icons/folder.png"));
					chatVisible = true;
				} else {
					toggleChatSharedButton.setToolTipText("View chat");
					toggleChatSharedButton.setIcon(new ImageIcon("icons/chat.png"));
					chatVisible = false;
				}
			} else if (actionToPerform == ActionID.VIEW_CHAT) {
				chatVisible = true;
				toggleChatSharedButton.setToolTipText("View shared content");
				toggleChatSharedButton.setIcon(new ImageIcon("icons/folder.png"));
			} else if (actionToPerform == ActionID.VIEW_SHARED_CONTENT) {
				chatVisible = false;
				toggleChatSharedButton.setToolTipText("View chat");
				toggleChatSharedButton.setIcon(new ImageIcon("icons/chat.png"));
			}
		}
	}

	@Override
	public int getPerformIDNumber(ActionIDCarrier action) {
		return canPerform.get(canPerform().indexOf(action)).getIDNumber();
	}
	
	private void setToggleConnectDisconnectButton(boolean b) {
		if (b) {
			connected = true;
			toggleConnectDisconnectButton.setToolTipText("Disconnect");
			toggleConnectDisconnectButton.setIcon(new ImageIcon("icons/disconnect.png"));
		} else {
			connected = false;
			toggleConnectDisconnectButton.setToolTipText("Connect");
			toggleConnectDisconnectButton.setIcon(new ImageIcon("icons/connect.png"));
		}
	}
}
