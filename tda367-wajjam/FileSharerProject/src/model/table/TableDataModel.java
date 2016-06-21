package model.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.state.ApplicationState;
import model.traffic.DataDirection;
import model.traffic.DownloadStatus;
/**
 * 
 * @author Johns lap
 *A tablemodel with a update method that changes the information in the model.
 */
public class TableDataModel implements TableModel {

	private ArrayList<TableModelListener> l = new ArrayList<TableModelListener>();
	private int tempsize = 0;
	private Object[][] obj;
	
	public TableDataModel() {
		update();
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		this.l.add(l);
	}
	
	public List<TableModelListener> getAllListeners(){
		return l;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0)
			return String.class;
		else if (columnIndex == 1)
			return TableSize.class;
		else if (columnIndex == 2)
			return TableSize.class;
		else if (columnIndex == 3)
			return JProgressBar.class;
		if (columnIndex == 4)
			return TableSpeed.class;
		else if (columnIndex == 5)
			return ImageIcon.class;
		else if (columnIndex == 6)
			return ImageIcon.class;
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 8;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0)
			return "Filename";
		else if (columnIndex == 1)
			return "Size";
		else if (columnIndex == 2)
			return "Downloaded";
		else if (columnIndex == 3)
			return "Done";
		else if (columnIndex == 4)
			return "Speed";
		else if (columnIndex == 5)
			return "Direction";
		else if (columnIndex == 6)
			return "Status";
		return "User";
	}

	@Override
	public int getRowCount() {
		if (tempsize != ApplicationState.getInstance().getTrafficList().size()) {
			for (int i = 0; i < l.size(); i++) {
				l.get(i).tableChanged(new TableModelEvent(this));
				update();
			}
			tempsize = ApplicationState.getInstance().getTrafficList().size();
		}
		return ApplicationState.getInstance().getTrafficList().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return obj[rowIndex][columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		this.l.remove(l);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		obj[rowIndex][columnIndex] = aValue;
	}

	public void update() {
		obj = new Object[ApplicationState.getInstance().getTrafficList().size()][getColumnCount()];
		for (int i = 0; i < ApplicationState.getInstance().getTrafficList().size(); i++) {
			for (int j = 0; j < getColumnCount(); j++) {
				if (j == 0) {
					setValueAt(new String(ApplicationState.getInstance()
							.getTrafficList().get(i).getFileName()), i, j);
				}
				else if (j == 1) {
					double size= ApplicationState.getInstance().getTrafficList().get(i).getMaxSize();
					setValueAt(new TableSize(size), i, j);
				} else if (j == 2) {
					double size= ApplicationState.getInstance()
					.getTrafficList().get(i).getRecivedSize();
					setValueAt(new TableSize(size), i, j);
				} else if (j == 3) {
					double send = ApplicationState.getInstance()
							.getTrafficList().get(i).getRecivedSize();
					double max = ApplicationState.getInstance()
							.getTrafficList().get(i).getMaxSize();
					double procent = ((send / max)*100);
					int pro = (int)(procent*100);
					procent = pro/100.0;
					setValueAt(new Float(procent), i, j);
				} else if (j == 4) {
					double speed = ApplicationState.getInstance()
					.getTrafficList().get(i).getSpeed();
					setValueAt(new TableSpeed(speed), i, j);
				} else if (j == 5) {
					if(ApplicationState.getInstance()
					.getTrafficList().get(i).getDirection()==DataDirection.DOWN){
						setValueAt(new ImageIcon("icons/down.png"), i, j);
					}else{
						setValueAt(new ImageIcon("icons/up.png"), i, j);
					}
					
				} else if (j == 6) {
					if(ApplicationState.getInstance()
							.getTrafficList().get(i).getStatus()==DownloadStatus.STOPPED){
						setValueAt(new ImageIcon("icons/stoplittle.png"),i, j);
					}else if(ApplicationState.getInstance()
							.getTrafficList().get(i).getStatus()==DownloadStatus.DONE){
						setValueAt(new ImageIcon("icons/done.png"),i, j);
					}else{
						setValueAt(new ImageIcon("icons/startlittle.png"),i, j);
					}

				} else {
					setValueAt(ApplicationState.getInstance().getTrafficList().get(i).getUser().toString(), i, j);
				}
			}
		}

	}
}
