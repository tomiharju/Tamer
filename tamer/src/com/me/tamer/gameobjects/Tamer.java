package com.me.tamer.gameobjects;


import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;


public class Tamer extends DynamicObject{
	
	private final float SPEED = 0.1f;
	private GryphonScream scream 			= null;

	public Tamer(){
		initializeScream();
		
		
	}
	
	@Override
	public void update(float dt){
		getVelocity().mul(0.99f);
		getVelocity().add(force);
		position.add(getVelocity().mul(dt));	
	}
	
	/**
	 * @param direction
	 */
	public void manouver(Vector2 direction){
		direction.rotate(-45);
		force.set(direction.mul(SPEED));
	}
	
	public void initializeScream(){
		
		scream = new GryphonScream();
		
		//scream.setRenderer("static:scream");

	}
	
	public GryphonScream getScream(){
		return scream;
	}
}
