package view;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import model.state.ApplicationState;
import model.table.TableDataModel;
import model.traffic.DataDirection;
import model.traffic.DownloadStatus;

import util.*;

/**
 * This class displays all ongoing file transfers, and their respective information.
 * @author Albin Bramstång
 */
public class OverviewTable extends Observable implements KeyListener,Performable<Object>, DataSupplier<Object>, TableModelListener,ActionListener,MouseListener {
	private Timer t;
	private JTable table = new JTable();
	private TableDataModel tm = new TableDataModel();
	private OpenFolderPopup openFolderPopupMenu;
	private ArrayList<ActionIDCarrier> canSupply = new ArrayList<ActionIDCarrier>();
	private ArrayList<ActionIDCarrier> canPerform = new ArrayList<ActionIDCarrier>();
	
	public OverviewTable(Observer o) {
		
		canSupply.add(new ActionIDCarrier(ActionID.START_DOWNLOAD));
		canSupply.add(new ActionIDCarrier(ActionID.STOP_DOWNLOAD));
		canSupply.add(new ActionIDCarrier(ActionID.DELETE_DOWNLOAD));
		canSupply.add(new ActionIDCarrier(ActionID.FILE_SELECTED));
		canSupply.add(new ActionIDCarrier(ActionID.STATUS_UPDATED));
		canPerform.add(new ActionIDCarrier(ActionID.OPEN_FOLDER));
		t= new Timer(200, this);
		t.start();
		addObserver(o);
		openFolderPopupMenu = new OpenFolderPopup(o);
		table.addMouseListener(this);
		table.setModel(tm);
		table.setGridColor(Color.white);
		table.setShowGrid(false);
		tm.addTableModelListener(this);
		//table.setAutoCreateRowSorter(true);
		table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).  
		put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				"doNothing");
		table.addKeyListener(this);
		ProgressRenderer renderer = new ProgressRenderer(0, 100);
        renderer.setStringPainted(true);
        table.setDefaultRenderer(JProgressBar.class, renderer);
        table.setRowHeight((int) renderer.getPreferredSize().getHeight());
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.NEW_PERFOMER_INSTANTIATED));
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.NEW_SUPPLIER_INSTANTIATED));
	}


	
	public JTable getTable() {
		return table;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		table.repaint();
		table.revalidate();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		tm.update();
		table.repaint();
		table.revalidate();
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount()==2){
			startFile();
		}		
	}

	/**
	 * Starts the selected file in the default program.
	 */
	public void startFile(){
		if(ApplicationState.getInstance().getTrafficList().get(table.getSelectedRow()).getDirection()==DataDirection.DOWN&&ApplicationState.getInstance().getTrafficList().get(table.getSelectedRow()).getStatus()==DownloadStatus.DONE){
			String path=ApplicationState.getInstance().getTrafficList().get(table.getSelectedRow()).getDirectory();
			path=path+ApplicationState.getInstance().getTrafficList().get(table.getSelectedRow()).getFileName();
			try {
				Desktop.getDesktop().open(new File(path));
			} catch (IOException e1) {}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.FILE_SELECTED));
		if (e.getButton() == 3) {
			table.requestFocus();
			int a = e.getY()/16;
			if(a<table.getRowCount()){
				table.changeSelection(a, a+1, false, false);
				if(ApplicationState.getInstance().getTrafficList().get(table.getSelectedRow()).getDirection()==DataDirection.DOWN){
					openFolderPopupMenu.getMenu().show(table, e.getX(), e.getY());
				}
				
			}
		}
	}

	@Override
	public List<ActionIDCarrier> canSupply() {
		return canSupply;
	}

	@Override
	public Object getData(ActionID action)
			throws UnsupportedActionException {
		if(table.getSelectedRow()==-1){
			return null;
		}
		if(action == ActionID.STATUS_UPDATED||action == ActionID.FILE_SELECTED){
			return ApplicationState.getInstance().getTrafficList().get(table.getSelectedRow());
		}
		return table.getSelectedRows();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==127){
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.DELETE_DOWNLOAD));
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER){
			startFile();
		} else {
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.FILE_SELECTED));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}



	@Override
	public List<ActionIDCarrier> canPerform() {
		return canPerform;
	}



	@Override
	public void perform(ActionID actionToPerform, Object requiredData) {
		if(actionToPerform == ActionID.OPEN_FOLDER){
			String path=ApplicationState.getInstance().getTrafficList().get(table.getSelectedRow()).getDirectory();
			try {
				Desktop.getDesktop().open(new File(path));
			} catch (IOException e1) {}
		}
		
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
