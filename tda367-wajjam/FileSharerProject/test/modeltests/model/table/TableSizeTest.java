package modeltests.model.table;

import static org.junit.Assert.*;

import model.table.TableSize;

import org.junit.Test;

public class TableSizeTest {

	@Test
	public void testGetSize() {
		double size = 45000;
		
		String[] sizeunit = {"B", "KB", "MB", "GB"};
		
		for(int i = sizeunit.length-1; i > -1; i--){
			if(size/(Math.pow(1024, i)) >= 1.0){
				size /= (Math.pow(1024, i));
				
				int s = (int)(size*100);
				size = s/100.0;
				
				break;
			}
		}
		
		int s = (int)(size*100);
		size = s/100.0;
		
		TableSize table = new TableSize(size);
		
		assertTrue(table.getSize() == size);
	}

	@Test
	public void testGetSufflix() {
		double size = 45000;
		
		String sizeu = "B";
		String[] sizeunit = {"B", "KB", "MB", "GB"};
		
		for(int i = sizeunit.length-1; i > -1; i--){
			if(size/(Math.pow(1024, i)) >= 1.0){
				sizeu = sizeunit[i];				
				break;
			}
		}
		
		TableSize table = new TableSize(size);
		
		assertTrue(table.getSufflix().equalsIgnoreCase(sizeu));
	}

	@Test
	public void testGetMsize() {

		double size = 45000;		
		TableSize table = new TableSize(size);
		
		assertTrue(table.getMsize() == size);
	}

	@Test
	public void testCompareTo() {
		int speed1 = 20000;
		int speed2 = 12000;
		
		TableSize table1 = new TableSize(speed1);
		TableSize table2 = new TableSize(speed2);
		
		assertTrue(table1.compareTo(table2) > 0);
		assertTrue(table1.compareTo(table1) == 0);
		assertTrue(table2.compareTo(table1) < 0);
	}

	@Test
	public void testToString() {
		double size = 45000;		
		TableSize table = new TableSize(size);
		
		assertNotNull(table.toString());
	}
}
