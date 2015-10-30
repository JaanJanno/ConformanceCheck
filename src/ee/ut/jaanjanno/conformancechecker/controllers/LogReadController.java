package ee.ut.jaanjanno.conformancechecker.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import ee.ut.jaanjanno.conformancechecker.log.Case;
import ee.ut.jaanjanno.conformancechecker.log.Event;
import ee.ut.jaanjanno.conformancechecker.log.EventLog;
import ee.ut.jaanjanno.conformancechecker.log.Trace;
import ee.ut.jaanjanno.conformancechecker.parsers.XLogReader;
import ee.ut.jaanjanno.conformancechecker.petrinet.Transition;

public class LogReadController {
	
	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
	
	public static EventLog parseLog(String address, Map<String, Transition> transitions) throws Exception {
		XLog log = XLogReader.openLog(address);
		Map<String, Event> events = getEventsMap(log, transitions);
		Set<Case> cases = getCases(events, log);
		return new EventLog(cases);
	}

	private static Set<Case> getCases(Map<String, Event> events, XLog log) {
		Map<List<Event>, Integer> map = getEventLists(events, log);
		Set<Case> cases = new HashSet<>();
		for (List<Event> eventSequence : map.keySet()) {
			Trace trace = new Trace(eventSequence);
			Case cas = new Case(trace, map.get(eventSequence));
			cases.add(cas);
		}
		return cases;
	}

	private static Map<List<Event>, Integer> getEventLists(Map<String, Event> events, XLog log) {
		Map<List<Event>, Integer> map = new HashMap<>();
		for (XTrace trace : log) {
			List<Event> eventSequence = new ArrayList<>();
			for (XEvent event : trace) {
				XAttributeMap eventAttributes = event.getAttributes();
				String name = eventAttributes.get("concept:name").toString();
				eventSequence.add(events.get(name));
			}
			int count = map.containsKey(eventSequence) ? map.get(eventSequence) : 0;
			map.put(eventSequence, count + 1);
		}
		return map;
	}

	private static Map<String, Event> getEventsMap(XLog log, Map<String, Transition> transitions) {
		Map<String, Event> map = new HashMap<>();
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				XAttributeMap eventAttributes = event.getAttributes();
				String name = eventAttributes.get("concept:name").toString();
				String date = eventAttributes.get("time:timestamp").toString();
				Date timeStamp = parseDate(date);
				Transition transition = transitions.get(name);
				map.put(name, new Event(transition, name, timeStamp));
			}
		}
		return map;
	}

	private static Date parseDate(String string) {
		try {
			return format.parse(string);
		} catch (ParseException e) {
			return new Date();
		}
	}

}
