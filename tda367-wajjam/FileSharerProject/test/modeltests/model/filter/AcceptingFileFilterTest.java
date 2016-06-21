package modeltests.model.filter;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import model.filter.AcceptingFileFilter;

import org.junit.Test;

public class AcceptingFileFilterTest {

	@Test
	public void testAccept() {
		File file = null;
		try{
			file = File.createTempFile("lol", ".lol");
		}
		catch(IOException e){
			fail(e.getMessage()+", (error in testcase)");	//	error, return fail
		}
		
		AcceptingFileFilter filter = new AcceptingFileFilter();
		
		assertTrue(filter.accept(file));
	}

}
