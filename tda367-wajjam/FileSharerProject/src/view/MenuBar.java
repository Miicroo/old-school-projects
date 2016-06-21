package view;

import javax.swing.*;
import model.login.Server;
import model.state.ApplicationState;
import model.traffic.DataDirection;
import model.traffic.DownloadStatus;
import model.traffic.TrafficObject;
import util.ActionID;
import util.ActionIDCarrier;
import util.Performable;
import java.util.*;
import java.awt.event.*;

/**
 * This class represents a menu bar with all available options.
 * 
 * @author Albin Bramstång
 */
public class MenuBar extends Observable implements ActionListener,
		Performable<Object> {
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");
	private JMenu helpMenu = new JMenu("Help");
	private JMenuItem exitMenuItem = new JMenuItem("Exit");
	private JMenuItem aboutMenuItem = new JMenuItem("About...");
	private JMenuItem settingsMenuItem = new JMenuItem("Settings");
	private JMenuItem connectToServerMenuItem = new JMenuItem("Connect");
	private JMenuItem disconnectFromServerMenuItem = new JMenuItem("Disconnect");
	private JMenuItem addServerMenuItem = new JMenuItem("Add server...");
	private JMenuItem startMenuItem = new JMenuItem("Start");
	private JMenuItem stopMenuItem = new JMenuItem("Stop");
	private JMenuItem deleteMenuItem = new JMenuItem("Delete");
	private JMenuItem viewSharedContentMenuItem = new JMenuItem(
			"View shared content");
	private JMenuItem viewChatMenuItem = new JMenuItem("View chat");
	private Server server;
	private List<ActionIDCarrier> canPerform = new LinkedList<ActionIDCarrier>();
	private boolean chatVisible = false;

	public MenuBar(Observer o) {
		this.addObserver(o);
		
		canPerform.add(new ActionIDCarrier(ActionID.UPDATE_SELECTED_SERVER_NAME));
		canPerform.add(new ActionIDCarrier(ActionID.SERVER_UPDATED));
		canPerform.add(new ActionIDCarrier(ActionID.FILE_SELECTED));
		canPerform.add(new ActionIDCarrier(ActionID.STATUS_UPDATED));
		canPerform.add(new ActionIDCarrier(ActionID.DELETE_DOWNLOAD));
		canPerform.add(new ActionIDCarrier(ActionID.REMOVE));
		canPerform.add(new ActionIDCarrier(ActionID.IS_CHATVIEW));
		canPerform.add(new ActionIDCarrier(ActionID.VIEW_CHAT));
		canPerform.add(new ActionIDCarrier(ActionID.VIEW_SHARED_CONTENT));
		
		exitMenuItem.addActionListener(this);
		aboutMenuItem.addActionListener(this);
		settingsMenuItem.addActionListener(this);
		connectToServerMenuItem.addActionListener(this);
		disconnectFromServerMenuItem.addActionListener(this);
		addServerMenuItem.addActionListener(this);
		startMenuItem.addActionListener(this);
		stopMenuItem.addActionListener(this);
		deleteMenuItem.addActionListener(this);
		viewSharedContentMenuItem.addActionListener(this);
		viewChatMenuItem.addActionListener(this);

		connectToServerMenuItem.setEnabled(false);
		disconnectFromServerMenuItem.setEnabled(false);
		viewSharedContentMenuItem.setEnabled(false);
		viewChatMenuItem.setEnabled(false);
		startMenuItem.setEnabled(false);
		stopMenuItem.setEnabled(false);
		deleteMenuItem.setEnabled(false);

		fileMenu.add(addServerMenuItem);
		fileMenu.add(connectToServerMenuItem);
		fileMenu.add(disconnectFromServerMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(startMenuItem);
		fileMenu.add(stopMenuItem);
		fileMenu.add(deleteMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(viewSharedContentMenuItem);
		fileMenu.add(viewChatMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(settingsMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		helpMenu.add(aboutMenuItem);

		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.NEW_PERFOMER_INSTANTIATED));
	}

	/**
	 * Returns the main graphical component of this class, containing all
	 * subcomponents.
	 * 
	 * @return JMenuBar
	 */
	public JMenuBar getMenuBar() {
		return menuBar;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exitMenuItem) {
			System.exit(0);
		} else if (e.getSource() == aboutMenuItem) {
			JOptionPane
					.showMessageDialog(
							null,
							"Name: File Sharer \nVersion: 1.0 "
									+ "\nDevelopers: Albin Bramstång, Andreas Wånge, John Johansson, Magnus Larsson",
							"About File Sharer",
							JOptionPane.INFORMATION_MESSAGE);
		} else if (e.getSource() == settingsMenuItem) {
			this.setChanged();
			this.notifyObservers(new ActionIDCarrier(ActionID.SHOW_SETTINGS_WINDOW));
		} else if (e.getSource() == connectToServerMenuItem) {
			this.setChanged();
			this.notifyObservers(new ActionIDCarrier(ActionID.CONNECT));
		} else if (e.getSource() == disconnectFromServerMenuItem) {
			this.setChanged();
			this.notifyObservers(new ActionIDCarrier(ActionID.DISCONNECT));
		} else if (e.getSource() == addServerMenuItem) {
			this.setChanged();
			this.notifyObservers(new ActionIDCarrier(ActionID.SHOW_SERVER_INPUT_WINDOW));
		} else if (e.getSource() == startMenuItem) {
			this.setChanged();
			this.notifyObservers(new ActionIDCarrier(ActionID.START_DOWNLOAD));
		} else if (e.getSource() == stopMenuItem) {
			this.setChanged();
			this.notifyObservers(new ActionIDCarrier(ActionID.STOP_DOWNLOAD));
		} else if (e.getSource() == deleteMenuItem) {
			this.setChanged();
			this.notifyObservers(new ActionIDCarrier(ActionID.DELETE_DOWNLOAD));
		} else if (e.getSource() == viewSharedContentMenuItem) {
			viewSharedContentMenuItem.setEnabled(false);
			viewChatMenuItem.setEnabled(true);
			chatVisible = false;
			this.setChanged();
			this.notifyObservers(new ActionIDCarrier(ActionID.VIEW_SHARED_CONTENT));
		} else if (e.getSource() == viewChatMenuItem) {
			viewSharedContentMenuItem.setEnabled(true);
			viewChatMenuItem.setEnabled(false);
			chatVisible = true;
			this.setChanged();
			this.notifyObservers(new ActionIDCarrier(ActionID.VIEW_CHAT));
		}
	}

	@Override
	public List<ActionIDCarrier> canPerform() {
		return canPerform;
	}

	@Override
	public void perform(ActionID actionToPerform, Object requiredData) {
		if (actionToPerform == ActionID.UPDATE_SELECTED_SERVER_NAME) {
			if (requiredData == null) {
				viewChatMenuItem.setEnabled(false);
				viewSharedContentMenuItem.setEnabled(false);
				connectToServerMenuItem.setEnabled(false);
				disconnectFromServerMenuItem.setEnabled(false);
			} else {
				if (((Server) requiredData).isConnected()) {
					connectToServerMenuItem.setEnabled(false);
					disconnectFromServerMenuItem.setEnabled(true);
				} else {
					connectToServerMenuItem.setEnabled(true);
					disconnectFromServerMenuItem.setEnabled(false);
				}
				
				toggleButtons();
			}
			
			server = (Server) requiredData;
		} else if (actionToPerform == ActionID.SERVER_UPDATED && requiredData.equals(server)) {
			if (ApplicationState.getInstance().getServerList().contains(requiredData)) {
				if (server.isConnected()) {
					connectToServerMenuItem.setEnabled(false);
					disconnectFromServerMenuItem.setEnabled(true);
				} else {
					connectToServerMenuItem.setEnabled(true);
					disconnectFromServerMenuItem.setEnabled(false);
				}
			} else {
				connectToServerMenuItem.setEnabled(false);
				disconnectFromServerMenuItem.setEnabled(false);
			}
			
			if (ApplicationState.getInstance().getServerList().size() > 0) {
				toggleButtons();
			}
		} else if (actionToPerform == ActionID.FILE_SELECTED || actionToPerform == ActionID.STATUS_UPDATED) {
			if (requiredData instanceof TrafficObject) {
				TrafficObject to = (TrafficObject)requiredData;

				deleteMenuItem.setEnabled(true);

				if (to.getDirection() == DataDirection.DOWN) {
					if (to.getStatus() == DownloadStatus.RUNNING) {
						startMenuItem.setEnabled(false);
						stopMenuItem.setEnabled(true);
					} else if (to.getStatus() == DownloadStatus.STOPPED) {
						startMenuItem.setEnabled(true);
						stopMenuItem.setEnabled(false);
					} else if (to.getStatus() == DownloadStatus.DONE || to.getStatus() == DownloadStatus.PREPARING_DOWNLOAD) {
						startMenuItem.setEnabled(false);
						stopMenuItem.setEnabled(false);
					}
				} else if (to.getDirection() == DataDirection.UP) {
					if (to.getStatus() == DownloadStatus.RUNNING) {
						startMenuItem.setEnabled(false);
						stopMenuItem.setEnabled(true);
					} else if (to.getStatus() == DownloadStatus.STOPPED) {
						startMenuItem.setEnabled(false);
						stopMenuItem.setEnabled(false);
					} else if (to.getStatus() == DownloadStatus.DONE || to.getStatus() == DownloadStatus.PREPARING_DOWNLOAD) {
						startMenuItem.setEnabled(false);
						stopMenuItem.setEnabled(false);
					}
				}
			}
		} else if (actionToPerform == ActionID.DELETE_DOWNLOAD) {
			startMenuItem.setEnabled(false);
			stopMenuItem.setEnabled(false);
			deleteMenuItem.setEnabled(false);
		} else if (actionToPerform == ActionID.IS_CHATVIEW) {
			if ((Boolean)requiredData) {
				chatVisible = true;
				toggleButtons();
			} else {
				chatVisible = false;
				toggleButtons();
			}
		} else if (actionToPerform == ActionID.VIEW_CHAT) {
			chatVisible = true;
			toggleButtons();
		} else if (actionToPerform == ActionID.VIEW_SHARED_CONTENT) {
			chatVisible = false;
			toggleButtons();
		}
	}
	
	private void toggleButtons() {
		if (!chatVisible) {
			viewSharedContentMenuItem.setEnabled(false);
			viewChatMenuItem.setEnabled(true);
			chatVisible = true;
		} else {			
			viewSharedContentMenuItem.setEnabled(true);
			viewChatMenuItem.setEnabled(false);
			chatVisible = false;
		}
	}
	
	@Override
	public int getPerformIDNumber(ActionIDCarrier action) {
		return canPerform.get(canPerform().indexOf(action)).getIDNumber();
	}
}
