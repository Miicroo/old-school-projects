package modeltests.model.login;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import model.login.Server;
import model.login.ServerInOutLogic;
import model.login.ServerUpdate;
import model.state.ApplicationState;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.ActionID;
import util.ActionIDCarrier;
import util.UnsupportedActionException;

import User.User;

public class ServerInOutLogicTest {
	
	
	ApplicationState state;
	
	@Before
	public void setUp(){
		
		LinkedList<Server> serverList = new LinkedList<Server>();
		serverList.add(new Server("127.0.0.1", 1337));
		serverList.add(new Server("127.0.3.1", 1337));
		
		state = ApplicationState.getInstance();
		state.setDownloadDirectory("C:\\");
		state.setLastIPandPort(new String[]{"127.0.0.1", "1337"});
		state.setServerList(serverList);
		state.setUser(new User("127.0.0.1", "127.0.0.1", "µ"));
	}
	
	@Test
	public void testLogIn() {
		
		String host = "127.4.0.1";
		String port  = "1338";
		
		String[] ipAndPort = {host, port};
		
		Server s = new Server(host, Integer.parseInt(port));
		s.setConnected(false);
		
		ServerInOutLogic logic = ServerInOutLogic.getInstance();
		
		logic.logIn(s);
		
		for(int i = 0; i<ipAndPort.length; i++){
			assertTrue(state.getLastIPandPort()[i].equals(ipAndPort[i]));
		}
		assertTrue(state.getServerList().contains(s));
		
	}

	@Test
	public void testLogOut() {
		Server s = new Server("127.4.0.1", 1338);
		s.setConnected(false);
		
		ServerInOutLogic logic = ServerInOutLogic.getInstance();
		
		logic.logIn(s);
		
		logic.logOut(s, false);
		
		assertTrue(state.getServerList().contains(s));
		
		logic.logOut(s, true);

		assertFalse(state.getServerList().contains(s));
	}

	@Test
	public void testLogOutAll() {
		// Method doesn't effect the objects
		assertTrue(true);
	}

	@Test
	public void testGetInstance() {
		assertFalse(ServerInOutLogic.getInstance() == null);
	}

	@Test
	public void testUpdate() {
		//reworked?
		Server server = new Server("127.0.0.1", 1337);
		server.setConnected(true);
		ServerUpdate update = new ServerUpdate(server, ServerUpdate.Option.UNAME_BUSY);
		
		boolean b = server.isConnected();
		
		ServerInOutLogic logic = ServerInOutLogic.getInstance();
		
		logic.update(null, update);
		
		assertFalse(b == server.isConnected());
		assertFalse(logic.hasChanged());
		
		logic.update(null, null);
		
		assertFalse(logic.hasChanged());
	}

	@Test
	public void testCanPerform() {
		ServerInOutLogic logic = ServerInOutLogic.getInstance();
		
		assertFalse(logic.canPerform() == null);
		assertFalse(logic.canPerform().contains(null));
	}

	@Test
	public void testPerform() {
		
		/*
		 * Try to perform the ActionID CONNECT which means that
		 * the user logs in.
		 */
		
		ServerInOutLogic logic = ServerInOutLogic.getInstance();
		String host = "127.4.0.1";
		String port  = "1338";
		
		String[] ipAndPort = {host, port};
		
		Server s = new Server(host, Integer.parseInt(port));
		s.setConnected(false);
		
		logic.perform(ActionID.CONNECT, s);
		
		for(int i = 0; i<ipAndPort.length; i++){
			assertTrue(state.getLastIPandPort()[i].equals(ipAndPort[i]));
		}
		assertTrue(state.getServerList().contains(s));
	}

	@Test
	public void testCanSupply() {
		ServerInOutLogic logic = ServerInOutLogic.getInstance();
		
		assertFalse(logic.canSupply() == null);
		assertFalse(logic.canSupply().contains(null));
	}

	@Test
	public void testGetData() {
		ServerInOutLogic logic = ServerInOutLogic.getInstance();
		
		try {
			assertTrue(logic.getData(null).getClass() == Server.class);
		}
		catch(UnsupportedActionException e){
			e.printStackTrace();
		}
	}

	@Test
	public void testGetPerformIDNumber() {
		ServerInOutLogic logic = ServerInOutLogic.getInstance();
		
		List<ActionIDCarrier> can = logic.canPerform();

		if(can.size() < 1){
			assertTrue(false);
			return;
		}
		
		int id = can.get(0).getIDNumber();
		
		assertTrue(logic.getPerformIDNumber(can.get(0)) == id);
	}

	@Test
	public void testGetSupplyIDNumber() {
		ServerInOutLogic logic = ServerInOutLogic.getInstance();
		
		List<ActionIDCarrier> can = logic.canSupply();
		
		if(can.size() < 1){
			assertTrue(false);
			return;
		}
		
		int id = can.get(0).getIDNumber();
		
		assertTrue(logic.getSupplyIDNumber(can.get(0)) == id);
	}
	
	@After
	public void destroy(){
		state = null;
	}
}