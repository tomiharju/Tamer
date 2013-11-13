package com.me.tamer.gameobjects.creatures;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.physics.RigidBodyBox;


public class AntOrc extends DynamicObject implements Creature{
	
	private final float WORM_SCAN_RADIUS = 5.0f; //ScanArea is a circle
	private final float WAYPOINT_SCAN_RADIUS = 3.0f;
	private final float ATTACK_DISTANCE = 1.0f;
	private final float SPEED_INCREASE = 2.0f; //A number on which the velocity is multiplied with
	private final float SPEED = 1.0f;
	
	private Environment environment;
	private ArrayList<Vector2> waypoints;
	
	private int nextWaypoint = 0;
	private WormPart target = null;
	private boolean attached = false;
	private boolean destinationReached = false;
	private ArrayList<Creature> creatures;
	
	public AntOrc(){
		waypoints = new ArrayList<Vector2>();
		waypoints.add(new Vector2(0,0));//place holder for the first value
		waypoints.add(new Vector2(10,10));
	}
	
	public void wakeUp(Environment environment){
		this.environment = environment;
		//Add the spawning position as a first waypoint
		waypoints.add(0, getPosition());
		setGraphics();
		markAsActive();
	}
	
	public void setGraphics() {
		Renderer render = RenderPool.addRendererToPool("animated", "antorc");
		render.loadGraphics("antorc", 1, 8);
		setSize(new Vector2(1,1));
		setRenderType("antorc");
	}
	
	public void update(float dt){
		//how often should this scan
		//scanWorms();
		System.out.println("updating");
		/*
		if (attached){
			//Needs to fumble with the invMass
			//Start some timer
			//setPosition( target.getPosition() );
			Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: ant attached");
		}
		else if(target != null) followTarget();
		else followPath();	*/
		
		setVelocity(getHeading().mul(SPEED));
		setPosition(getPosition().add(getVelocity().tmp().mul(dt)));
		
	}
	
	public void lockToTarget(WormPart wp){
		//Increase speed and set target
		target = wp;
		setVelocity( getVelocity().mul(SPEED_INCREASE) );
	}
	
	public void followTarget(){
		//Check if target is close enough to be attacked and update heading to target
		if ( getPosition().dst( target.getPosition() ) < ATTACK_DISTANCE ){
			attached = true;	
		}
		
		setHeading( target.getPosition().sub( getPosition()).nor() );
	}
	
	public void followPath(){
		//first check if waypoint is reached then set heading to next waypoint
		
		if ( getPosition().dst( waypoints.get(nextWaypoint)) < WAYPOINT_SCAN_RADIUS ){	
			if (!destinationReached)nextWaypoint++;
			else nextWaypoint--;
		}
		
		if (nextWaypoint == waypoints.size()) destinationReached = true;
		if (nextWaypoint == -1 ) kill();
		
		setHeading( waypoints.get(nextWaypoint).sub(getPosition()).nor() );
	}
	
	public void scanWorms(){
		//lock to wormpart that is scanned first
		creatures = environment.getCreatures();
		for (Creature creature : creatures){		
			if (creature.getClass().getName() == "WormPart"){
				if ( ((WormPart)creature).getPosition().dst( getPosition() ) < WORM_SCAN_RADIUS){
					lockToTarget( (WormPart)creature);
				}
			}
		}
	}
	
	@Override
	public Creature affectedCreature(Vector2 point,float radius) {
		if( this.getPosition().dst(point) < radius)
			return this;
		else
			return null;
		
	}

	public void addWaypoint(Vector2 waypoint){
		waypoints.add(waypoint);
	}
	
	@Override
	public void spearHit(Spear spear) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lassoHit(String lasso) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveToPoint(Vector2 point) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unBind() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void applyPull(Vector2 point,float magnitude) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAffected(Vector2 point, float radius) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RigidBodyBox getCollider() {
		// TODO Auto-generated method stub
		return null;
	}

	

	
}
