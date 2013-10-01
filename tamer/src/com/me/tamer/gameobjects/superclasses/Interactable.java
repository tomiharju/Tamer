package com.me.tamer.gameobjects.superclasses;

import com.me.tamer.gameobjects.Spear;

public interface Interactable {

	
	
	/**
	 * What happens when this object is hit by a spear
	 */
	public void spearHit(Spear spear);
	/**
	 * What happens when this object is hit by a lasso
	 */
	public void lassoHit(String lasso);
}
