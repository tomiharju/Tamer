package com.me.tamer.gameobjects.tiles;

import com.me.tamer.actions.Action;
import com.me.tamer.gameobjects.GameObject;

public class GrassTile implements Tile {

	private Action action;
	public GrassTile(Action action){
		this.action = action;
	}
	@Override
	public boolean isInsideTile(GameObject obj) {
		// TODO Auto-generated method stub
		return false;
	}
	public void executeAction() {
		action.execute();
	}

}
