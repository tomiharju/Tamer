package com.me.tamer.actions;

public interface Action {
	public enum ActionType{
		QUICKSAND,STARTINGPOINT,ENDINGPOINT,DEFAULTACTION
	}
	
	/**
	 * Executes this action, action can be falling into a pit, pull from quicksand etc.
	 */
	public void execute();
}
