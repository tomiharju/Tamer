package com.me.tamer;

import com.badlogic.gdx.Game;
import com.me.tamer.actions.Action.ActionType;
import com.me.tamer.gameobjects.tiles.Tile;
import com.me.tamer.gameobjects.tiles.Tile.TileType;
import com.me.tamer.utils.TileFactory;

public class TamerMain extends Game{

	@Override
	public void create() {
		// TODO Auto-generated method stub
		Tile tile = TileFactory.createTile(TileType.GRASS, ActionType.DEFAULTACTION);
	}
	

}




