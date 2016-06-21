package model.traffic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.tree.TreeNode;

import model.data.ChatObject;
import model.data.DirectoryTreeNode;
import model.data.FileInformation;
import model.data.TreeObject;
import model.login.Server;
import model.state.ApplicationState;

import User.User;

import util.ActionID;
import util.ActionIDCarrier;
import util.DataSupplier;
import util.Performable;
import util.UnsupportedActionException;

/**
 * This class is responsible for all traffic from a user to another user
 * 
 * @author Johns lap
 * 
 */
public class TrafficLogic extends Observable implements Observer,
		Performable<Object>, DataSupplier<TreeObject> {

	private static TrafficLogic instance = null;
	private Thread list = null;
	private ClientSocketListener cl = null;
	private TreeObject data = null;
	private TreeObject obj = null;
	private Observer o;
	private ArrayList<FileInformation> lista = new ArrayList<FileInformation>();
	private int amountFolder = 0;
	private int amountFolder1 = 0;
	private String tempdir = "";
	private ArrayList<ActionIDCarrier> canSupply = new ArrayList<ActionIDCarrier>();
	private ArrayList<ActionIDCarrier> canPerform = new ArrayList<ActionIDCarrier>();
	private ArrayList<FileReceiver> fileGetting = new ArrayList<FileReceiver>();
	private ArrayList<User> userList = new ArrayList<User>();

	private TrafficLogic(Observer o) {
		this.o = o;
		addObserver(o);
		cl = new ClientSocketListener(1336, o);
		list = new Thread(cl);
		list.start();

		canSupply.add(new ActionIDCarrier(ActionID.TREE_RECEIVED));
		canPerform.add(new ActionIDCarrier(ActionID.RECEIVE_FILE));
		canPerform.add(new ActionIDCarrier(ActionID.CHANGE_PORT));
		canPerform.add(new ActionIDCarrier(ActionID.RECEIVE_TREE));
		canPerform.add(new ActionIDCarrier(ActionID.ACQUIRE_FILE));
		canPerform.add(new ActionIDCarrier(ActionID.START_DOWNLOAD));
		canPerform.add(new ActionIDCarrier(ActionID.STOP_DOWNLOAD));
		canPerform.add(new ActionIDCarrier(ActionID.DELETE_DOWNLOAD));
		canPerform.add(new ActionIDCarrier(ActionID.DOWNLOAD_NODE));
		canPerform.add(new ActionIDCarrier(ActionID.STATUS_UPDATED));
		canPerform.add(new ActionIDCarrier(ActionID.SEND_CHAT));
		canPerform.add(new ActionIDCarrier(ActionID.SERVER_UPDATED));
	}

	public void changePort(int port) {
		try {
			cl.getServerSocket().close();
		} catch (IOException e) {
		}
		list.interrupt();
		// list.stop();
		cl = new ClientSocketListener(port, o);
		list = new Thread(cl);
		list.start();
	}

	public synchronized static TrafficLogic getInstance(Observer o) {
		if (instance == null) {
			instance = new TrafficLogic(o);
		}
		return instance;
	}

	public void getFile(FileGetObject o) {
		if (userList.contains(o.getUser())) {
			new CryptFileGetter(o.getUser(), o.getPath(), o.getFileName(), o
					.getDir(), this.o);
		}
	}

	public void getTree(User user) {
		if (userList.contains(user)) {
			new DirectoryTreeReceiver(user, this);
		}
	}

	public synchronized void getFile(TreeObject ob) {
		if (userList.contains(ob.getUser())) {
			if (fileGetting.size() != 0) {
				for (FileReceiver f : fileGetting) {
					if (f.getThread().isAlive()) {
						f.deleteObservers();
					}
				}
				fileGetting.clear();
			}
			fileGetting.add(new FileReceiver(ob.getUser(), ob.getNode(), o));
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof DirectoryTreeReceiver) {
			TreeObject dm = (TreeObject) arg;
			data = dm;
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.TREE_RECEIVED));
		} else if (o instanceof FileReceiver) {
			if (((ActionIDCarrier) arg).getId() == ActionID.FILE_ACQUIRED) {
				FileReceiver a = (FileReceiver) o;
				try {
					lista.addAll(a.getData(ActionID.FILE_ACQUIRED).getList());
					amountFolder1++;
					if (amountFolder == amountFolder1) {
						startDownloadFiles();
					}
				} catch (UnsupportedActionException e) {
				}
			}
		}

	}

	public void stopTraffic(TrafficObject data) {

	}

	@Override
	public List<ActionIDCarrier> canPerform() {
		return canPerform;
	}

	@Override
	public void perform(ActionID actionToPerform, Object requiredData) {
		if (actionToPerform == ActionID.RECEIVE_FILE) {
			if (requiredData instanceof FileGetObject) {
				getFile((FileGetObject) requiredData);
			}
		} else if (actionToPerform == ActionID.RECEIVE_TREE) {
			if (requiredData instanceof User) {
				getTree((User) requiredData);
			}
		} else if (actionToPerform == ActionID.CHANGE_PORT) {
			if (requiredData instanceof Integer) {
				changePort((Integer) requiredData);
			}
		} else if (actionToPerform == ActionID.ACQUIRE_FILE) {
			if (requiredData instanceof TreeObject) {
				getFile((TreeObject) requiredData);
			}
		} else if (actionToPerform == ActionID.DELETE_DOWNLOAD) {
			if (requiredData != null) {
				deleteTransfer((int[]) requiredData);
			}
		} else if (actionToPerform == ActionID.START_DOWNLOAD) {
			startTransfer((int[]) requiredData);
		} else if (actionToPerform == ActionID.STOP_DOWNLOAD) {
			stopTransfer((int[]) requiredData);
		} else if (actionToPerform == ActionID.DOWNLOAD_NODE) {
			downloadFolder((TreeObject) requiredData);
		} else if (actionToPerform == ActionID.SEND_CHAT) {
			sendChat((ChatObject) requiredData);
		} else if (actionToPerform == ActionID.SERVER_UPDATED) {
			userList.clear();
			for (Server s : ApplicationState.getInstance().getServerList()) {
				userList.addAll(s.getUlist());
			}
			for (TrafficObject o : ApplicationState.getInstance()
					.getTrafficList()) {
				if (!userList.contains(o.getUser())) {
					o.stopTransfer();
				}
			}
		}
	}

	public void sendChat(ChatObject s) {
		new ChatSender(s.getMessage(), s.getServer());
	}

	public void downloadFolder(TreeObject o) {
		obj = o;
		TreeNode[] l = o.getTreeNode().getPath();
		tempdir = l[l.length - 1].toString();
		lista = new ArrayList<FileInformation>();
		List<DirectoryTreeNode> list = new LinkedList<DirectoryTreeNode>();
		amountFolder1 = 0;
		list = saveNodes(o.getTreeNode(), list);
		amountFolder = list.size();
		for (int i = 0; i < list.size(); i++) {
			new FileReceiver(o.getUser(), list.get(i).getFile(), this);
		}
	}

	public void startDownloadFiles() {
		for (FileInformation f : lista) {
			String dir = "";
			String folders[] = f.getFilename().split("\\\\");
			int u = 0;
			for (u = 0; u < folders.length; u++) {
				if (folders[u].equals(tempdir)) {
					break;
				}
			}
			StringBuilder builder = new StringBuilder();
			for (; u < folders.length - 1; u++) {
				builder.append(folders[u] + System.getProperty("file.separator"));
			}
			dir = builder.toString();
			getFile(new FileGetObject(obj.getUser(), f.getFilename(), f
					.getName(), ApplicationState.getInstance()
					.getDownloadDirectory()
					+ System.getProperty("file.separator") + dir));
		}
	}

	private List<DirectoryTreeNode> saveNodes(DirectoryTreeNode parent,
			List<DirectoryTreeNode> list) {

		final int c = parent.getChildCount();
		list.add(parent);
		for (int i = 0; i < c; i++) {
			DirectoryTreeNode node = (DirectoryTreeNode) parent.getChildAt(i);

			list.add(node);
			saveNodes(node, list);
		}

		return list;
	}

	public void stopTransfer(int[] in) {
		for (int i = in.length - 1; i >= 0; i--) {
			TrafficObject t = ApplicationState.getInstance().getTrafficList()
					.get(in[i]);
			if (t.getStatus() == DownloadStatus.RUNNING) {
				t.stopTransfer();
			}
		}
	}

	public void startTransfer(int[] in) {
		for (int i = in.length - 1; i >= 0; i--) {
			TrafficObject t = ApplicationState.getInstance().getTrafficList()
					.get(in[i]);
			if (t.getStatus() == DownloadStatus.STOPPED) {
				if (userList.contains(t.getUser())) {
					t.startTransfer(o);
				}
			}
		}
	}

	public void deleteTransfer(int[] in) {
		for (int i = in.length - 1; i >= 0; i--) {
			TrafficObject t = ApplicationState.getInstance().getTrafficList()
					.get(in[i]);
			if (t.getStatus() == DownloadStatus.RUNNING) {
				t.stopTransfer();
			}
			ApplicationState.getInstance().getTrafficList().remove(t);
		}
	}

	@Override
	public List<ActionIDCarrier> canSupply() {
		return canSupply;
	}

	@Override
	public TreeObject getData(ActionID action)
			throws UnsupportedActionException {
		return data;
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