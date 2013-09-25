package com.me.tamer.gameobjects.tiles;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.actions.Action;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.utils.ActionFactory;
import com.me.tamer.utils.IsoHelper;

public class GroundTile extends StaticObject implements Tile {
	
	public GroundTile(){
		position = new Vector2();
	}
	@Override
	public boolean resolveTile(GameObject obj) {
		// TODO Auto-generated method stub
		return false;
	}
	public void executeAction() {
	
	}
	@Override
	public void setAction(String action) {
		//this.action = ActionFactory.createAction(action);
		
	}
	@Override
	public void setPosition(String pos) {
		// TODO ask grid object for real raw screen coordinate
		String[] values = pos.split(":");
		float x = Float.parseFloat(values[0]);
		float y = Float.parseFloat(values[1]);
		position.set(x,y);
	}
	
	
	

}
