package com.me.tamer.gameobjects.creatures;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.superclasses.Creature;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.utils.IsoHelper;


public class AntOrc extends DynamicObject implements Creature{
	
	private Environment environment;
	private ArrayList<Vector2> waypoints;
	private float wormScanRad = 5.0f; //ScanArea is a circle
	private float waypointScanRad = 3.0f;
	private float attackDist = 1.0f;
	private float speedIncrease = 2.0f; //A number on which the velocity is multiplied with
	private int nextWaypoint = 0;
	private WormPart target = null;
	private boolean attached = false;
	private boolean destinationReached = false;
	private ArrayList<Creature> creatures;
	
	public AntOrc(){
		waypoints.add(new Vector2(0,0));//place holder for the first value
	}
	
	public void wakeUp(Environment environment){
		this.environment = environment;
		//Add the spawning position as a first waypoint
		waypoints.add(0, getPosition());
		markAsActive();
	}
	
	public void update(){
		//how often should this scan
		scanWorms();
		
		if (attached){
			//Needs to fumble with the invMass
			//Start some timer
			//setPosition( target.getPosition() );
			System.out.println("Mulkku Attached");
		}
		else if(target != null) followTarget();
		else followPath();		
	}
	
	public void lockToTarget(WormPart wp){
		//Increase speed and set target
		target = wp;
		setVelocity( getVelocity().mul(speedIncrease) );
	}
	
	public void followTarget(){
		//Check if target is close enough to be attacked and update heading to target
		if ( getPosition().dst( target.getPosition() ) < attackDist ){
			attached = true;	
		}
		
		setHeading( target.getPosition().sub( getPosition()).nor() );
	}
	
	public void followPath(){
		//first check if waypoint is reached then set heading to next waypoint
		
		if ( getPosition().dst( waypoints.get(nextWaypoint)) < waypointScanRad ){	
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
				if ( ((WormPart)creature).getPosition().dst( getPosition() ) < wormScanRad){
					lockToTarget( (WormPart)creature);
				}
			}
		}
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
	public void moveToFinish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unBind() {
		// TODO Auto-generated method stub
		
	}
}
