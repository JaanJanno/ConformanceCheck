package ee.ut.jaanjanno.conformancechecker.petrinet;

import java.util.Date;
import java.util.Map;

public class Event {
	
	private Transition transition;
	@SuppressWarnings("unused")
	private String name;
	@SuppressWarnings("unused")
	private Date timeStamp;
	@SuppressWarnings("unused")
	private Map<String, EventAttribute> eventAttributes;
	
	public Transition getTransition() {
		return transition;
	}

}