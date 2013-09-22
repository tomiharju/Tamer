package com.me.tamer.gameobjects.tiles;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.actions.Action;
import com.me.tamer.gameobjects.GameObject;
import com.me.tamer.gameobjects.StaticObject;
import com.me.tamer.utils.ActionFactory;
import com.me.tamer.utils.IsoHelper;

public class GroundTile extends StaticObject implements Tile {

	private Action action = null;
	
	@Override
	public boolean isInsideTile(GameObject obj) {
		// TODO Auto-generated method stub
		return false;
	}
	public void executeAction() {
		if(action != null)
			action.execute();
	}
	@Override
	public void setAction(String action) {
		this.action = ActionFactory.createAction(action);
		
	}
	@Override
	public void setPosition(String pos) {
		// TODO ask grid object for real raw screen coordinate
		String[] values = pos.split(":");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		Vector2 position = IsoHelper.twoDToIso(new Vector2(x,y));
		this.position = position;
		
	}
	
	

}
