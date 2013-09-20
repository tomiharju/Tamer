package com.me.tamer.gameobjects.tiles;

import com.me.tamer.actions.Action;
import com.me.tamer.gameobjects.GameObject;
import com.me.tamer.gameobjects.StaticObject;
import com.me.tamer.utils.ActionFactory;

public class DirtTile extends StaticObject implements Tile {

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
	public void setAction(String action) {
		this.action = ActionFactory.createAction(action);
		
	}
	
}
