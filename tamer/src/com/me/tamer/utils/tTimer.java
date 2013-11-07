package com.me.tamer.utils;

import java.lang.reflect.Method;

import com.badlogic.gdx.Gdx;
import com.me.tamer.core.TamerGame;
import com.me.tamer.core.TamerStage;

public class tTimer extends Thread{
	
	private Object caller;
	private Method targetMethod;
	private long interval;
	private long accumulation = 0;
	private long loopAccumulation;
	private int repetitions;
	private int repetitionsDone = 0;
	
	public tTimer(Object caller, String target, long interval, int repetitions){
		this.caller = caller;
		this.interval = interval;
		this.loopAccumulation = this.interval / 100; // is this too much?
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
	
	public void run(){
		
		try {
			Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Timer thread started: " + caller.toString() +" -> " + targetMethod.toGenericString());
			while(repetitionsDone < repetitions){
				while( accumulation < interval){
					
					if (TamerStage.gameState == TamerStage.GAME_RUNNING){
						Thread.sleep((long)loopAccumulation);
						accumulation += loopAccumulation;
					}
				}
				accumulation = 0;
				targetMethod.invoke(caller);
				repetitionsDone++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
