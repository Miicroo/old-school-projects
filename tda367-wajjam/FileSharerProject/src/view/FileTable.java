package view;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.data.TreeObject;
import model.login.Server;
import model.state.ApplicationState;
import model.table.TableSize;
import model.traffic.FileGetObject;

import util.ActionID;
import util.ActionIDCarrier;
import util.DataSupplier;
import util.Performable;
import util.UnsupportedActionException;

/**
 * This class is responsible for the graphical representation of the files in
 * a selected foler.
 * @author Albin Bramstång
 */
public class FileTable extends Observable implements KeyListener,
		Performable<Object>, DataSupplier<FileGetObject>, MouseListener {
	private JTable table = new JTable();
	private List<ActionIDCarrier> canPerform = new LinkedList<ActionIDCarrier>();
	private ArrayList<ActionIDCarrier> canSupply = new ArrayList<ActionIDCarrier>();
	private DefaultTableModel tm = new DefaultTableModel();
	private FileGetObject o = null;
	private Server server;
	private Server activeServer;
	private TreeObject treeObject = null;
	private JScrollPane scrollPane;
	
	public FileTable(Server s, Observer o, int id) {
		addObserver(o);

		server = s;
		table.addKeyListener(this);
		table.addMouseListener(this);
		
		canPerform.add(new ActionIDCarrier(ActionID.RECEIVE_TREE));
		canPerform.add(new ActionIDCarrier(ActionID.FILE_ACQUIRED));
		canPerform.add(new ActionIDCarrier(ActionID.SERVER_UPDATED));
		canPerform.add(new ActionIDCarrier(ActionID.ACQUIRE_FILE));
		canPerform.add(new ActionIDCarrier(ActionID.UPDATE_SELECTED_SERVER_NAME));
		canSupply.add(new ActionIDCarrier(ActionID.RECEIVE_FILE));
		table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "doNothing");
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.NEW_PERFOMER_INSTANTIATED));
		scrollPane = new JScrollPane();
		scrollPane.getViewport().setBackground(Color.white);
		table.setModel(new DefaultTableModel());
		table.setGridColor(Color.white);
	}

	@Override
	public List<ActionIDCarrier> canPerform() {
		return canPerform;
	}
	
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	
	public void removeAllRows(){
		for(int i=0;i<tm.getRowCount();i++){
			tm.removeRow(i);
		}
	}

	@Override
	public void perform(ActionID actionToPerform, Object requiredData) {
		if (actionToPerform == ActionID.FILE_ACQUIRED) {
			if (server.equals(activeServer)) {	
				scrollPane.setViewportView(table);
				String[] names = {"Name","Size","Type"};
				tm = new DefaultTableModel(names,0) {
					private static final long serialVersionUID = 8985721810267495153L;

					@Override
					public Class<?> getColumnClass(int columnIndex) {
						if (columnIndex == 0)
							return String.class;
						else if (columnIndex == 1)
							return TableSize.class;
	
						return String.class;
					}
					@Override
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
				

				treeObject = (TreeObject) requiredData;
				for (int i = 0; i < treeObject.getList().size(); i++) {
					Double size= treeObject.getList().get(i).getSize();
					tm.addRow(new Object[] {
							treeObject.getList().get(i).getName(), new TableSize(size),
							treeObject.getList().get(i).getMimeType() });

				}
				synchronized(table) {
					table.setModel(tm);
					table.repaint();
					table.revalidate();
				}
			}
		} else if (actionToPerform == ActionID.SERVER_UPDATED) {
			if ((Server) requiredData == server) {
				if (!server.isConnected()) {
					table.setModel(new DefaultTableModel());
				}
			}
		} else if (actionToPerform == ActionID.RECEIVE_TREE) {
			table.setModel(new DefaultTableModel());
		} else if (actionToPerform == ActionID.UPDATE_SELECTED_SERVER_NAME) {
			activeServer = (Server) requiredData;
		}else if (actionToPerform == ActionID.ACQUIRE_FILE) {
			if(activeServer.equals(server)){
				scrollPane.setViewportView(new JLabel(new ImageIcon("res/load.gif")));
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			downlodSelectedFiles();
		}
	}

	/**
	 * Saves information for all selectedfiles, to enable downloading.
	 */
	public void downlodSelectedFiles() {
		int[] list = table.getSelectedRows();
		for (int id = 0; id < table.getSelectedRows().length; id++) {
			o = new FileGetObject(treeObject.getUser(), treeObject.getList()
					.get(list[id]).getFilename(), treeObject.getList().get(
					list[id]).getName(), ApplicationState.getInstance()
					.getDownloadDirectory()
					+ System.getProperty("file.separator"));
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.NEW_SUPPLIER_INSTANTIATED));
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.RECEIVE_FILE));
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.SUPPLIER_DELETED));
		}
	}

	public void remove() {
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.PERFORMER_DELETED));
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.SUPPLIER_DELETED));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public List<ActionIDCarrier> canSupply() {
		return canSupply;
	}

	@Override
	public FileGetObject getData(ActionID action)
			throws UnsupportedActionException {
		return o;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			downlodSelectedFiles();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
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
