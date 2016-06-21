package modeltests.model.logic;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import model.logic.Directory;

import org.junit.Test;

public class DirectoryTest {

	@Test
	public void testGetDir() {
		Directory dir = new Directory("C:\\");
		assertNotNull(dir.getDir());
	}

	@Test
	public void testGetFileInformation() {
		Directory dir = new Directory("C:\\");
		assertNotNull(dir.getFileInformation());
		assertFalse(dir.getFileInformation().contains(null));
	}

	@Test
	public void testUpdateFiles(){
		
		/*
		 * Test the update by adding another file to a directory and
		 * see if it appears. 
		 */
		
		File file = new File("hej");
		file.mkdir();
		
		Directory dir = new Directory(file.getAbsolutePath());
		int firstNumber = dir.getFileInformation().size();
		
		try {
			File.createTempFile("lol", ".lol", file);
		}
		catch(IOException e){
			fail(e.getMessage()+", (error in testcase)");	//	error, return fail
		}
		
		dir.updateFiles();
		
		assertTrue(firstNumber+1 == dir.getFileInformation().size());
	}

}
