package com.me.tamer.gameobjects.creatures;


import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.tamer.Spear;

public interface Creature{

	public static final int TYPE_WORM = 1;
	public static final int TYPE_ANT = 2;
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
	 * Used to slowly fade away dead creatures
	 */
	public void decay();
	public boolean isDecaying();
	
	/**
	 * @return wether this object should react to collision
	 */

	public void applyPull(Vector2 point,float magnitude);
	public Creature affectedCreature(Vector2 poitn, float radius);
	public Vector2 getHeading();
	public int getType();
	public float getSpeed();
}
