package modeltests.model;

import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import model.ModelInitiater;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModelInitiaterTest {

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
	public void testGetPerformers() {
		ModelInitiater init = new ModelInitiater(obs);
		
		assertNotNull(init.getPerformers());
		assertNotNull(init.getPerformers().contains(null));
	}

	@Test
	public void testGetSuppliers() {
		ModelInitiater init = new ModelInitiater(obs);
		
		assertNotNull(init.getSuppliers());
		assertNotNull(init.getSuppliers().contains(null));
	}
	
	@After
	public void destroy(){
		obs = null;
	}
}
