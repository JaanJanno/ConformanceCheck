package ee.ut.jaanjanno.conformancechecker.petrinet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PetriNet {
	
	private Set<Place> places;
	private Set<Transition> transitions;
	private Set<Arc> arcs;
	
	private Place begin;
	private Place end;
	
	private Map<String, ee.ut.jaanjanno.conformancechecker.petrinet.Transition> labels;
	
	@SuppressWarnings("unused")
	private int tokens;
	
	public PetriNet(Set<Place> places, Set<Transition> transitions, Set<Arc> arcs, Map<String, Transition> labels) {
		this.places = places;
		this.transitions = transitions;
		this.arcs = arcs;
		this.tokens = 1;
		this.labels = labels;
		setExtremes();
		setInitialMarking();	
	}
	
	public Map<String, ee.ut.jaanjanno.conformancechecker.petrinet.Transition> getLabels() {
		return labels;
	}

	private void setExtremes() {
		Map<Place, Integer> inputs = new HashMap<>();
		Map<Place, Integer> outputs = new HashMap<>();
		for(Place p: places) {
			inputs.put(p, 0);
			outputs.put(p, 0);
		}
		for(Arc a: arcs) {
			incrementIn(inputs, a.getDestination());
			incrementIn(outputs, a.getSource());
		}
		setBegin(inputs);
		setEnd(outputs);
	}
	
	private void setBegin(Map<Place, Integer> inputs) {
		for(Place p: inputs.keySet()) {
			if(inputs.get(p) == 0) {
				begin = p;
				break;
			}
		}
	}
	
	private void setEnd(Map<Place, Integer> outputs) {
		for(Place p: outputs.keySet()) {
			if(outputs.get(p) == 0) {
				end = p;
				break;
			}
		}
	}

	private void incrementIn(Map<Place, Integer> map, Node connection) {
		if(connection instanceof Place) {
			Integer currentValue = map.get(connection);
			map.put((Place)connection, currentValue+1);
		}
	}

	public void setInitialMarking() {
		for(Place place: places){
			place.setTokens(0);
		}
		begin.setTokens(1);
	}

	public Place getEnd() {
		return end;
	}

	public Set<Transition> getTransitions() {
		return transitions;
	}

	public Set<Place> getPlaces() {
		return places;
	}

}