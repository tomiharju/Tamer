package com.me.tamer.gameobjects.creatures;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.tamer.Spear;

public interface Creature {

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
	
	/**
	 * This method is used for checking if point is affecting the creature within given radius.
	 * For example, when a spear is thrown, we check if that spears position has hit this creature.
	 * When isAffected is called on Worm, it checks through all its parts for potential hit.
	 * When isAffected is called on Ant, it only checks if it hits the ant directly.
	 */
	public Creature affectedCreature(Vector2 point, float radius);
	
	/**
	 * @param point center of pull
	 * This method is called from Quicksans, its used to cause the pulling effect to the creature
	 */
	/**
	 * @return
	 * Chec if this creature is withint certain range from certain point. This function returns a boolean value instead of the actual affected object
	 */
	public boolean isAffected(Vector2 point, float radius);
	public void applyPull(Vector2 point);
	public void moveToPoint(Vector2 point);
	public void setHeading(Vector2 rotate);
	public Vector2 getHeading();
	
}
