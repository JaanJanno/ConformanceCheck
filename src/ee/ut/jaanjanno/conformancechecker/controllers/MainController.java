package ee.ut.jaanjanno.conformancechecker.controllers;

import ee.ut.jaanjanno.conformancechecker.log.EventLog;
import ee.ut.jaanjanno.conformancechecker.petrinet.PetriNet;

public class MainController {
	
	private static String urlXES;
	private static String urlNML;

	static {
		urlXES = "test.xes";
		urlNML = "test.pnml";
	}
	
	public static void main(String[] args) {
		
		if(args.length < 2) {
			System.out.println("Enter file URL's to CMD as follows:");
			System.out.println("{java exec} MainController <PNML petrinet URL> <XES log URL>");
			System.out.println("Using default test files (works from eclipse).");
		} else {
			urlNML = args[0];
			urlXES = args[1];
		}

		try {
			PetriNet petriNet = PetriReadController.parsePetriNet(urlNML);
			EventLog log = LogReadController.parseLog(urlXES, petriNet.getLabels());
			
			float fitness = ComputeController.fitness(petriNet, log);
			System.out.println("Fitness:");
			System.out.println(fitness);
			
			float simpleBehavioralAppropriateness = ComputeController.simpleBehavioralAppropriateness(petriNet, log);
			System.out.println("Simple behavioral appropriateness");
			System.out.println(simpleBehavioralAppropriateness);
			
			float simpleStructuralAppropriateness = ComputeController.simpleStructuralAppropriateness(petriNet);
			System.out.println("Simple structural appropriateness");
			System.out.println(simpleStructuralAppropriateness);
			
		} catch (Exception e) {
			System.out.println("Failed to compute.");
			e.printStackTrace();
		}
	}

}