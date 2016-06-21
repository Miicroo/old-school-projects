package model.statusbar;


import model.state.ApplicationState;
import model.traffic.DataDirection;
/**
 * 
 * @author Johns lap
 *	Class with static methods for getting the total up and down speed
 */
public class StatusbarData {

	private static double downSpeed = 0;

	public static String getDownSpeed() {
		downSpeed = 0;
		String speed = " B/s";
		for (int i = 0; i < ApplicationState.getInstance().getTrafficList()
				.size(); i++) {
			if (ApplicationState.getInstance().getTrafficList().get(i).getDirection() == DataDirection.DOWN) {
				downSpeed = downSpeed
						+ ApplicationState.getInstance().getTrafficList()
								.get(i).getSpeed();
			}
		}

		if(downSpeed/(1024*1024*1024)>=1){
			downSpeed = downSpeed/(1024*1024*1024);
			speed = " GB/s";
		}else if(downSpeed/(1024*1024)>=1){
			downSpeed = downSpeed/(1024*1024);			
			speed = " MB/s";			
		}else if(downSpeed/(1024)>=1){
			downSpeed = downSpeed/(1024);	
			speed = " KB/s";
		}
		
		int s = (int)(downSpeed*100);
		downSpeed = s/100.0;

		return downSpeed+speed;
	}

	public static String getUpSpeed() {
		downSpeed = 0;
		String speed = " B/s";
		for (int i = 0; i < ApplicationState.getInstance().getTrafficList()
				.size(); i++) {
			if (ApplicationState.getInstance().getTrafficList().get(i).getDirection() == DataDirection.UP) {
				downSpeed = downSpeed
						+ ApplicationState.getInstance().getTrafficList()
								.get(i).getSpeed();
			}
		}
		if(downSpeed/(1024*1024*1024)>=1){
			downSpeed = downSpeed/(1024*1024*1024);
			speed = " GB/s";
		}else if(downSpeed/(1024*1024)>=1){
			downSpeed = downSpeed/(1024*1024);			
			speed = " MB/s";			
		}else if(downSpeed/(1024)>=1){
			downSpeed = downSpeed/(1024);	
			speed = " KB/s";
		}
		
		int s = (int)(downSpeed*100);
		downSpeed = s/100.0;

		return downSpeed+speed;
	}
}