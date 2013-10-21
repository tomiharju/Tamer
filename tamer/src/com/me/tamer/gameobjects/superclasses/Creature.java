package com.me.tamer.gameobjects.superclasses;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.tamer.Spear;

public interface Creature {

	
	/**
	 * @param pos
	 * Spawn points call this method to set correct spawning position
	 */
	public void setPosition(Vector2 pos);
	/**
	 * @param vel
	 * Spawning points call this method to set correct spawning velocity
	 */
	public void setVelocity(Vector2 vel);
	/**
	 * What happens when this object is hit by a spear
	 */
	public void spearHit(Spear spear);
	/**
	 * When a spear is picked up, this unBind is called. 
	 */
	public void unBind();
	/**
	 * What happens when this object is hit by a lasso
	 */
	public void lassoHit(String lasso);
	/**
	 * What needs to be done when specific creature is killed
	 */
	public void kill();
	public void moveToFinish();
	
}
