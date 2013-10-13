package com.me.tamer.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.Interactable;
import com.me.tamer.utils.RuntimeObjectFactory;

public class Spear extends DynamicObject{
	
	private Level level;
	private Vector2 target = null ;
	private Interactable targetCreature = null;
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
		//When the spear has reached its destination, check if there is some creature
		//If there is, call that creatures spearHit method to resolve damage.
		if(!isAttached && position.dst(target) < 0.5){
			position.set(target);
			ArrayList<Interactable> creatures = level.getCreatures();
			int size = creatures.size();
			System.out.println("Searching for potential hit amont "+size+" creatures.");
			for(int i = 0 ; i < size ; i ++)
				if(position.dst(((DynamicObject) creatures.get(i)).getPosition()) < 0.5){
					targetCreature = creatures.get(i);
					targetCreature.spearHit(this);
					break;
				}
			isAttached = true;
		}
		
	}
	
	public void wakeUp(Level level){
		this.level = level;
		isAttached = false;
		markAsActive();
	}
	public void throwAt(Vector2 point,float power){
		target.set(point);
		Vector2 dir = point.sub(position);
		force.set(dir.tmp().nor().mul(power));
	}
	
	/**
	 * Add this spear back to the pool of spears
	 * Remove from active gameobjects
	 */
	public void pickUp(){
		if(targetCreature != null){
			targetCreature.unBind();
			targetCreature = null;
		}
		RuntimeObjectFactory.addToObjectPool("spear",this);
		markAsCarbage();
	}
	public void setPosition(Vector2 pos){
		position.set(pos);
	}
	public boolean isAttached(){
		return isAttached;
	}

}
