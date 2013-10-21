package com.me.tamer.gameobjects.tamer;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.Creature;
import com.me.tamer.utils.RuntimeObjectFactory;

public class Spear extends DynamicObject{
	
	private Environment environment;
	private Vector2 target = new Vector2() ;
	private Creature targetCreature = null;
	private boolean isAttached = false;
	private Vector2 heading = new Vector2();
	
	public Spear(){
		setGraphics();
	}
	
	public void setup(){
		
	}
	
	public void setGraphics(){
		Renderer render = RenderPool.addRendererToPool("animated","spear");
		render.loadGraphics("spear",1,8);
		setSize("1:1");
		renderType = "spear";
		System.out.println("Spear graphics are set");
	}
	
	public void update(float dt){
		if(!isAttached)
			position.add(force.tmp().mul(dt));
		//When the spear has reached its destination, check if there is some creature
		//If there is, call that creatures spearHit method to resolve damage.
		if(!isAttached && position.dst(target) < 0.5){
			position.set(target);
			ArrayList<Creature> creatures = environment.getCreatures();
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
	
	public void wakeUp(Environment environment){
		System.out.println("Spear woke up!");
		this.environment = environment;
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
	
	public Vector2 getHeading(){
		heading.set(getForce().tmp().nor());
		return heading;
	}

}
