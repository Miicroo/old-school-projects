package org.wajjam.project.server.gui;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.JFrame;
import org.wajjam.project.server.SocketListenerThread;


public class FileServer extends JFrame implements ActionListener,WindowListener{
	
	private static final long serialVersionUID = 547299214453606299L;
	private TrayIcon trayIcon;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private SystemTray tray = SystemTray.getSystemTray();
    private Image image = Toolkit.getDefaultToolkit().getImage("dc.gif");
	private PopupMenu popmenu = new PopupMenu();
	private MenuItem exitMenuItem = new MenuItem("Exit");
	private MenuItem infoMenuItem = new MenuItem("Info");
	private ServerPanel serverpanel = new ServerPanel();
	private ServerSocket socket;
	private SocketListenerThread socketlistener;
	//Starts the server and set all layout
	public FileServer(int port){
		try {
			socket = new ServerSocket(port);
			serverpanel.setPort(socket.getLocalPort());
			socketlistener = new SocketListenerThread(socket);
			socketlistener.start();
		} catch (IOException e) {}
		this.setTitle("File Sharer Server");
		this.setIconImage(image);
		this.add(serverpanel);
		this.setVisible(true);
		this.setResizable(false);
		this.addWindowListener(this);
		this.setLocation((int)(screenSize.getWidth()/2-200), (int)(screenSize.getHeight()/2-150));
		this.setSize(400, 300);
		systemTray();
	}
	
	private void systemTray(){
		if(SystemTray.isSupported()){
			popmenu.add(infoMenuItem);
			popmenu.addSeparator();
			popmenu.add(exitMenuItem);
			infoMenuItem.addActionListener(this);
			exitMenuItem.addActionListener(this);
			trayIcon= new TrayIcon(image, "File Sharer Users:0", popmenu);
			trayIcon.addActionListener(this);
			serverpanel.setTrayicon(trayIcon);
			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				e.printStackTrace();
			}	
		}		
	}


	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(exitMenuItem)){
			System.exit(0);
		}else if(e.getSource().equals(infoMenuItem)){
			this.setVisible(true);
		}else if(e.getSource().equals(trayIcon)){
			this.setVisible(true);
			this.setExtendedState(NORMAL);
		}
		
	}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {
		this.setVisible(false);
	}

	@Override
	public void windowOpened(WindowEvent e) {}
	
}