package modeltests.model.filter;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import model.filter.DirectoryFilter;

import org.junit.Test;

public class DirectoryFilterTest {

	@Test
	public void testAccept() throws IOException {
		File file = new File("hej");
		file.mkdir();
		
		DirectoryFilter filter = new DirectoryFilter();
		
		assertTrue(filter.accept(file));
	}

}
