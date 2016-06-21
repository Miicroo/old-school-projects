package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import model.traffic.DataDirection;
import model.traffic.DownloadStatus;
import model.traffic.TrafficObject;

import util.ActionID;
import util.ActionIDCarrier;
import util.Performable;

/**
 * Contains a panel with buttons to control downloads.
 * @author Albin Bramstång
 */
public class MediaButtonsPanel extends Observable implements ActionListener, Performable<Object> {

	private JPanel mainPanel = new JPanel();
	private JButton startDownloadButton = new JButton();
	private JButton stopDownloadButton = new JButton();
	private JButton deleteDownloadButton = new JButton();
	private ArrayList<ActionIDCarrier> canPerform = new ArrayList<ActionIDCarrier>();

	public MediaButtonsPanel(Observer o) {
		this.addObserver(o);
		
		canPerform.add(new ActionIDCarrier(ActionID.FILE_SELECTED));
		canPerform.add(new ActionIDCarrier(ActionID.STATUS_UPDATED));
		canPerform.add(new ActionIDCarrier(ActionID.DELETE_DOWNLOAD));
		
		startDownloadButton.setToolTipText("Start");
		startDownloadButton.setIcon(new ImageIcon("icons/start.png"));
		startDownloadButton.addActionListener(this);
		startDownloadButton.setEnabled(false);
		mainPanel.add(startDownloadButton);

		stopDownloadButton.setToolTipText("Stop");
		stopDownloadButton.setIcon(new ImageIcon("icons/stop.png"));
		stopDownloadButton.addActionListener(this);
		stopDownloadButton.setEnabled(false);
		mainPanel.add(stopDownloadButton);

		deleteDownloadButton.setToolTipText("Delete");
		deleteDownloadButton.setIcon(new ImageIcon("icons/delete.png"));
		deleteDownloadButton.addActionListener(this);
		deleteDownloadButton.setEnabled(false);
		mainPanel.add(deleteDownloadButton);
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.NEW_PERFOMER_INSTANTIATED));
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	public JButton getStartDownloadButton() {
		return startDownloadButton;
	}

	public JButton getStopDownloadButton() {
		return stopDownloadButton;
	}

	public JButton getDeleteDownloadButton() {
		return deleteDownloadButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startDownloadButton) {
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.START_DOWNLOAD));
		} else if (e.getSource() == stopDownloadButton) {
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.STOP_DOWNLOAD));
		} else if (e.getSource() == deleteDownloadButton) {
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.DELETE_DOWNLOAD));
		}
	}

	@Override
	public List<ActionIDCarrier> canPerform() {
		return canPerform;
	}

	@Override
	public void perform(ActionID actionToPerform, Object requiredData) {
		if (actionToPerform == ActionID.FILE_SELECTED || actionToPerform == ActionID.STATUS_UPDATED) {
			if (requiredData != null) {
				TrafficObject trafficObject = (TrafficObject)requiredData;
				deleteDownloadButton.setEnabled(true);
				if (trafficObject.getDirection() == DataDirection.DOWN) {
					if (trafficObject.getStatus() == DownloadStatus.RUNNING) {
						startDownloadButton.setEnabled(false);
						stopDownloadButton.setEnabled(true);
					} else if (trafficObject.getStatus() == DownloadStatus.STOPPED) {
						startDownloadButton.setEnabled(true);
						stopDownloadButton.setEnabled(false);
					} else if (trafficObject.getStatus() == DownloadStatus.DONE || 
							trafficObject.getStatus() == DownloadStatus.PREPARING_DOWNLOAD) {
						startDownloadButton.setEnabled(false);
						stopDownloadButton.setEnabled(false);
					} 
				} else if (trafficObject.getDirection() == DataDirection.UP) {
					if (trafficObject.getStatus() == DownloadStatus.RUNNING) {
						startDownloadButton.setEnabled(false);
						stopDownloadButton.setEnabled(true);
					} else if (trafficObject.getStatus() == DownloadStatus.STOPPED) {
						startDownloadButton.setEnabled(false);
						stopDownloadButton.setEnabled(false);
					} else if (trafficObject.getStatus() == DownloadStatus.DONE || 
							trafficObject.getStatus() == DownloadStatus.PREPARING_DOWNLOAD) {
						startDownloadButton.setEnabled(false);
						stopDownloadButton.setEnabled(false);
					} 
				}
			}
		} else if (actionToPerform == ActionID.DELETE_DOWNLOAD){
			deleteDownloadButton.setEnabled(false);
			startDownloadButton.setEnabled(false);
			stopDownloadButton.setEnabled(false);
		}
	}
	
	@Override
	public int getPerformIDNumber(ActionIDCarrier action) {
		return canPerform.get(canPerform().indexOf(action)).getIDNumber();
	}
}
