package com.me.tamer.gameobjects.tiles;

import com.me.tamer.actions.Action;
import com.me.tamer.gameobjects.GameObject;
import com.me.tamer.gameobjects.StaticObject;

public class QuickSandTile extends StaticObject implements Tile {

	private Action action = null;

	@Override
	public boolean isInsideTile(GameObject obj) {
		// TODO Calculation algorithm to determine whether the object is inside this tile
		return false;
	}
	public void executeAction() {
		action.execute();
	}
	@Override
	public void setAction(String action) {
		
	}

}
