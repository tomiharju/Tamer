package com.me.tamer.gameobjects.tamer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.utils.RuntimeObjectFactory;

public class Spear extends DynamicObject{
	
	private Environment environment;
	private Vector2 target = new Vector2() ;
	private Creature targetCreature = null;
	private boolean isAttached = false;
	private Vector2 heading = new Vector2();

	//Same variables in spearbutton
	private final float GRAVITY = 5.0f;
	private final float INITIAL_SPEED = 2.0f;

	private Vector2 direction = new Vector2();
	private float distance;
	
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
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: Spear graphics are set");
	}
	
	public void update(float dt){
		
		if(!isAttached)
			//position.add(force.tmp().mul(dt));
			position.add( direction.x * (INITIAL_SPEED * dt + GRAVITY * dt), direction.y * (INITIAL_SPEED * dt + GRAVITY * dt) );
		//When the spear has reached its destination, check if there is some creature
		//If there is, call that creatures spearHit method to resolve damage.
		if(!isAttached && position.dst(target) < 0.5){
			setPosition(target);
			
			ArrayList<Creature> creatures = environment.getCreatures();
			int size = creatures.size();
			for(int i = 0 ; i < size ; i ++){
					targetCreature = creatures.get(i).affectedCreature(position, 0.5f);
					if(targetCreature != null){
						targetCreature = creatures.get(i);
						targetCreature.spearHit(this);
						
						break;
					}
			}

		}	
	}
	
	public void wakeUp(Environment environment){

		this.environment = environment;
		isAttached = false;
		markAsActive();
	}
	public void throwAt(Vector2 point,float power){
		target.set(point);

		direction.set(point.sub(getPosition()));
		distance = direction.len();
		direction.nor();
		
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
		System.out.println("Spear picked up");
	}
	
	public boolean isAttached(){
		return isAttached;
	}
	
	public Vector2 getHeading(){
		heading.set(getForce().tmp().nor());
		return heading;
	}
}
