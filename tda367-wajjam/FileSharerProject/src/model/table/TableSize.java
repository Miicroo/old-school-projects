package model.table;

/**
 * 
 * @author Johns lap A object that files size is written out and can be sorted
 *         for tables
 */
public class TableSize implements Comparable<TableSize> {

	private String sufflix = "";
	private double size = 0.0;
	private double msize = 0.0;

	public TableSize(double size) {
		this.msize = size;
		this.sufflix = "B";
		if (size / (1024 * 1024 * 1024) >= 1) {
			size = size / (1024 * 1024 * 1024);
			this.sufflix = "GB";
		} else if (size / (1024 * 1024) >= 1) {
			size = size / (1024 * 1024);
			this.sufflix = "MB";
		} else if (size / (1024) >= 1) {
			size = size / (1024);
			this.sufflix = "KB";
		}
		int sizea = (int) (size * 100);
		this.size = sizea / 100.0;
	}

	public double getSize() {
		return size;
	}

	public String getSufflix() {
		return sufflix;
	}

	public double getMsize() {
		return msize;
	}

	@Override
	public int compareTo(TableSize o) {
		if (getMsize() > o.getMsize()) {
			return 1;
		} else if (o.getMsize() == getMsize()) {
			return 0;
		}
		return -1;

	}

	@Override
	public String toString() {
		return size + " " + sufflix;
	}
}
