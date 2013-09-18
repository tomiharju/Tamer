package com.me.tamer.gameobjects.tiles;

import com.me.tamer.actions.Action;
import com.me.tamer.gameobjects.GameObject;

public interface Tile {

	public enum TileType{
		GRASS,DIRT,QUICKSAND
	}
	public void setAction(String action);
	public boolean isInsideTile(GameObject obj);
	public void executeAction();
}
