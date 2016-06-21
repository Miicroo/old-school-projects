/**
 * Class for Edge in Kruskal's algorithm.
 * The KruskalEdge has a weight that shows how must this edge will cost.
 * The KruskalEdge is compared to other Kruskaledge's depending on the weight;
 * the lesser weight, the cheaper is the edge.
 * 
 * @author Magnus Larsson and Andreas Wånge. Group 5.
 *
 * @param <E extends Edge> The specified Edge type.
 */
public class KruskalEdge<E extends Edge> implements Comparable<KruskalEdge<E>> {

	private double weight;
	private E edge;

	public KruskalEdge(E edge, double w){
		this.edge = edge;
		weight = w;
	}

	@Override
	public int compareTo(KruskalEdge<E> o) {
		if (weight > o.weight) {
			return 1;
		} else if (weight < o.weight) {
			return -1;
		} else {
			return 0;
		}
	}
	
	/**
	 * Gets the edge as a raw type.
	 * 
	 * @return the edge.
	 */
	public E getEdge(){
		return edge;
	}
	
	/**
	 * Gets the start node from the edge.
	 * 
	 * @return the start node.
	 */
	public int getFrom(){
		return edge.from;
	}
	
	/**
	 * Gets the end node from the edge.
	 *
	 * @return the end node.
	 */
	public int getTo(){
		return edge.to;
	}
}