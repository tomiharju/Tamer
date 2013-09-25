package com.me.tamer.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


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
	public void update(float dt);
	
	/**
	 * Generic object draw method, used to draw sprites etc.
	 * Draw method delegates the drawing for renderer specified for the object.
	 */
	public void draw(SpriteBatch batch);
	
	/**
	 *	 * @param renderer
	 *	 Set renderer for object ( Static or Dynamic )
	 */
	public void setRenderer(Renderer renderer);
	/**
	 * Sets the value isCarbage = true
	 * Causes the carbage collection cycle to remove this object
	 */
	public void markAsCarbage();
	public boolean isCarbage();
}

