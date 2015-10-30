package ee.ut.jaanjanno.conformancechecker.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.processmining.models.connections.GraphLayoutConnection;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.pnml.Pnml;

import ee.ut.jaanjanno.conformancechecker.parsers.PnmlImportUtils;
import ee.ut.jaanjanno.conformancechecker.petrinet.Arc;
import ee.ut.jaanjanno.conformancechecker.petrinet.PetriNet;

public class PetriReadController {

	public static PetriNet parsePetriNet(String address) {			
		try {
			PetrinetGraph net = getPetrinetGraph(address);

			Collection<Place> places = net.getPlaces();
			Map<Place, ee.ut.jaanjanno.conformancechecker.petrinet.Place> placeMap;
			placeMap = getPlaceMap(places);

			Collection<Transition> transitions = net.getTransitions();
			Map<Transition, ee.ut.jaanjanno.conformancechecker.petrinet.Transition> transMap;
			transMap = getTransitionMap(transitions);

			Set<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edges = net.getEdges();
			
			Set<Arc> petriArcs = getArcSet(edges, placeMap, transMap);
			Set<ee.ut.jaanjanno.conformancechecker.petrinet.Transition> transSet = getSet(transMap);
			Set<ee.ut.jaanjanno.conformancechecker.petrinet.Place> placeSet = getSet(placeMap);

			Map<String, ee.ut.jaanjanno.conformancechecker.petrinet.Transition> eventLabelMap = new HashMap<>();
			for (Transition transition : transitions) {
				eventLabelMap.put(transition.getLabel(), transMap.get(transition));
			}	
			return new PetriNet(placeSet, transSet, petriArcs, eventLabelMap);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Returns a set of all possible values of type T in the map,
	 * which was mapped by types V.
	 */
	
	private static <V, T> Set<T> getSet(Map<V, T> map) {
		Set<T> set = new HashSet<>();
		for(V t: map.keySet()){
			set.add(map.get(t));
		}
		return set;
	}
	
	/*
	 * Build all possible arcs between transitions and places.
	 */

	private static Set<Arc> getArcSet(Set<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edges,
			Map<Place, ee.ut.jaanjanno.conformancechecker.petrinet.Place> placeMap,
			Map<Transition, ee.ut.jaanjanno.conformancechecker.petrinet.Transition> transMap) {
		Set<Arc> arcs = new HashSet<>();
		for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> n : edges) {
			PetrinetNode source = (PetrinetNode) n.getSource();
			PetrinetNode target = (PetrinetNode) n.getTarget();
			if (source instanceof Place && target instanceof Transition) {
				Arc arc = new Arc(placeMap.get((Place) source), transMap.get((Transition) target));
				transMap.get((Transition) target).addInput(arc);
				arcs.add(arc);
			}
			if (source instanceof Transition && target instanceof Place) {
				Arc arc = new Arc(transMap.get((Transition) source), placeMap.get((Place) target));
				transMap.get((Transition) source).addOutput(arc);
				arcs.add(arc);
			}
		}
		return arcs;
	}
	
	/*
	 * Maps transitions to their raw counterpart type in the parsed petrinet.
	 */

	private static Map<Transition, ee.ut.jaanjanno.conformancechecker.petrinet.Transition> getTransitionMap(
			Collection<Transition> transitions) {
		Map<Transition, ee.ut.jaanjanno.conformancechecker.petrinet.Transition> transMap = new HashMap<>();
		for (Transition t : transitions) {
			ee.ut.jaanjanno.conformancechecker.petrinet.Transition newTans = new ee.ut.jaanjanno.conformancechecker.petrinet.Transition();
			transMap.put(t, newTans);
		}
		return transMap;
	}
	
	
	/*
	 * Maps places to their raw counterpart type in the parsed petrinet.
	 */

	private static Map<Place, ee.ut.jaanjanno.conformancechecker.petrinet.Place> getPlaceMap(Collection<Place> places) {
		Map<Place, ee.ut.jaanjanno.conformancechecker.petrinet.Place> placeMap = new HashMap<>();
		for (Place p : places) {
			ee.ut.jaanjanno.conformancechecker.petrinet.Place newPlace = new ee.ut.jaanjanno.conformancechecker.petrinet.Place(
					p.getLabel());
			placeMap.put(p, newPlace);
		}
		return placeMap;
	}
	
	/*
	 * Reads in the file and converts to its own raw petrinet type
	 * which will be used for generating a new one.
	 */

	private static PetrinetGraph getPetrinetGraph(String name) throws Exception {
		File f = new File(name);
		PnmlImportUtils ut = new PnmlImportUtils();
		InputStream input = new FileInputStream(f);
		Pnml pnml = ut.importPnmlFromStream(input, f.getName(), f.length());
		PetrinetGraph net = PetrinetFactory
				.newInhibitorNet(pnml.getLabel() + " (imported from " + f.getName() + ")");
		Marking marking = new Marking();
		pnml.convertToNet(net, marking, new GraphLayoutConnection(net));
		return net;
	}

}
