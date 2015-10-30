package ee.ut.jaanjanno.conformancechecker.controllers;

import java.util.List;

import ee.ut.jaanjanno.conformancechecker.log.Case;
import ee.ut.jaanjanno.conformancechecker.log.Counter;
import ee.ut.jaanjanno.conformancechecker.log.Event;
import ee.ut.jaanjanno.conformancechecker.log.EventLog;
import ee.ut.jaanjanno.conformancechecker.petrinet.PetriNet;
import ee.ut.jaanjanno.conformancechecker.petrinet.Transition;

public class Controller {
	
	public static float fitness(PetriNet petri, EventLog log) {
		float missingsSum = 0;
		float consumedSum = 0;
		float remaininSum = 0;
		float producedSum = 0;
		for (Case c : log.getCases()) {
			Counter counter = playTrace(petri, c.getTrace().getEvents());
			float n = c.getDuplicateCases();
			missingsSum += n * counter.missingTokens;
			consumedSum += n * counter.consumedTokens;
			remaininSum += n * counter.remainingTokens;
			producedSum += n * counter.producedTokens;
		}
		return 0.5f * (1f - missingsSum / consumedSum) + 0.5f * (1f - remaininSum / producedSum);
	}
	
	public static float simpleBehavioralAppropriateness(PetriNet petri, EventLog log) {
		float T = petri.getTransitions().size();
		float numerator = 0;
		float denominatorSum = 0;
		for (Case c : log.getCases()) {
			float n = c.getDuplicateCases();
			float x = getMeanEnabledTransitions(petri, c.getTrace().getEvents());
			numerator += n * (T - x);
			denominatorSum += n;
		}
		return numerator / ((T - 1) * denominatorSum);
	}

	public static float simpleStructuralAppropriateness(PetriNet petri) {
		float places = petri.getPlaces().size();
		float transitions = petri.getTransitions().size();
		float N = transitions + places;
		float L = petri.getTransitions().size();
		return (L + 2) / N;
	}

	private static Counter playTrace(PetriNet petri, List<Event> events) {
		Counter counter = new Counter();
		petri.setInitialMarking();
		for (Event e : events) {
			e.getTransition().fire(counter);
		}
		return counter;
	}

	private static float getMeanEnabledTransitions(PetriNet petri, List<Event> events) {
		Counter counter = new Counter();
		petri.setInitialMarking();
		float enabled = countEnabledTransitions(petri);
		for (Event e : events) {
			e.getTransition().fire(counter);
			enabled += countEnabledTransitions(petri);
		}
		return enabled / (events.size());
	}

	private static int countEnabledTransitions(PetriNet petri) {
		int count = 0;
		for (Transition t : petri.getTransitions())
			if (t.isEnabled())
				count++;
		return count;
	}

}