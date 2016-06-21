import java.util.*;

/**
 * This class models a graph. It takes the number of nodes as a parameter to its constructor
 * and then edges between them are supplied through <code>addEdge(E e)</code>.
 * 
 * With this information, it can then supply the shortest path between two nodes in the graph,
 * and the minimum spanning tree of the graph. Both are returned as an Iterator over type E
 * i.e. an Iterator over the edges that are in the subset of all edges that were computed
 * by either method.
 * 
 * @author Magnus Larsson and Andreas Wånge
 * @param <E> a subclass of {@link Edge} to be used to model the edges of this graph
 */
public class DirectedGraph<E extends Edge> {

	private final int noOfNodes;
	private List<E> edges = new LinkedList<E>();
	
	//We're using an array of List's instead of a two-dimensional array. Firstly, it seems not
	//possible to instantiate an array of generic type (not even for lists, which is why we have
	//the suppressWarnings); and secondly, this allows us to use LinkedList to save space, as we
	//won't know how many lines will go from each station, and this will save us from having to
	//default-initialize the inner arrays to the size where they can handle a node connected
	//to all other nodes.
	private List<E>[] destinations;
	
	@SuppressWarnings("unchecked")
	public DirectedGraph(int noOfNodes) {
		this.noOfNodes = noOfNodes;
		destinations = new LinkedList[noOfNodes];
	}

	public void addEdge(E e) {
		edges.add(e);
		destinations[e.from].add(e);
	}
	
	/**
	 * Finds the shortest path to a node to from a node from
	 * 
	 * @param from the node to start at.
	 * @param to the destination to arrive to.
	 * @return an Iterator with the nodes that are in the path.
	 */
	public Iterator<E> shortestPath(int from, int to) {
		
		PriorityQueue<ComparableEdge<E>> queue = new PriorityQueue<ComparableEdge<E>>();
		ArrayList<Integer> visited = new ArrayList<Integer>();
		
		ComparableEdge<E> start = new ComparableEdge<E>(from, 0.0, new LinkedList<E>());
		queue.add(start);		
		
		ComparableEdge<E> node;
		while((node = queue.poll()) != null){
			int nodeNo = node.getNode();
			if(!visited.contains(nodeNo)){
				if(nodeNo == to){
					break;
				}
				visited.add(nodeNo);
				for(E i : destinations[nodeNo]){
					if(!visited.contains(i.to)){
						List<E> oldPath = new LinkedList<E>(node.getPathFromStart());
						oldPath.add(i);
						queue.add(new ComparableEdge<E>(i.to, node.getCostFromStart() + i.getWeight(), oldPath));
					}
				}
			}
		}
		return node.getPathFromStart().iterator();
	}

	/**
	 * Finds the MST of this graph.
	 * 
	 * @return an iterator over the nodes in the MST.
	 */
	public Iterator<E> minimumSpanningTree() {
		PriorityQueue<KruskalEdge<E>> queue = new PriorityQueue<KruskalEdge<E>>();
		for(E edge : edges){
			queue.add(new KruskalEdge<E>(edge, edge.getWeight()));
		}
		
		@SuppressWarnings("unchecked")
		LinkedList<E>[] cc = (LinkedList<E>[]) new LinkedList[noOfNodes];
		for(int i = 0; i<cc.length; i++){
			cc[i] = new LinkedList<E>(); 
		}

		KruskalEdge<E> edge = queue.peek();
		int longList = (cc[edge.getFrom()].size() > cc[edge.getTo()].size() ? edge.getFrom() : edge.getTo());
		int shortList = (longList == edge.getFrom() ? edge.getTo() : edge.getFrom());
		
		while((edge = queue.poll()) != null && cc[longList].size() < noOfNodes-1){
			if(cc[edge.getFrom()] != cc[edge.getTo()]){
				longList = (cc[edge.getFrom()].size() > cc[edge.getTo()].size() ? edge.getFrom() : edge.getTo());
				shortList = (longList == edge.getFrom() ? edge.getTo() : edge.getFrom());
				
				for(E element : cc[shortList]){
					cc[longList].add(element);
					cc[element.from] = cc[longList];
					cc[element.to] = cc[longList];
				}
				
				cc[longList].add(edge.getEdge());
				cc[shortList] = cc[longList];
			}
		}
		
		return cc[longList].iterator();
	}
}