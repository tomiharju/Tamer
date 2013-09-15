package com.me.tamer.utils;

import com.me.tamer.actions.Action;
import com.me.tamer.actions.Action.ActionType;
import com.me.tamer.gameobjects.tiles.DirtTile;
import com.me.tamer.gameobjects.tiles.GrassTile;
import com.me.tamer.gameobjects.tiles.QuickSandTile;
import com.me.tamer.gameobjects.tiles.Tile;
import com.me.tamer.gameobjects.tiles.Tile.TileType;

public class TileFactory {
	
	
public static Tile createTile(TileType type,ActionType actionType){
	Action action = ActionFactory.createAction(actionType);
	switch(type){
		case DIRT:
			return new DirtTile();
		case GRASS:
			return new GrassTile();
		case QUICKSAND:
			return new QuickSandTile();
		default:
			break;
	
	
	}
	return null;
}
}
