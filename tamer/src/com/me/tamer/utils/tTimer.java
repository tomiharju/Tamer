package com.me.tamer.utils;

import java.lang.reflect.Method;

public class tTimer extends Thread{
	
	private Object caller;
	private Method targetMethod;
	private long interval;
	private float accumulation = 0;
	
	public tTimer(Object caller, String target, long interval){
		this.caller = caller;
		this.interval = interval * 1000;
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
	
	public void run(){
		
		try {
			System.out.println("Timer thread started: " + caller.toString() +" -> " + targetMethod.toGenericString());
			Thread.sleep(interval);
			targetMethod.invoke(caller);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}
	
	
	

}
