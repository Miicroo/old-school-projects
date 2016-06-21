package modeltests.model.logic;

import static org.junit.Assert.*;

import java.io.File;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.PathChecker;

import model.logic.DirectoryTreeLogics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DirectoryTreeLogicsTest {

	PathChecker checker;
	
	@Before
	public void setUp(){
		checker = new PathChecker();
		checker.keep(new File("C:\\Adabas\\")); //	CHANGE THIS TO A SMALLER DIRECTORY IF YOU DON't HAVE ADABAS
	}
	
	@Test
	public void testDirectoryTreeLogics() {
		DirectoryTreeLogics logics = new DirectoryTreeLogics(checker);
		
		assertNotNull(logics.getAbstractRoot());
		assertNotNull(logics.getRealRoot());
	}

	@Test
	public void testGetAbstractRoot() {
		DirectoryTreeLogics logics = new DirectoryTreeLogics(checker);
		
		assertNotNull(logics.getAbstractRoot());
	}

	@Test
	public void testGetRealRoot() {
		DirectoryTreeLogics logics = new DirectoryTreeLogics(checker);
		
		assertNotNull(logics.getRealRoot());
	}

	@After
	public void destroy(){
		checker = null;
	}
}
