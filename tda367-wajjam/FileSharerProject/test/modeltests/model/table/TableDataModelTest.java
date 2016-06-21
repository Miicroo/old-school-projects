package modeltests.model.table;

import static org.junit.Assert.*;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import model.state.ApplicationState;
import model.table.TableDataModel;

import org.junit.Test;

public class TableDataModelTest {
	
	@Test
	public void testAddTableModelListener() {
		TableDataModel model = new TableDataModel();
		model.addTableModelListener(new Listener());
		
		assertTrue(model.getAllListeners().size() == 1);
	}

	@Test
	public void testGetColumnClass() {
		TableDataModel model = new TableDataModel();
		for(int i = 0; i<model.getColumnCount(); i++){
			String classname = model.getColumnClass(i).getName();
			try{
				this.getClass().getClassLoader().loadClass(classname);
			}
			catch(ClassNotFoundException e){
				assertTrue(false);
				return;
			}
		}
		assertTrue(true);
	}

	@Test
	public void testGetColumnCount() {
		TableDataModel model = new TableDataModel();
		
		assertTrue(model.getColumnCount() == 8);
	}

	@Test
	public void testGetColumnName() {
		TableDataModel model = new TableDataModel();
		for(int i = 0; i<model.getColumnCount(); i++){
			assertNotNull(model.getColumnName(i));
		}
	}

	@Test
	public void testGetRowCount() {
		int size = ApplicationState.getInstance().getTrafficList().size();
		TableDataModel model = new TableDataModel();
		
		assertTrue(model.getRowCount() == size);
	}

	@Test
	public void testGetValueAt() {
		/*
		 * Can't be tested since the update handles the get/set
		 */
		assertTrue(true);
	}

	@Test
	public void testIsCellEditable() {
		TableDataModel model = new TableDataModel();
		assertFalse(model.isCellEditable(0, 0));
	}

	@Test
	public void testRemoveTableModelListener() {
		TableDataModel model = new TableDataModel();
		Listener l = new Listener();
		model.addTableModelListener(l);
		
		assertTrue(model.getAllListeners().size() == 1);
		
		model.removeTableModelListener(l);
		
		assertTrue(model.getAllListeners().size() == 0);
	}

	@Test
	public void testSetValueAt() {
		/*
		 * Can't be tested since the update handles the get/set
		 */
		assertTrue(true);
	}

	@Test
	public void testUpdate() {
		/*
		 * Can't be tested since this requires ApplicationState and a running server
		 */
		assertTrue(true);
	}

	class Listener implements TableModelListener{

		@Override
		public void tableChanged(TableModelEvent e) {
			
		}
	}
}
