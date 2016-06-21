package modeltests.model.traffic;

import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import model.traffic.ClientSocketListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClientSocketListenerTest {

	Observer obs;
	
	@Before
	public void setUp(){
		obs = new Observer(){
			@Override
			public void update(Observable o, Object arg) {
				
			}
		};
	}

	@Test
	public void testGetServerSocket() {
		ClientSocketListener cl = new ClientSocketListener(1337, obs);
		
		assertNotNull(cl.getServerSocket());
	}
	
	@After
	public void destroy(){
		obs = null;
	}
}
