package modeltests.model.filter;

import static org.junit.Assert.*;

import java.io.File;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.PathChecker;

import model.filter.CheckedDirectoryFilter;

import org.junit.Test;

public class CheckedDirectoryFilterTest {

	
	@Test
	public void testAccept() {
		PathChecker checker = new PathChecker();
		File file = new File("hej");
		file.mkdir();
		
		checker.remove(file);
		
		CheckedDirectoryFilter filter = new CheckedDirectoryFilter(checker);
		
		assertFalse(filter.accept(file));
		
		checker.keep(file);
		
		assertTrue(filter.accept(file));
	}
}
