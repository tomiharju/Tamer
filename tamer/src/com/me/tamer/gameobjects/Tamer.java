package com.me.tamer.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;

public class Tamer extends DynamicObject{
	
	private final float SPEED = 1f;
	
	@Override
	public void update(float dt){
		position.add(force);
	}
	
	
	
	
	/**
	 * @param direction
	 */
	public void manouver(Vector2 direction){
		force.set(direction.mul(SPEED));
	}

}
