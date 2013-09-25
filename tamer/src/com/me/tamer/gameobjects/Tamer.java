package com.me.tamer.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;

public class Tamer extends DynamicObject{
	
	private final float SPEED = 1;
	
	@Override
	public void update(float dt){
		velocity.mul(0.99f);
		velocity.add(force);
		position.add(velocity.mul(dt));
	
		
		
	}
	
	
	
	
	/**
	 * @param direction
	 */
	public void manouver(Vector2 direction){
		direction.rotate(-45);
		force.set(direction.mul(SPEED));
	}

}
