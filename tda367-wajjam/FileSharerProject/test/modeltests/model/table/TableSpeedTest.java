package modeltests.model.table;

import static org.junit.Assert.*;

import model.table.TableSpeed;

import org.junit.Test;

public class TableSpeedTest {

	@Test
	public void testGetSpeed() {
		double speed = 45000;
		
		String[] speedunit = {"B/s", "KB/s", "MB/s", "GB/s"};
		
		for(int i = speedunit.length-1; i > -1; i--){
			if(speed/(Math.pow(1024, i)) >= 1.0){
				speed /= (Math.pow(1024, i));
				
				int s = (int)(speed*100);
				speed = s/100.0;
				
				break;
			}
		}
		
		int s = (int)(speed*100);
		speed = s/100.0;
		
		TableSpeed table = new TableSpeed(speed);
		
		assertTrue(table.getSpeed() == speed);
	}

	@Test
	public void testGetSufflix() {
		double speed = 45000;
		
		String speedu = "B/s";
		String[] speedunit = {"B/s", "KB/s", "MB/s", "GB/s"};
		
		for(int i = speedunit.length-1; i > -1; i--){
			if(speed/(Math.pow(1024, i)) >= 1.0){
				speedu = speedunit[i];				
				break;
			}
		}
		
		TableSpeed table = new TableSpeed(speed);
		
		assertTrue(table.getSufflix().equalsIgnoreCase(speedu));
	}

	@Test
	public void testGetMspeed() {
		double speed = 45000;		
		TableSpeed table = new TableSpeed(speed);
		
		assertTrue(table.getMspeed() == speed);
	}

	@Test
	public void testCompareTo() {
		int speed1 = 20000;
		int speed2 = 12000;
		
		TableSpeed table1 = new TableSpeed(speed1);
		TableSpeed table2 = new TableSpeed(speed2);
		
		assertTrue(table1.compareTo(table2) > 0);
		assertTrue(table1.compareTo(table1) == 0);
		assertTrue(table2.compareTo(table1) < 0);
	}

	@Test
	public void testToString() {
		double speed = 45000;		
		TableSpeed table = new TableSpeed(speed);
		
		assertNotNull(table.toString());
	}
}
