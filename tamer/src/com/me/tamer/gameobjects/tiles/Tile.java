package com.me.tamer.gameobjects.tiles;

import com.me.tamer.actions.Action;
import com.me.tamer.gameobjects.superclasses.GameObject;

public interface Tile {

	public void setAction(String action);
	public boolean resolveTile(GameObject obj);
	public void executeAction();
}
