package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import util.ActionID;
import util.ActionIDCarrier;

/**
 * This popup is used to open the directory of a downloaded file.
 * @author Albin Bramstång
 */
public class OpenFolderPopup extends Observable implements ActionListener {
	
	private JPopupMenu menu = new JPopupMenu();
	private JMenuItem openFolder = new JMenuItem("Open folder");
	
	public OpenFolderPopup(Observer o) {
		addObserver(o);
		
		openFolder.addActionListener(this);
		menu.add(openFolder);
	}
	
	public JPopupMenu getMenu() {
		return menu;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openFolder) {
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.OPEN_FOLDER));
		}
	}
}
