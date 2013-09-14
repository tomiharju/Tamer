package com.me.tamer.gameobjects.tiles;

import com.me.tamer.gameobjects.GameObject;

public interface Tile {

	public enum TileType{
		GRASS,DIRT,QUICKSAND
	}
	public boolean isInsideTile(GameObject obj);
	public void executeAction();
}
