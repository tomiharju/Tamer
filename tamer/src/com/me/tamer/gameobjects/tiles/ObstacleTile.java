package com.me.tamer.gameobjects.tiles;

import com.me.tamer.actions.Action;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.physics.RigidBody;

public class ObstacleTile extends StaticObject implements Tile{

	private Action action = null;
	
	
	
	public ObstacleTile(){
		
	}
	@Override
	public void setAction(String action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean resolveTile(GameObject obj) {
		if(obj.getPosition().dst(this.position) < this.size.x)
			action.execute(obj);
		return false;
	}

	@Override
	public void executeAction() {
	
		
	}
	public RigidBody getRigidBody(){
		return body;
	}

}
