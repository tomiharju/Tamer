package com.me.tamer.gameobjects.tiles;

import com.me.tamer.actions.Action;
import com.me.tamer.gameobjects.GameObject;
import com.me.tamer.gameobjects.StaticObject;

public class GrassTile extends StaticObject implements Tile {

	private Action action = null;
	
	@Override
	public boolean isInsideTile(GameObject obj) {
		// TODO Auto-generated method stub
		return false;
	}
	public void executeAction() {
		action.execute();
	}
	@Override
	public void setAction(Action action) {
		this.action = action;
		
	}

}
