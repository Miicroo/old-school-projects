package org.wajjam.project.server.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TrayIcon;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.wajjam.project.server.Server;
import org.wajjam.project.server.UserList;
import User.User;
public class ServerPanel extends JPanel implements Observer{
	
	private static final long serialVersionUID = -5836639065059670866L;
	private String[] listinfo;
	private javax.swing.JList list;
	private JScrollPane scrollpane = new JScrollPane();
	private JPanel serverinfo = new JPanel();	
	private LinkedList<User> users;
	private JLabel ip,port,members,ip2;
	private UserList ulist;
	private TrayIcon trayicon=null;
	//The layout for the server in FileServer
	public ServerPanel(){
		list = new RowStripeJList();
		ulist = UserList.getInstance();
		ulist.addObserver(this);
		
		members = new JLabel("",SwingConstants.LEFT);
		updateList();
		ip2 = new JLabel("Local host: "+Server.getLocalIp());
		ip = new JLabel("Extern host: "+Server.getIp());
		port = new JLabel("Port: 1337" );
		this.setBackground(Color.white);
		this.setLayout(new BorderLayout());
		serverinfo.setLayout(new GridLayout(2,2));
		serverinfo.setPreferredSize(new Dimension(10,30));
		serverinfo.add(ip);
		
		serverinfo.add(port);
		serverinfo.add(ip2);
		serverinfo.add(members);
		scrollpane.setViewportView(list);
		this.add(serverinfo,BorderLayout.NORTH);
		this.add(scrollpane,BorderLayout.CENTER);
		this.repaint();
		list.repaint();		
	}
	
	public void setPort(int p){
		port.setText("Port: "+p);
	}
	
	public void setTrayicon(TrayIcon trayicon) {
		this.trayicon = trayicon;
	}
	/**
	 * Update the list on the servers view
	 */
	public void updateList(){
		users = (LinkedList<User>) ulist.getUserList();
		list.removeAll();
		listinfo = new String[users.size()];
		for(int i=0;i<users.size();i++){
			listinfo[i]=users.get(i).toString();
		}
		list.setListData(listinfo);
		this.repaint();
		members.setText("Users: "+users.size());
	}
	
	private void updateTrayicon(){
		trayicon.setToolTip("FileSharer Users:"+users.size());	
	}
	
	@Override
	public void update(Observable o, Object arg) {
		updateTrayicon();
		updateList();
	}
	
	
	
}
