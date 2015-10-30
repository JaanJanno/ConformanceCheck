package ee.ut.jaanjanno.conformancechecker.log;

import java.util.List;

public class Trace {
	
	private List<Event> events;

	public Trace(List<Event> events) {
		this.events = events;
	}

	public List<Event> getEvents() {
		return events;
	}

}