package com.me.tamer.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.utils.RuntimeObjectFactory;

public class Spear extends DynamicObject{
	
	private Level level;
	private Vector2 target = null ;
	private float SPEED = 10;
	private boolean isAttached = false;
	
	public Spear(){
		
	}
	public void setup(){
		setRenderer("static:spear");
		setSize("1:1");
		setPosition("0:0");
		setVelocity("0:0");
		setForce("0:0");
		target = new Vector2();
	}
	
	public void update(float dt){
		if(!isAttached)
			position.add(force.tmp().mul(dt));
		if(position.dst(target) < 0.1)
			isAttached = true;
		
	}
	
	public void wakeUp(Level level){
		this.level = level;
	}
	public void throwAt(Vector2 point){
		target.set(point);
		Vector2 dir = point.sub(position);
		force.set(dir.tmp().nor().mul(SPEED));
	}
	
	/**
	 * Add this spear back to the pool of spears
	 * Remove from active gameobjects
	 */
	public void pickUp(){
		RuntimeObjectFactory.addToObjectPool("spear",this);
		markAsCarbage();
	}
	public void setPosition(Vector2 pos){
		position.set(pos);
	}

}
