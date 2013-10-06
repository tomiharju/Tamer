package com.me.tamer.actions;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.superclasses.Interactable;

public interface Action {
	
	/**
	 * Executes this action, action can be falling into a pit, pull from quicksand etc.
	 */
	public void execute(Interactable obj);
	public void setCenterPoint(Vector2 point);
}
