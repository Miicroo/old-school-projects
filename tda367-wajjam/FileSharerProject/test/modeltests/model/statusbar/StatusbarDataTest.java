package modeltests.model.statusbar;

import static org.junit.Assert.*;

import model.state.ApplicationState;
import model.statusbar.StatusbarData;
import model.traffic.DataDirection;

import org.junit.Test;

public class StatusbarDataTest {

	@Test
	public void testGetDownSpeed() {
		double down = 0.0;
		
		for (int i = 0; i < ApplicationState.getInstance().getTrafficList().size(); i++) {
			if(ApplicationState.getInstance().getTrafficList().get(i).getDirection() == DataDirection.DOWN) {
				down += ApplicationState.getInstance().getTrafficList().get(i).getSpeed();
			}
		}
		
		String downSpeed = "";
		String[] speedunit = {"B/s", "KB/s", "MB/s", "GB/s"};
		
		for(int i = speedunit.length-1; i > -1; i--){
			if(down/(Math.pow(1024, i)) >= 1.0){
				down /= (Math.pow(1024, i));
				
				int s = (int)(down*100);
				down = s/100.0;
				
				downSpeed = down+" "+speedunit[i];
				break;
			}
		}
		
		if(downSpeed.indexOf("/s") == -1){
			int s = (int)(down*100);
			down = s/100.0;
			
			downSpeed = down+" "+speedunit[0];
		}
		
		StatusbarData data = new StatusbarData();
		
		assertTrue(data.getDownSpeed().equalsIgnoreCase(downSpeed));
	}

	@Test
	public void testGetUpSpeed() {
		double up = 0.0;
		
		for (int i = 0; i < ApplicationState.getInstance().getTrafficList().size(); i++) {
			if(ApplicationState.getInstance().getTrafficList().get(i).getDirection() == DataDirection.UP) {
				up += ApplicationState.getInstance().getTrafficList().get(i).getSpeed();
			}
		}
		
		String downSpeed = "";
		String[] speedunit = {"B/s", "KB/s", "MB/s", "GB/s"};
		
		for(int i = speedunit.length-1; i > -1; i--){
			if(up/(Math.pow(1024, i)) >= 1.0){
				up /= (Math.pow(1024, i));
				
				int s = (int)(up*100);
				up = s/100.0;
				
				downSpeed = up+" "+speedunit[i];
				break;
			}
		}
		
		if(downSpeed.indexOf("/s") == -1){
			int s = (int)(up*100);
			up = s/100.0;
			
			downSpeed = up+" "+speedunit[0];
		}
		
		StatusbarData data = new StatusbarData();
		
		assertTrue(data.getUpSpeed().equalsIgnoreCase(downSpeed));
	}

}
