package com.me.tamer.gameobjects;


/**
 * @author Kesyttäjät
 * Common gameobject interface
 * Holds all general actions for gameobjects
 *
 */

public interface GameObject {
	
	/**
	 * Generic object update method, used to iterate positions, velocities etc.
	 */
	public void update();
	
	/**
	 * Generic object draw method, used to draw sprites etc.
	 */
	public void draw();
}

