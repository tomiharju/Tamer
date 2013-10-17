package com.me.tamer.gameobjects.tiles;

import com.me.tamer.actions.Action;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.superclasses.Creature;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.physics.RigidBody;
import com.me.tamer.utils.ActionFactory;

public class ObstacleTile extends StaticObject implements Tile{

	private Action action = null;
	
	
	
	public ObstacleTile(){
		
	}
	@Override
	public void setAction(String action) {
		this.action = ActionFactory.createAction(action);
		this.action.setCenterPoint(position);
	}

	@Override
	public boolean resolveTile(Creature interactable) {
	//TODO TODO TODO TODO TODO
		if(action != null)
		if(((GameObject) interactable).getPosition().dst(position) < this.size.x)
			action.execute(interactable);
		return false;
	}

	@Override
	public RigidBody getRigidBody(){
		return body;
	}

}
