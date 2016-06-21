package tests;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.Player;

public class TestPlayer {

	private Player player = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		player = new Player("admin");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		player = null;
	}

	/**
	 * Test method for {@link model.Player#Player(java.lang.String)}.
	 */
	@Test
	public void testPlayer() {
		String name = "blaha";
		Player p = new Player(name);
		assertTrue(p.getUsername().equals(name));
	}

	/**
	 * Test method for {@link model.Player#getLetters()}.
	 */
	@Test
	public void testGetNrLetters() {
		int ll = 5;
		player.setNrLetters(ll);
		assertTrue(player.getNrLetters() == ll);
	}

	/**
	 * Test method for {@link model.Player#setLetters(int)}.
	 */
	@Test
	public void testSetNrLetters() {
		int ll = 5;
		player.setNrLetters(ll);
		assertTrue(player.getNrLetters() == ll);
	}

	/**
	 * Test method for {@link model.Player#getUsername()}.
	 */
	@Test
	public void testGetUsername() {
		String name = "hejhehjH";
		Player p = new Player(name);
		assertTrue(p.getUsername().equals(name));
	}

	/**
	 * Test method for {@link model.Player#getPoints()}.
	 */
	@Test
	public void testGetPoints() {
		int p = 500000;
		player.addPoints(p);
		assertTrue(player.getPoints() == p);
	}

	/**
	 * Test method for {@link model.Player#setPoints(int)}.
	 */
	@Test
	public void testSetPoints() {
		int p = 500000;
		player.addPoints(p);
		assertTrue(player.getPoints() == p);
	}
}
