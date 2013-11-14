package com.me.tamer.gameobjects.creatures;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.physics.RigidBodyBox;
import com.me.tamer.utils.tEvent;


public class AntOrc extends DynamicObject implements Creature{
	
	private final float WORM_SCAN_RADIUS = 10.0f; //ScanArea is a circle
	private final float WAYPOINT_SCAN_RADIUS = 0.5f;
	private final float ATTACK_DISTANCE = 0.3f;
	private final float SPEED_INCREASE = 1.1f; //A number on which the velocity is multiplied with
	private final float SPEED = 8.0f;
	
	private Environment environment;
	private ArrayList<Vector2> waypoints;
	
	private int nextWaypoint = 1;
	private WormPart target = null;
	private boolean attached = false;
	private boolean destinationReached = false;
	private boolean markedDead = false;
	private boolean returning = false;
	private ArrayList<Creature> creatures;
	
	private Vector2 help = new Vector2();
	private tEvent eatingTimer;
	
	public AntOrc(){
		waypoints = new ArrayList<Vector2>();
		waypoints.add(new Vector2(0,0));//place holder for the first value
		waypoints.add(new Vector2(-2,10));
		waypoints.add(new Vector2(10,15));
	}
	
	public void wakeUp(Environment environment){
		this.environment = environment;
		//Add the spawning position as a first waypoint
		setGraphics();
		markAsActive();
		
		help.set(getPosition());
		waypoints.set(0, help);
		
	}
	
	public void setGraphics() {
		Renderer render = RenderPool.addRendererToPool("animated", "antorc");
		render.loadGraphics("antorc", 1, 8);
		setSize(new Vector2(1,1));
		setRenderType("antorc");
	}
	
	public void update(float dt){
		//how often should this scan
		if(!returning && target==null)scanWorms();
		if (attached){
			//Needs to invMass to keep worm in place
			//Here you go :)
			target.bind();
			getPosition().set( target.getPosition() );
			if(!eatingTimer.isFinished())eatingTimer.step(dt);
		}else if(target != null) followTarget();
		else followPath();
		
		setVelocity(getHeading().tmp().mul(SPEED));
		getPosition().add(getVelocity().tmp().mul(dt));	
	}
	
	public void lockToTarget(WormPart wp){
		//Increase speed and set target
		target = wp;
		//getVelocity().mul(SPEED_INCREASE) ;
	}
	
	public void detach(){
		System.out.println("detached");
		target.unBind();
		target.breakJoint();
		returning = true;
		attached = false;
		destinationReached = true;
		target=null;
		nextWaypoint = 0;
	}
	
	public void followTarget(){
		//Check if target is close enough to be attacked and update heading to target
		if ( getPosition().dst( target.getPosition() ) < ATTACK_DISTANCE ){
			//attach and start timer for detaching
			attached = true;
			eatingTimer = new tEvent(this, "detach", 3, 1);
		}
		setHeading( target.getPosition().tmp().sub( getPosition()).nor() );
	}
	
	public void followPath(){
		if (!markedDead){
			setHeading( waypoints.get(nextWaypoint).tmp().sub(getPosition()).nor() );
			if (nextWaypoint == waypoints.size() - 1) destinationReached = true;
			//System.out.println("destinationreChed: " +waypoints.get(0));
			//first check if waypoint is reached then set heading to next waypoint
			if ( getPosition().dst( waypoints.get(nextWaypoint)) < WAYPOINT_SCAN_RADIUS ){	
				if (!destinationReached){
					nextWaypoint++;
				}else nextWaypoint--;	
			}
			
			if (nextWaypoint < 0 ){
				markedDead = true;
				breakJoint();
			}	
		}
	}
	
	public void scanWorms(){
		//lock to wormpart that is scanned first
		creatures = environment.getCreatures();
		for (int i = 0; i < creatures.size(); i++){			
			if ( creatures.get(i).getType() == Creature.TYPE_WORM ){
				ArrayList<WormPart> wormParts = ((Worm)creatures.get(i)).getParts();
				for (int j = 0; j < wormParts.size(); j++){
					if (wormParts.get(j).getOrdinal() > 2 &&  wormParts.get(j).getPosition().dst( getPosition() ) < WORM_SCAN_RADIUS){
						lockToTarget( wormParts.get(j));
					}
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
	public void breakJoint() {
		markAsCarbage();	
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
	public void debugDraw(ShapeRenderer shapeRndr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGraphics(String graphics) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setup(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setzIndex(String index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getType() {
		return Creature.TYPE_ANT;
	}

	@Override
	public void decay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean collisionEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}
