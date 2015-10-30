package ee.ut.jaanjanno.conformancechecker.log;

import java.util.HashMap;

public class Case {
	
	private Trace trace;
	private int duplicateTraces;
	
	public Trace getTrace() {
		return trace;
	}

	public int getDuplicateTraces() {
		return duplicateTraces;
	}

	@SuppressWarnings("unused")
	private HashMap<String, CaseAttribute> caseAttributes;

}