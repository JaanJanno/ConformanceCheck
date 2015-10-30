package ee.ut.jaanjanno.conformancechecker.log;

import java.util.HashMap;

public class Case {
	
	private Trace trace;
	private int duplicateCaseCount;
	@SuppressWarnings("unused")
	private HashMap<String, CaseAttribute> caseAttributes;
	
	public Case(Trace trace, int duplicateCases) {
		super();
		this.trace = trace;
		this.duplicateCaseCount = duplicateCases;
	}

	public Trace getTrace() {
		return trace;
	}

	public int getDuplicateCaseCount() {
		return duplicateCaseCount;
	}
	
	@Override
	public String toString() {
		String r = Integer.toString(duplicateCaseCount) + " * ";
		for(Event e : trace.getEvents()) {
			r += e.getName();
		}
		return r;
	}

}