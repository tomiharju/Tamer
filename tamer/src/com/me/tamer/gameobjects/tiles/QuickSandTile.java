package com.me.tamer.gameobjects.tiles;

import com.me.tamer.actions.Action;
import com.me.tamer.gameobjects.GameObject;

public class QuickSandTile implements Tile {

	private Action action;
	public QuickSandTile(Action action){
		this.action = action;
	}
	@Override
	public boolean isInsideTile(GameObject obj) {
		// TODO Calculation algorithm to determine whether the object is inside this tile
		return false;
	}
	public void executeAction() {
		action.execute();
	}

}
