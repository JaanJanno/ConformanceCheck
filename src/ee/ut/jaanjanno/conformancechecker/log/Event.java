package ee.ut.jaanjanno.conformancechecker.log;

import java.util.Date;
import java.util.Map;

import ee.ut.jaanjanno.conformancechecker.petrinet.Transition;

public class Event {
	
	private Transition labelling;
	private String name;
	@SuppressWarnings("unused")
	private Date timeStamp;
	@SuppressWarnings("unused")
	private Map<String, EventAttribute> eventAttributes;	

	public Event(Transition transition, String name, Date timeStamp) {
		super();
		this.labelling = transition;
		this.name = name;
		this.timeStamp = timeStamp;
	}

	public Transition getLabelling() {
		return labelling;
	}

	public String getName() {
		return name;
	}

}