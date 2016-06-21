import java.util.List;

/**
 * Class for representing a comparable Edge.
 * The Edge contains information for finding a path back to a certain start node,
 * and how much this path costs.
 * The Edge is Comparable depending on the weight; the lesser weight the cheaper
 * way.
 * 
 * @author Magnus Larsson and Andreas Wånge. Group 5.
 *
 * @param <E extends Edge> The specified Edge type.
 */

public class ComparableEdge<E extends Edge> implements Comparable<ComparableEdge> {

	private int node;
	private double costFromStart;
	private List<E> pathFromStart;

	public ComparableEdge(int node, double weight, List<E> path) {
		this.node = node;
		this.costFromStart = weight;
		pathFromStart = path;
	}
	
	public int getNode() {
		return node;
	}

	public double getCostFromStart() {
		return costFromStart;
	}

	public List<E> getPathFromStart() {
		return pathFromStart;
	}

	@Override
	public int compareTo(ComparableEdge o) {
		if (costFromStart > o.costFromStart) {
			return 1;
		} else if (costFromStart < o.costFromStart) {
			return -1;
		} else {
			return 0;
		}
	}
}