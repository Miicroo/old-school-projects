package modeltests.model.login;

import static org.junit.Assert.*;

import model.login.Server;
import model.login.ServerUpdate;
import model.login.ServerUpdate.Option;

import org.junit.Test;

public class ServerUpdateTest {
	
	@Test
	public void testGetOption() {
		Server server = new Server("127.0.0.1", 1337);
		Option option = ServerUpdate.Option.BAD_HOST;
		ServerUpdate update = new ServerUpdate(server, option);
		
		assertTrue(update.getOption().equals(option));
	}

	@Test
	public void testGetServer() {
		Server server = new Server("127.0.0.1", 1337);
		Option option = ServerUpdate.Option.BAD_HOST;
		ServerUpdate update = new ServerUpdate(server, option);
		
		assertTrue(update.getServer().equals(server));
	}
}
