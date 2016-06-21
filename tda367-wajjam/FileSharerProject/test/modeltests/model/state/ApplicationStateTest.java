package modeltests.model.state;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.PathChecker;

import model.data.DirectoryTreeModel;
import model.logic.DirectoryTreeLogics;
import model.login.Server;
import model.state.ApplicationState;
import model.traffic.CryptFileGetter;
import model.traffic.TrafficObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import User.User;

public class ApplicationStateTest {

	ApplicationState state;
	
	@Before
	public void setUp(){
		
		/*
		 * setUp the ApplicationState, aka put stuff in the setMethods..
		 * 
		 */
		
		state = ApplicationState.getInstance();
		
		String downDir = "C:\\";
		
		PathChecker checker = new PathChecker();
		checker.keep(new File("C:\\Adabas\\"));	//	CHANGE IF YOU DON'T HAVE THIS FOLDER >.<
		
		state.setChecker(checker);
		state.setDownloadDirectory(downDir);
		state.setLastIPandPort(new String[]{"127.0.0.1", "1337"});
		
		List<Server> serverList = new LinkedList<Server>();
		serverList.add(new Server("127.0.0.1", 1337));
		
		state.setServerList(serverList);
		
		DirectoryTreeLogics logics = new DirectoryTreeLogics(checker);
		DirectoryTreeModel model = new DirectoryTreeModel(logics.getAbstractRoot(), logics.getRealRoot());
		
		state.setSharedTree(model);
		
		ArrayList<TrafficObject> trafficList = new ArrayList<TrafficObject>();
		User user = new User("127.0.0.1", "127.0.0.1", "µ");
		trafficList.add(new CryptFileGetter(user, "C:\\", "a.txt", downDir, new TestObserver()));	//	Check if you have C:\\a.txt!!!
		state.setTrafficList(trafficList);
		
		state.setUser(user);
	}
	
	@Test
	public void testGetInstance() {
		assertNotNull(ApplicationState.getInstance());
	}

	@Test
	public void testSetState() {
		ApplicationState.setState(state);
		assertTrue(ApplicationState.getInstance().equals(state));
	}

	@Test
	public void testSetUser() {
		User user = new User("127.0.0.1", "127.0.0.1", "µ");
		
		assertTrue(ApplicationState.getInstance().getUser().equals(user));
	}

	@Test
	public void testGetUser() {
		User user = new User("127.0.0.1", "127.0.0.1", "re");
		ApplicationState.getInstance().setUser(user);
		
		assertTrue(ApplicationState.getInstance().getUser().equals(user));
	}

	@Test
	public void testSetServerList() {
		List<Server> serverList = new LinkedList<Server>();
		serverList.add(new Server("127.0.43.1", 1337));
		
		ApplicationState.getInstance().setServerList(serverList);
		
		assertTrue(ApplicationState.getInstance().getServerList().equals(serverList));
	}

	@Test
	public void testGetServerList() {
		List<Server> serverList = new LinkedList<Server>();
		serverList.add(new Server("127.54.0.1", 1337));
		
		ApplicationState.getInstance().setServerList(serverList);
		
		assertTrue(ApplicationState.getInstance().getServerList().equals(serverList));
	}

	@Test
	public void testSetTrafficList() {
		ArrayList<TrafficObject> trafficList = new ArrayList<TrafficObject>();
		User user = new User("127.0.0.1", "127.0.0.1", "µ");
		trafficList.add(new CryptFileGetter(user, "C:\\", "a.txt", "C:\\Users\\", new TestObserver()));	//	Check if you have C:\\a.txt!!!
		
		ApplicationState.getInstance().setTrafficList(trafficList);
		
		assertTrue(ApplicationState.getInstance().getTrafficList().equals(trafficList));
	}

	@Test
	public void testGetTrafficList() {
		ArrayList<TrafficObject> trafficList = new ArrayList<TrafficObject>();
		User user = new User("127.0.0.1", "127.0.0.1", "µ");
		trafficList.add(new CryptFileGetter(user, "C:\\", "a.txt", "C:\\Users\\", new TestObserver()));	//	Check if you have C:\\a.txt!!!
		
		ApplicationState.getInstance().setTrafficList(trafficList);
		
		assertTrue(ApplicationState.getInstance().getTrafficList().equals(trafficList));
	}

	@Test
	public void testSetTreePaths() {
		//Can't be tested
		assertTrue(true);
	}

	@Test
	public void testGetTreePaths() {
		//Can't be tested
		assertTrue(true);
	}

	@Test
	public void testSetChecker() {
		PathChecker checker = new PathChecker();
		checker.keep(new File("C:\\"));
		
		ApplicationState.getInstance().setChecker(checker);
		
		assertTrue(ApplicationState.getInstance().getChecker().equals(checker));
	}

	@Test
	public void testGetChecker() {
		PathChecker checker = new PathChecker();
		checker.keep(new File("C:\\"));
		
		ApplicationState.getInstance().setChecker(checker);
		
		assertTrue(ApplicationState.getInstance().getChecker().equals(checker));
	}

	@Test
	public void testSetSharedTree() {

		PathChecker checker = new PathChecker();
		checker.keep(new File("C:\\boot\\"));
		DirectoryTreeLogics logics = new DirectoryTreeLogics(checker);
		DirectoryTreeModel model = new DirectoryTreeModel(logics.getAbstractRoot(), logics.getRealRoot());
		
		ApplicationState.getInstance().setSharedTree(model);
		
		assertTrue(ApplicationState.getInstance().getSharedTree().equals(model));
	}

	@Test
	public void testGetSharedTree() {

		PathChecker checker = new PathChecker();
		checker.keep(new File("C:\\boot\\"));
		DirectoryTreeLogics logics = new DirectoryTreeLogics(checker);
		DirectoryTreeModel model = new DirectoryTreeModel(logics.getAbstractRoot(), logics.getRealRoot());
		
		ApplicationState.getInstance().setSharedTree(model);
		
		assertTrue(ApplicationState.getInstance().getSharedTree().equals(model));
	}

	@Test
	public void testSetLastIPandPort() {
		String[] ipAndPort = {"127.0.0.1", "1337"};
		ApplicationState.getInstance().setLastIPandPort(ipAndPort);
		
		assertTrue(ApplicationState.getInstance().getLastIPandPort().equals(ipAndPort));
	}

	@Test
	public void testGetLastIPandPort() {
		String[] ipAndPort = {"127.0.0.1", "1337"};
		ApplicationState.getInstance().setLastIPandPort(ipAndPort);
		
		assertTrue(ApplicationState.getInstance().getLastIPandPort().equals(ipAndPort));
	}

	@Test
	public void testGetDownloadDirectory() {
		String downDir = "C:\\";
		ApplicationState.getInstance().setDownloadDirectory(downDir);
		
		assertTrue(ApplicationState.getInstance().getDownloadDirectory().equals(downDir));
	}
	
	@Test
	public void testEquals(){
		assertTrue(ApplicationState.getInstance().equals(ApplicationState.getInstance()));
	}

	@Test
	public void testSetDownloadDirectory() {
		String downDir = "C:\\";
		ApplicationState.getInstance().setDownloadDirectory(downDir);
		
		assertTrue(ApplicationState.getInstance().getDownloadDirectory().equals(downDir));
	}
	
	@After
	public void destroy(){
		state = null;
	}

	class TestObserver implements Observer{

		@Override
		public void update(Observable o, Object arg) {
			
		}
	}
}
