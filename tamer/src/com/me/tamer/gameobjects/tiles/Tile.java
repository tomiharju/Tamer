package com.me.tamer.gameobjects.tiles;

import com.me.tamer.actions.Action;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.superclasses.Creature;

public interface Tile {

	public void setAction(String action);
	public boolean resolveTile(Creature obj);
}
