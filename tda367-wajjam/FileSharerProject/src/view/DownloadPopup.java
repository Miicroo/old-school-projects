package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import util.ActionID;
import util.ActionIDCarrier;

/**
 * This class is used to display a popup menu, to enable downloading
 * of a whole foler.
 * @author John Johansson
 */
public class DownloadPopup extends Observable implements ActionListener{

	private JPopupMenu popupMenu = new JPopupMenu();
	private JMenuItem menuItem = new JMenuItem("Download folder");
	
	public DownloadPopup(Observer o){
		this.addObserver(o);
		menuItem.addActionListener(this);
		popupMenu.add(menuItem);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.DOWNLOAD_FOLDER));
		
	}
	
	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}
}
