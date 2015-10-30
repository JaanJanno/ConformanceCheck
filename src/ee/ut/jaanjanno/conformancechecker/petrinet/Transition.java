package ee.ut.jaanjanno.conformancechecker.petrinet;

import java.util.HashSet;
import java.util.Set;

import ee.ut.jaanjanno.conformancechecker.log.Counter;

public class Transition implements Node {

	private Set<Arc> inputPlaces;
	private Set<Arc> outputPlaces;

	public Transition() {
		this.inputPlaces = new HashSet<>();
		this.outputPlaces = new HashSet<>();
	}
	
	public void addInput(Arc arc) {
		inputPlaces.add(arc);
	}
	
	public void addOutput(Arc arc) {
		outputPlaces.add(arc);
	}

	public boolean isEnabled() {
		for (Arc arc : inputPlaces) {
			if (((Place) arc.getSource()).getTokens() == 0)
				return false;
		}
		return true;
	}

	public void fire(Counter c) {
		fireInputs(c);
		fireOutputs(c);
	}

	private void fireInputs(Counter c) {
		for (Arc arc : inputPlaces) {
			Place input = (Place) arc.getSource();
			if (input.getTokens() > 0) {
				input.consumeToken();
				c.remainingTokens--;
			} else {
				c.missingTokens++;
			}
			c.consumedTokens++;

		}
	}

	private void fireOutputs(Counter c) {
		for (Arc arc : outputPlaces) {
			Place output = (Place) arc.getDestination();
			output.produceToken();
			c.producedTokens++;
			c.remainingTokens++;
		}
	}

	public Set<Arc> getInputPlaces() {
		return inputPlaces;
	}

	public Set<Arc> getOutputPlaces() {
		return outputPlaces;
	}

}