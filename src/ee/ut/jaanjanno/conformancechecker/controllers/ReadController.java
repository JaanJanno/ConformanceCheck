package ee.ut.jaanjanno.conformancechecker.controllers;

import ee.ut.jaanjanno.conformancechecker.log.EventLog;
import ee.ut.jaanjanno.conformancechecker.petrinet.PetriNet;

public class ReadController {
	
	private static final String testXES;
	private static final String testNML;

	static {
		testXES = "C:\\Users\\Jaan\\OneDrive\\Documents\\UT-Ained\\Süsteemide modelleerimine\\Homework3\\tests\\test.xes";
		testNML = "C:\\Users\\Jaan\\OneDrive\\Documents\\UT-Ained\\Süsteemide modelleerimine\\Homework3\\tests\\test.pnml";
	}
	
	public static void main(String[] args) {

		try {
			PetriNet p = PetriReadController.parsePetriNet(testNML);
			EventLog l = LogReadController.parseLog(testXES, p.getLabels());

			System.out.println(l.getCases().iterator().next().getTrace().getEvents().iterator().next().getTransition());
			
			float c = Controller.fitness(p, l);
			System.out.println(c);
			
			float b = Controller.simpleBehavioralAppropriateness(p, l);
			System.out.println(b);
			
			float a = Controller.simpleStructuralAppropriateness(p);
			System.out.println(a);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
