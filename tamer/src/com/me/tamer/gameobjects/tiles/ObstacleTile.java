package com.me.tamer.gameobjects.tiles;

import com.me.tamer.actions.Action;
import com.me.tamer.gameobjects.GameObject;
import com.me.tamer.gameobjects.StaticObject;

public class ObstacleTile extends StaticObject implements Tile{

	private Action action = null;
	@Override
	public void setAction(String action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInsideTile(GameObject obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void executeAction() {
		if(action != null)
			action.execute();
		
	}

}
