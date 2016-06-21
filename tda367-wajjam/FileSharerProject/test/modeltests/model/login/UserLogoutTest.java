package modeltests.model.login;

import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import model.login.Server;
import model.login.UserLogout;
import model.state.ApplicationState;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import User.User;

public class UserLogoutTest {

	@Before
	public void setUp(){
		ApplicationState.getInstance().setUser(new User("127.0.0.1", "127.0.0.1", "µ"));
	}
	
	@Test
	public void testRun() {
		Server server = new Server("127.0.0.1", 1337);
		
		UserLogout out = new UserLogout(server, new Observer(){
			public void update(Observable o, Object arg) {
				assertTrue(true);
			}
		});
	}

	@After
	public void destroy(){
		ApplicationState.getInstance().setUser(null);
	}
}
