package modeltests.model.traffic;

import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import model.traffic.TrafficLogic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.ActionIDCarrier;
import util.UnsupportedActionException;

public class TrafficLogicTest {

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
	public void testGetInstance() {
		assertNotNull(TrafficLogic.getInstance(obs));
	}

	@Test
	public void testGetFileFileGetObject() {
		/*
		 * Calling other threads, not to be tested.
		 */
		assertTrue(true);
	}

	@Test
	public void testGetTree() {
		/*
		 * Calling other threads, not to be tested.
		 */
		assertTrue(true);
	}

	@Test
	public void testGetFileString() {
		/*
		 * Calling other threads, not to be tested.
		 */
		assertTrue(true);
	}

	@Test
	public void testUpdate() {
		/*
		 * Calling other threads, not to be tested.
		 */
		assertTrue(true);
	}

	@Test
	public void testStopTraffic() {
		/*
		 * Calling other threads, not to be tested.
		 */
		assertTrue(true);
	}

	@Test
	public void testCanPerform() {

		assertNotNull(TrafficLogic.getInstance(obs).canPerform());
		assertFalse(TrafficLogic.getInstance(obs).canPerform().contains(null));
	}

	@Test
	public void testPerform() {
		/*
		 * Calling other threads, not to be tested.
		 */
		assertTrue(true);
	}

	@Test
	public void testSendChat() {
		/*
		 * Calling other threads, not to be tested.
		 */
		assertTrue(true);
	}

	@Test
	public void testDownloadFolder() {
		/*
		 * Calling other threads, not to be tested.
		 */
		assertTrue(true);
	}

	@Test
	public void testStartDownloadFiles() {
		/*
		 * Calling other threads, not to be tested.
		 */
		assertTrue(true);
	}

	@Test
	public void testStopTransfer() {
		/*
		 * Calling other threads, not to be tested.
		 */
		assertTrue(true);
	}

	@Test
	public void testStartTransfer() {
		/*
		 * Calling other threads, not to be tested.
		 */
		assertTrue(true);
	}

	@Test
	public void testDeleteTransfer() {
		/*
		 * Calling other threads, not to be tested.
		 */
		assertTrue(true);
	}

	@Test
	public void testCanSupply() {
		assertNotNull(TrafficLogic.getInstance(obs).canSupply());
		assertFalse(TrafficLogic.getInstance(obs).canSupply().contains(null));
	}

	@Test
	public void testGetData() {
		// Do some setup first?
		try {
			assertNotNull(TrafficLogic.getInstance(obs).getData(null));
		}
		catch(UnsupportedActionException e) {
			fail(e.getMessage()+", (test throws exception)");
		}
	}

	@Test
	public void testGetPerformIDNumber() {
		int index = 0;
		
		if(TrafficLogic.getInstance(obs).canPerform().size() < (index+1)){
			fail("Can't perform action at index "+index);
		}
		
		int id = TrafficLogic.getInstance(obs).canPerform().get(index).getIDNumber();
		ActionIDCarrier action = TrafficLogic.getInstance(obs).canPerform().get(index);
		
		
		assertTrue(TrafficLogic.getInstance(obs).getPerformIDNumber(action) == id);
	}

	@Test
	public void testGetSupplyIDNumber() {
		int index = 0;
		
		if(TrafficLogic.getInstance(obs).canSupply().size() < (index+1)){
			fail("Can't supply action at index "+index);
		}
		
		int id = TrafficLogic.getInstance(obs).canSupply().get(index).getIDNumber();
		ActionIDCarrier action = TrafficLogic.getInstance(obs).canSupply().get(index);
		
		
		assertTrue(TrafficLogic.getInstance(obs).getSupplyIDNumber(action) == id);
	}

	@After
	public void destroy(){
		obs = null;
	}
}
