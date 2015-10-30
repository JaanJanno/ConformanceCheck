package ee.ut.jaanjanno.conformancechecker.log;

import java.util.HashMap;

public class Case {
	
	private Trace trace;
	private int duplicateCases;
	
	public Case(Trace trace, int duplicateCases) {
		super();
		this.trace = trace;
		this.duplicateCases = duplicateCases;
	}

	public Trace getTrace() {
		return trace;
	}

	public int getDuplicateCases() {
		return duplicateCases;
	}

	@SuppressWarnings("unused")
	private HashMap<String, CaseAttribute> caseAttributes;
	
	@Override
	public String toString() {
		String r = Integer.toString(duplicateCases) + " * ";
		for(Event e : trace.getEvents()) {
			r += e.getName();
		}
		return r;
	}

}