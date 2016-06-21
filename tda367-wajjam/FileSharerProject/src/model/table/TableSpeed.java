package model.table;

/**
 * 
 * @author Johns lap A object that files speed is written out and can be sorted
 *         for tables
 */
public class TableSpeed implements Comparable<TableSpeed> {

	private String sufflix = "";
	private double speed = 0.0;
	private double mspeed = 0.0;

	public TableSpeed(double speed) {
		this.mspeed = speed;
		sufflix = "B/s";
		if (speed / (1024 * 1024 * 1024) >= 1) {
			speed = speed / (1024 * 1024 * 1024);
			sufflix = "GB/s";
		} else if (speed / (1024 * 1024) >= 1) {
			speed = speed / (1024 * 1024);
			sufflix = "MB/s";
		} else if (speed / (1024) >= 1) {
			speed = speed / (1024);
			sufflix = "KB/s";
		}
		int spe = (int) (speed * 100);
		this.speed = spe / 100.0;

	}

	public double getSpeed() {
		return speed;
	}

	public String getSufflix() {
		return sufflix;
	}

	public double getMspeed() {
		return mspeed;
	}

	@Override
	public int compareTo(TableSpeed o) {
		if (getMspeed() > o.getMspeed()) {
			return 1;
		} else if (o.getMspeed() == getMspeed()) {
			return 0;
		}
		return -1;

	}

	@Override
	public String toString() {
		return speed + " " + sufflix;
	}
}
