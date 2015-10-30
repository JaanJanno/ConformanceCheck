package ee.ut.jaanjanno.conformancechecker.log;

import java.util.Date;
import java.util.Map;

import ee.ut.jaanjanno.conformancechecker.petrinet.Transition;

public class Event {
	
	private Transition transition;
	private String name;
	@SuppressWarnings("unused")
	private Date timeStamp;
	@SuppressWarnings("unused")
	private Map<String, EventAttribute> eventAttributes;	

	public Event(Transition transition, String name, Date timeStamp) {
		super();
		this.transition = transition;
		this.name = name;
		this.timeStamp = timeStamp;
	}

	public Transition getTransition() {
		return transition;
	}

	public String getName() {
		return name;
	}

}