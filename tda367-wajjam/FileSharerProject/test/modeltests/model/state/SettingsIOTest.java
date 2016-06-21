package modeltests.model.state;

import static org.junit.Assert.*;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.PathChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.data.DirectoryTreeModel;
import model.logic.DirectoryTreeLogics;
import model.login.Server;
import model.state.ApplicationState;
import model.state.SettingsIO;
import model.traffic.CryptFileGetter;
import model.traffic.TrafficObject;
import modeltests.model.state.ApplicationStateTest.TestObserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import User.User;

public class SettingsIOTest {
	
	@Before
	public void setUp(){
		
		/*
		 * setUp the ApplicationState, aka put stuff in the setMethods..
		 * 
		 */
		
		ApplicationState state = ApplicationState.getInstance();
		
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
		
		User user = new User("127.0.0.1", "127.0.0.1", "µ");
		
		state.setUser(user);
	}
	
	
	
	@Test
	public void testSave() {
		
		SettingsIO.save(ApplicationState.getInstance());
		
		assertTrue(SettingsIO.open().equals(ApplicationState.getInstance()));
	}

	@Test
	public void testOpen() {
		SettingsIO.save(ApplicationState.getInstance());
		
		assertTrue(SettingsIO.open().equals(ApplicationState.getInstance()));
	}

	@After
	public void destroy(){
		//cleanup
	}
	
	class TestObserver implements Observer{

		@Override
		public void update(Observable o, Object arg) {
			
		}
	}
}
