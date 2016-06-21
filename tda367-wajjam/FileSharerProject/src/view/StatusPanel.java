package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import model.login.Server;
import model.state.ApplicationState;
import model.statusbar.StatusbarData;

import util.ActionID;
import util.ActionIDCarrier;
import util.Performable;

/**
 * This class represents a status panel for the File Sharing application.
 * @author Albin Bramstång
 */
public class StatusPanel extends Observable implements Performable<Server>, ActionListener {

	private JPanel statusPanel = new JPanel();
	private JLabel serverTextLabel = new JLabel("Server:");
	private JLabel serverLabel = new JLabel("N/A");
	private JLabel statusTextLabel = new JLabel("Status:");
	private JLabel statusLabel = new JLabel("N/A");
	private JLabel downloadTextLabel = new JLabel("D:");
	private JLabel downloadSpeedLabel = new JLabel("0");
	private JLabel uploadTextLabel = new JLabel("U:");
	private JLabel uploadSpeedLabel = new JLabel("0");
	private JSeparator separator1 = new JSeparator(SwingConstants.VERTICAL);
	private JSeparator separator2 = new JSeparator(SwingConstants.VERTICAL);
	private JSeparator separator3 = new JSeparator(SwingConstants.VERTICAL);
	private JSeparator separator4 = new JSeparator(SwingConstants.VERTICAL);
	private JSeparator separator5 = new JSeparator(SwingConstants.VERTICAL);
	private List<ActionIDCarrier> canPerform = new LinkedList<ActionIDCarrier>();
	private Server activeServer = null;
	private Timer updateTimer = new Timer(500, this);
	
	public StatusPanel(Observer o) {
		this.addObserver(o);
		
		separator1.setPreferredSize(new Dimension(1, 20));
		statusPanel.add(separator1);
		statusPanel.add(serverTextLabel);
		statusPanel.add(serverLabel);
		
		separator2.setPreferredSize(new Dimension(1, 20));
		statusPanel.add(separator2);
		statusPanel.add(statusTextLabel);
		statusPanel.add(statusLabel);
		
		separator3.setPreferredSize(new Dimension(1, 20));
		statusPanel.add(separator3);
		statusPanel.add(downloadTextLabel);
		statusPanel.add(downloadSpeedLabel);
		
		separator4.setPreferredSize(new Dimension(1, 20));
		statusPanel.add(separator4);
		statusPanel.add(uploadTextLabel);
		statusPanel.add(uploadSpeedLabel);
		
		separator5.setPreferredSize(new Dimension(1, 20));
		statusPanel.add(separator5);
		
		canPerform.add(new ActionIDCarrier(ActionID.UPDATE_SELECTED_SERVER_NAME));
		canPerform.add(new ActionIDCarrier(ActionID.UPDATE_DOWNLOAD_SPEED));
		canPerform.add(new ActionIDCarrier(ActionID.UPDATE_UPLOAD_SPEED));
		canPerform.add(new ActionIDCarrier(ActionID.SERVER_UPDATED));
		
		updateTimer.start();
		
		this.setChanged();
		this.notifyObservers(new ActionIDCarrier(ActionID.NEW_PERFOMER_INSTANTIATED));
	}
	
	/**
	 * Returns the main graphical component of this class, containing
	 * all subcomponents. 
	 * 
	 * @return JPanel
	 */
	public JPanel getStatusPanel() {
		return statusPanel;
	}

	public void updateServerName(String name) {
		serverLabel.setText(name);
	}
	
	public void updateConnectionStatus(boolean status) {
		statusLabel.setText((status ? "Connected" : "Disconnected"));
	}
	
	public void updateDownloadSpeed(String speed) {
		downloadSpeedLabel.setText(speed);
	}
	
	public void updateUploadSpeed(String speed) {
		uploadSpeedLabel.setText(speed);
	}

	@Override
	public List<ActionIDCarrier> canPerform() {
		return canPerform;
	}

	@Override
	public void perform(ActionID actionToPerform, Server requiredData) {		
		switch (actionToPerform) {
		case UPDATE_SELECTED_SERVER_NAME: 
			if(requiredData!=null){
				this.updateConnectionStatus(requiredData.isConnected());
				this.updateServerName(requiredData.getHost());
			}else{
				this.updateConnectionStatus(false);
				this.updateServerName("N/A");
			}
			activeServer = requiredData;
			break;
		case UPDATE_DOWNLOAD_SPEED:
			this.updateDownloadSpeed(null);
			break;
		case UPDATE_UPLOAD_SPEED:
			this.updateUploadSpeed(null);
			break;
		case SERVER_UPDATED:
			if(ApplicationState.getInstance().getServerList().contains(requiredData)){
				if(requiredData.equals(activeServer) || serverLabel.getText().equals("N/A")){
					this.updateServerName(requiredData.getHost());
					this.updateConnectionStatus(requiredData.isConnected());
				}
			}
			break;	
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		downloadSpeedLabel.setText(StatusbarData.getDownSpeed());
		uploadSpeedLabel.setText(StatusbarData.getUpSpeed());
	}
	
	@Override
	public int getPerformIDNumber(ActionIDCarrier action) {
		return canPerform.get(canPerform().indexOf(action)).getIDNumber();
	}
}
