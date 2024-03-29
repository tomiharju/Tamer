package com.me.tamer.gameobjects.superclasses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;


/**
 * @author Kesyttäjät
 * Common gameobject interface
 * Holds all general actions for gameobjects
 *
 */

public interface GameObject{
	
	/**
	 * Generic object update method, used to iterate positions, velocities etc.
	 */
	public void update(float dt);
	
	/**
	 * Generic object draw method, used to draw sprites etc.
	 * Draw method delegates the drawing for renderer specified for the object.
	 */
	public void draw(SpriteBatch batch);
	public void debugDraw(ShapeRenderer shapeRndr);

	/**
	 * Sets the value isCarbage = true
	 * Causes the carbage collection cycle to remove this object
	 */
	public void markAsCarbage();
	/**
	 * Sets isCarbage to false
	 */
	public void markAsActive();
	/**
	 * Returns whether this object is marked as carbage
	 */
	public boolean isCarbage();
	/**
	 * @param w
	 * @param h
	 * Calls this.renderer.setSize(w,h);
	 */
	public void setSize(Vector2 size);
	/**
	 * @param graphics
	 * Loads graphics with string name
	 */
	public void setGraphics(String graphics);
	public void setPosition(String pos);
	public void setSize(float x,float y);
	public boolean getDebug();
	public Vector2 getPosition();
	public Vector2 getCenterPosition();
	public Vector2 getSize();
	/**
	 * Required for objects that are created in runtime with threads.
	 */
	/**
	 * Called at level loading, used to load all graphics and other assets
	 * @param level 
	 */
	public void setup(Environment level);
	/**
	 * Wakeup is called when object is put into play. For example when worm is fetched from object pool, wakeup method adds that worm and its parts to active gameobjects
	 *
	 * @param level
	 */
	public void wakeUp(Environment level);
	
	/**
	 * Actions that needs to be taken when this object this object is removed from game.
	 */
	public void dispose(Environment level);
	public boolean isWithinRange(Vector2 poitn, float radius);
	public int getZIndex();
	public void setZindex(int z);
	public void setzIndex(String index);
	public void setPosition(Vector2 pos);
}

