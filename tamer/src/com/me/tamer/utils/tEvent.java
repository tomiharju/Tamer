package com.me.tamer.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class tEvent {
	
	private Object caller;
	private Method targetMethod;
	private float interval;
	private float accumulation = 0;
	private int repetitions	 = 1; //1 by default
	private int repetitionsDone = 0;
	
	private boolean eventFinished = false;
	
	
	public tEvent(Object caller, String target, float interval, int repetitions){
		this.caller = caller;
		this.interval = interval;
		this.repetitions = repetitions;
		
		try {
			this.targetMethod = caller.getClass().getMethod(target);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param dt
	 * Steps this timer by dt, if accumulation time is exceeded, fire given action
	 * Check if there are more repetitions, start accumulation again
	 * set eventFinished false if there are more repetitions
	 * setEventFinished true if this event is over ( EventPool uses this boolean to determine wether this event shoudl be removed or not )
	 * 
	 * @return
	 */
	public void step(float dt){
		accumulation += dt;
		if(accumulation >= interval)
			try {
				targetMethod.invoke(caller);
				repetitionsDone++;
				if(repetitionsDone < repetitions){
					accumulation = 0;
					eventFinished = false;
				}
				else
					eventFinished = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		
	
		
	}
	
	public boolean isFinished(){
		return eventFinished;
	}
	
	
	
}
