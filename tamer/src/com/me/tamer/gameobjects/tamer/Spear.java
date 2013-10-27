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

	private Creature targetCreature = null;
	private boolean attached = false;
	private boolean targetReached = false;
	
	ArrayList<Vector2> waypoints;
	private int currentWayPoint;

	
	private final float SPEED = 12.0f;

	private Vector2 direction = new Vector2();
	
	public Spear(){
		setGraphics();
	}
	
	public void setGraphics(){
		Renderer render = RenderPool.addRendererToPool("animated","spear");
		render.loadGraphics("spear",1,8);
		setSize(new Vector2(1,1));
		renderType = "spear";
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: Spear graphics are set");
	}
	
	public void update(float dt){
		
		if(!attached)
			position.add( direction.x * (SPEED * dt), direction.y * (SPEED * dt));

		if ( currentWayPoint == 0) {
			//falling spear has zIndex 0
			setzIndex(0);
			targetReached = true;
		}
		
		//System.out.println("attached: " +attached +", targetReached: " +targetReached +", distance: " +position.dst( waypoints.get(currentWayPoint) ));
		
		if(!attached && targetReached && position.dst( waypoints.get(currentWayPoint) ) < 0.1){
			setPosition( waypoints.get(currentWayPoint) );
			attached = true;
			
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
		}else if (!attached && position.dst( waypoints.get(currentWayPoint) ) < 0.1) {
			currentWayPoint--;
			heading.set(waypoints.get(currentWayPoint).tmp().sub(getPosition()));	
			direction.set(waypoints.get(currentWayPoint).tmp().sub(getPosition()));
			direction.nor();
		}
	}
	
	public void wakeUp(Environment environment){

		this.environment = environment;
		attached = false;
		targetReached = false;
		setzIndex(-1);
		markAsActive();
	}
	
	public void throwAt(ArrayList<Vector2> waypoints){
		this.waypoints = waypoints;
		
		heading.set(waypoints.get( waypoints.size() - 1 ).tmp().sub(getPosition()) );
		direction.set(waypoints.get( waypoints.size() - 1 ).tmp().sub(getPosition() ));
		direction.nor();
		
		currentWayPoint = waypoints.size() - 1;
	}
	
	/**
	 * Add this spear back to the pool of spears
	 * Remove from active gameobjects
	 */
	public void pickUp(){
		attached = false;
		targetReached = false;// not sure if needed here
		setzIndex(-1); // not sure if needed here
		
		
		if(targetCreature != null){
			targetCreature.unBind();
			targetCreature = null;
		}
		RuntimeObjectFactory.addToObjectPool("spear",this);
		markAsCarbage();
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: Spear picked up ");
	}
	
	public boolean isAttached(){
		return attached;
	}
}
