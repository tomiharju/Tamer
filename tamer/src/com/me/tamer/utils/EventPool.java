package com.me.tamer.utils;

import java.util.ArrayList;

/**
 * @author tharju
 *	Utility class that holds and executes tEvents 
 */
public class EventPool {

	private static ArrayList<tEvent> events = new ArrayList<tEvent>();
	private static ArrayList<tEvent> oldEvents = new ArrayList<tEvent>();
	
	/**
	 * @param dt
	 * General update method which is called in Environment.update cycle
	 * Used to step all the timers, and execute them if need be.
	 */
	public static void step(float dt){
		int i;
		int size = events.size();
		for(i = 0 ; i < size ; i++){
			events.get(i).step(dt);
			//If event is finished, add event to "oldEvents", which will be destroyed or maybe even reused or something?
			if(events.get(i).isFinished()){
				oldEvents.add(events.get(i));
			}
		}
		//For now, lets just delete all old events...
		events.removeAll(oldEvents);
		oldEvents.clear();
	}
	public static void addEvent(tEvent e){
		events.add(e);
	}
}
