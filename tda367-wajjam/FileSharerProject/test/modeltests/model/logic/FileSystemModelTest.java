package modeltests.model.logic;

import static org.junit.Assert.*;

import java.io.File;

import model.filter.AcceptingFileFilter;
import model.logic.FileSystemModel;

import org.junit.Test;

public class FileSystemModelTest {
	
	@Test
	public void testGetRoot() {
		FileSystemModel model = new FileSystemModel(File.listRoots());
		
		assertNotNull(model.getRoot());
	}

	@Test
	public void testGetChild() {
		FileSystemModel model = new FileSystemModel(File.listRoots());
		
		int index = 0;
		File file = File.listRoots()[index];
		
		assertTrue(((File)(model.getChild(model.getRoot(), index))).equals(file));
	}

	@Test
	public void testGetChildCount() {
		FileSystemModel model = new FileSystemModel(File.listRoots());
		
		int count = File.listRoots().length;
		
		assertTrue(model.getChildCount(model.getRoot()) == count);
	}

	@Test
	public void testIsLeaf(){
		FileSystemModel model = new FileSystemModel(File.listRoots());
		
		assertFalse(model.isLeaf(model.getRoot()));
		
		File file = File.listRoots()[0];
		File f = file.listFiles(new AcceptingFileFilter())[0];
		
		assertTrue(model.isLeaf(f));
	}
	
	@Test
	public void testGetIndexOfChild() {
		FileSystemModel model = new FileSystemModel(File.listRoots());
		
		Object root = model.getChild(model.getRoot(), 0);
		int index = 0;
		
		assertTrue(model.getIndexOfChild(root, model.getChild(root, index)) == index);
	}

	@Test
	public void testValueForPathChanged() {
		assertTrue(true);
	}

	@Test
	public void testAddTreeModelListener() {
		assertTrue(true);
	}

	@Test
	public void testRemoveTreeModelListener() {
		assertTrue(true);
	}
}
