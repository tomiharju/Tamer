package com.me.tamer.gameobjects.superclasses;

import com.me.tamer.gameobjects.Spear;

public interface Interactable {

	
	
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
	
}
