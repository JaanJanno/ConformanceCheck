package ee.ut.jaanjanno.conformancechecker.log;

import java.util.Set;

public class EventLog {
	
	private Set<Case> cases;

	public EventLog(Set<Case> cases) {
		super();
		this.cases = cases;
	}

	public Set<Case> getCases() {
		return cases;
	}

}
