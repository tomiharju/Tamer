package com.me.tamer.actions;

import com.me.tamer.gameobjects.superclasses.GameObject;

public interface Action {
	
	/**
	 * Executes this action, action can be falling into a pit, pull from quicksand etc.
	 */
	public void execute(GameObject obj);
}
