package modeltests.model.traffic;

import static org.junit.Assert.*;

import model.traffic.FileGetObject;

import org.junit.Test;

import User.User;

public class FileGetObjectTest {

	@Test
	public void testGetUser() {
		User user = new User("127.0.0.1", "127.0.0.1", "µ");
		String path = "C:\\lol\\";
		String filename = "trolol.lol";
		String downDir = "C:\\";
		FileGetObject fgo = new FileGetObject(user, path, filename, downDir);
		
		assertTrue(fgo.getUser().equalsUserName(user));
	}

	@Test
	public void testGetDir() {
		User user = new User("127.0.0.1", "127.0.0.1", "µ");
		String path = "C:\\lol\\";
		String filename = "trolol.lol";
		String downDir = "C:\\";
		FileGetObject fgo = new FileGetObject(user, path, filename, downDir);
		
		assertTrue(fgo.getDir().equalsIgnoreCase(downDir));
	}

	@Test
	public void testGetFileName() {
		User user = new User("127.0.0.1", "127.0.0.1", "µ");
		String path = "C:\\lol\\";
		String filename = "trolol.lol";
		String downDir = "C:\\";
		FileGetObject fgo = new FileGetObject(user, path, filename, downDir);
		
		assertTrue(fgo.getFileName().equalsIgnoreCase(filename));
	}

	@Test
	public void testGetPath() {
		User user = new User("127.0.0.1", "127.0.0.1", "µ");
		String path = "C:\\lol\\";
		String filename = "trolol.lol";
		String downDir = "C:\\";
		FileGetObject fgo = new FileGetObject(user, path, filename, downDir);
		
		assertTrue(fgo.getPath().equalsIgnoreCase(path));
	}

}
