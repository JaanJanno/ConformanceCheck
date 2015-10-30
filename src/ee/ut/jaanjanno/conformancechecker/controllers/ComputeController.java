package ee.ut.jaanjanno.conformancechecker.controllers;

import java.util.List;

import ee.ut.jaanjanno.conformancechecker.log.Case;
import ee.ut.jaanjanno.conformancechecker.log.Counter;
import ee.ut.jaanjanno.conformancechecker.log.Event;
import ee.ut.jaanjanno.conformancechecker.log.EventLog;
import ee.ut.jaanjanno.conformancechecker.petrinet.PetriNet;
import ee.ut.jaanjanno.conformancechecker.petrinet.Transition;

public class ComputeController {
	
	/*
	 * Class for doing computation tasks.
	 */
	
	public static float fitness(PetriNet petri, EventLog log) {
		float missingsSum = 0;
		float consumedSum = 0;
		float remaininSum = 0;
		float producedSum = 0;
		for (Case c : log.getCases()) {
			Counter counter = playTrace(petri, c.getTrace().getEvents());
			float n = c.getDuplicateCaseCount();
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
			float n = c.getDuplicateCaseCount();
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
	
	/*
	 * Plays through given events on petri net.
	 * Returns a counter that describes end state
	 * on petri net.
	 */

	private static Counter playTrace(PetriNet petri, List<Event> events) {
		Counter counter = new Counter();
		petri.setInitialMarking();
		for (Event e : events) {
			e.getLabelling().fire(counter);
		}
		if (petri.getEnd().getTokens() > 0) {
			counter.remainingTokens -= 1;
		} else {
			counter.missingTokens += 1;
		}
		return counter;
	}
	
	/*
	 * Sums enabled transitions over states surrounding events
	 * divides by their number to get the mean amount.
	 */

	private static float getMeanEnabledTransitions(PetriNet petri, List<Event> events) {
		Counter counter = new Counter();
		petri.setInitialMarking();
		float enabled = countEnabledTransitions(petri);
		for (Event e : events) {
			e.getLabelling().fire(counter);
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