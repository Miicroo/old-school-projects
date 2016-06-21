package modeltests.model.login;

import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import model.login.Server;
import model.login.ServerUpdate;
import model.login.UserLogin;
import model.state.ApplicationState;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import User.User;

public class UserLoginTest {

	@Before
	public void setUp(){
		ApplicationState.getInstance().setUser(new User("127.0.0.1", "127.0.0.1", "µ"));
	}
	
	@Test
	public void testRun() {
		Server server = new Server("127.0.0.1", 1337);
		
		UserLogin login = new UserLogin(server, new Observer(){
			@Override
			public void update(Observable o, Object arg) {
				ServerUpdate upd = (ServerUpdate)arg;
				assertTrue(upd.getOption() == ServerUpdate.Option.DISCONNECT);
			}
		});
	}

	@After
	public void destroy(){
		ApplicationState.getInstance().setUser(null);
	}
}
