package com.me.tamer.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


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
	public void setRender(String renderer);
	/**
	 * @param graphics filepath for graphics files
	 * Sets what graphics to draw.
	 */
	public void setGraphics(String graphics);
	/**
	 * Sets the value isCarbage = true
	 * Causes the carbage collection cycle to remove this object
	 */
	public void markAsCarbage();
	/**
	 * Returns whether this object is marked as carbage
	 */
	public boolean isCarbage();
	/**
	 * @param w
	 * @param h
	 * Calls this.renderer.setSize(w,h);
	 */
	public void setGraphicSize(String size);
	public void setPosition(String pos);
	public Vector2 getPosition();
}

