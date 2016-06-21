package modeltests.model.login;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import model.login.Server;

import org.junit.Test;

import User.User;

public class ServerTest {

	@Test
	public void testHashCode() {
		Server server = new Server("127.0.0.1", 1337);
		Server server2 = new Server("137.0.0.13", 1284);
		Server server3 = new Server("127.0.0.1", 1337);
		
		assertTrue(server.hashCode() == server3.hashCode());
		assertFalse(server.hashCode() == server2.hashCode());
	}

	@Test
	public void testSetConnected() {
		Server server = new Server("127.0.0.1", 1337);
		server.setConnected(false);
		assertFalse(server.isConnected());
	}

	@Test
	public void testIsConnected() {
		Server server = new Server("127.0.0.1", 1337);
		server.setConnected(true);
		assertTrue(server.isConnected());
	}

	@Test
	public void testGetHost() {
		String host = "127.0.0.1";
		int port = 1337;
		Server server = new Server(host, port);
		
		assertTrue(server.getHost().equals(host));
	}

	@Test
	public void testGetPort() {
		String host = "127.0.0.1";
		int port = 1337;
		Server server = new Server(host, port);
		
		assertTrue(server.getPort() == port);
	}

	@Test
	public void testSetUlist() {
		Server server = new Server("127.0.0.1", 1337);
		server.setConnected(false);
		
		List<User> userList = new LinkedList<User>();
		
		User a = new User("hej", "hej", "hej");
		User b = new User("hej", "hej", "hej");
		User c = new User("hej", "hej", "hej");
		User d = new User("hej", "hej", "hej");
		
		userList.add(a);
		userList.add(b);
		userList.add(c);
		userList.add(d);
		
		server.setUlist(userList);
		
		assertTrue(server.getUlist().equals(userList));
		
	}

	@Test
	public void testGetUlist() {
		Server server = new Server("127.0.0.1", 1337);
		server.setConnected(false);
		
		List<User> userList = new LinkedList<User>();
		
		User a = new User("hej", "hej", "hej");
		User b = new User("hej", "hej", "hej");
		User c = new User("hej", "hej", "hej");
		User d = new User("hej", "hej", "hej");
		
		userList.add(a);
		userList.add(b);
		userList.add(c);
		userList.add(d);
		
		server.setUlist(userList);
		
		assertTrue(server.getUlist().equals(userList));
	}

	@Test
	public void testGetUserArray() {
		
		Server server = new Server("127.0.0.1", 1337);
		server.setConnected(false);
		
		List<User> userList = new LinkedList<User>();
		
		User a = new User("hej", "hej", "hej");
		User b = new User("hej", "hej", "hej");
		User c = new User("hej", "hej", "hej");
		User d = new User("hej", "hej", "hej");
		
		userList.add(a);
		userList.add(b);
		userList.add(c);
		userList.add(d);
		
		server.setUlist(userList);
		User[] serverUsers = server.getUserArray();
		
		assertNotNull(serverUsers);
		assertTrue(userList.size() == serverUsers.length);
		
		for(int i = 0; i<serverUsers.length; i++){
			assertTrue(userList.get(i).equals(serverUsers[i]));
		}
	}

	@Test
	public void testEquals() {
		Server server = new Server("127.0.0.1", 1337);
		Server server2 = new Server("137.0.0.1", 3380);
		
		assertTrue(server.equals(server));
		assertFalse(server.equals(server2));
	}

	@Test
	public void testToString() {
		Server server = new Server("127.0.0.1", 1337);
		
		assertNotNull(server.toString());
	}
}