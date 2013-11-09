package com.me.tamer.gameobjects.tamer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.tiles.Prop;
import com.me.tamer.gameobjects.tiles.obstacles.Obstacle;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.utils.RuntimeObjectFactory;

public class Spear extends DynamicObject{
	
	private Environment environment;
	private SoundManager sound;
	
	private Creature targetCreature 	= null;
	private boolean attached 			= false;
	private boolean targetReached 		= false;
	private Vector2 targetPoint			= new Vector2();
	
	private boolean justDropped			= false;
	
	ArrayList<Vector2> waypoints;
	private int currentWayPoint;
	private Vector2 direction 			= new Vector2();
	private final float SPEED 			= 15.0f;
	private Prop hitbox					= new Prop();
	
	
	public Spear(){
		setGraphics();
		sound = SoundManager.instance();
		
		//hitbox
		hitbox.setGraphics("mountain1");
		hitbox.setPixelsX("40");
		hitbox.setPixelsY("40");
		//hitbox.setRenderType("joystick");
		hitbox.setHitBox("1");
	}
	
	public void setGraphics(){
		Renderer render = RenderPool.addRendererToPool("animated","spear");
		render.loadGraphics("spear",1,8);
		setSize(new Vector2(2.4f,1.5f));
		setRenderType("spear");
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: Spear graphics are set");
	}
	
	public void update(float dt){
		/*
		if ( currentWayPoint == 0) {
			
			//falling spear has zIndex 0
			setzIndex(0);
			
		}
		*/
		
		if (justDropped && attached && environment.getTamer().getShadow().getPosition().dst(getPosition()) > 1){
			justDropped = false;
			System.out.println("just dropped changed");
		}
		
		if(!attached){
			direction.set(targetPoint.tmp().sub(getPosition()));
			direction.nor();
			getPosition().add( direction.x * (SPEED * dt), direction.y * (SPEED * dt));
			
			
			if (getPosition().dst( targetPoint ) < 0.5f){
				if(targetCreature != null){
					targetCreature.spearHit(this);
					sound.setVolume(0.3f);
					Gdx.app.log(TamerGame.LOG, this.getClass()
							.getSimpleName() + " :: playing sound HIT");
					sound.play(TamerSound.HIT);
					
					
					//create hitbox
					/*
					hitbox.setPosition(getPosition());
					environment.addNewObject(hitbox);
					hitbox.markAsActive();
					*/
					
				}
				
				setPosition( targetPoint );
				attached = true;		
			}		

		}
	}
	
	public void wakeUp(Environment environment){

		this.environment 	= environment;
		attached 			= false;
		setzIndex(-1);
		markAsActive();
		throwAt();
	}
	
	public void throwAt(){
		setPosition(environment.getTamer().getPosition());
		boolean targetFound = false;
		
		
		ArrayList<Creature> creatures = environment.getCreatures();
		int size = creatures.size();
		for(int i = 0 ; i < size ; i ++){
				targetCreature = creatures.get(i).affectedCreature(environment.getTamer().getShadow().getPosition(), 3);
				if(targetCreature != null){
					targetPoint = ((DynamicObject)targetCreature).getPosition();
					targetFound = true;
				}	
		}	
		
		//Switch to SPEAR_CAM if spear is going to hit
		/*
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: switched to SPEAR_CAMERA");
		environment.getStage().setCameraHolder(TamerStage.SPEAR_CAMERA);*/
		
		
		if(!targetFound){
			targetPoint = new Vector2();
			targetPoint.set(environment.getTamer().getShadow().getPosition());
			targetPoint.x = (float) Math.floor(targetPoint.x);
			targetPoint.y = (float) Math.floor(targetPoint.y);
		}
		
		setHeading(targetPoint.tmp().sub(getPosition()) );
		direction.set(targetPoint.tmp().sub(getPosition()));
		direction.nor();
		
		justDropped = true;
	
		
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
		
		//remove hitbox
		//hitbox.markAsCarbage();
		
		RuntimeObjectFactory.addToObjectPool("spear",this);
		markAsCarbage();
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: Spear picked up ");
	}
	
	public boolean isAttached(){
		return attached;
	}
	
	public boolean isJustDropped(){
		return justDropped;
	}
}
