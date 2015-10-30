package ee.ut.jaanjanno.conformancechecker.petrinet;

public class Arc {
	
	private Node source;
	private Node destination;
	
	public Arc(Node source, Node destination) {
		this.source = source;
		this.destination = destination;
	}

	public Node getSource() {
		return source;
	}

	public Node getDestination() {
		return destination;
	}

}