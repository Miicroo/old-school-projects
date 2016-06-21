package org.wajjam.project.server;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import User.User;

public class UserListTest {
	
	@Test
	public void testAddUser() {
		UserList list = UserList.getInstance();
		
		User a = new User("127.0.0.1", "127.0.0.1", "µ");
		list.addUser(a);
		
		assertTrue(list.getUserList().contains(a));
		
		list.removeUser(a);	//cleanup
	}

	@Test
	public void testGetUserList() {
		UserList list = UserList.getInstance();
		
		User a = new User("127.0.0.1", "127.0.0.1", "µ");
		list.addUser(a);
		
		
		assertTrue(list.getUserList().size() == 1);
		assertTrue(list.getUserList().get(0).equalsUserName(a));
		
		list.removeUser(a);	//cleanup
	}

	@Test
	public void testRemoveUser() {
		UserList list = UserList.getInstance();
		
		User a = new User("127.0.0.1", "127.0.0.1", "µ");
		list.addUser(a);
		
		assertTrue(list.getUserList().contains(a));
		
		list.removeUser(a);
		assertFalse(list.getUserList().contains(a));
	}

	@Test
	public void testGetInstance() {
		assertNotNull(UserList.getInstance());
	}

}
