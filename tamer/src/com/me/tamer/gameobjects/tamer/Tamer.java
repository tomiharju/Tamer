package com.me.tamer.gameobjects.tamer;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.core.TamerStage;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RuntimeObjectFactory;
import com.me.tamer.utils.VectorHelper;


public class Tamer extends DynamicObject{
	
	private final float SPEED 		= 5f;
	private final float AIM_SPEED 	= 0.04f; //heading interpolating coefficient
	private int numSpears 			= 3;
	private ArrayList<Spear> spears = null;
	private TamerShadow shadow;
	private Environment environment;
	private Vector2 help 			= new Vector2();
	private Vector2 movementAxis 	= new Vector2();
	
	//Variables for entering the field
	private Vector2 spawnPosition 	= new Vector2();
	private Vector2 spawnDirection 	= new Vector2();
	private Vector2 isoPosition 	= new Vector2();
	private Vector2 mapBounds 		= new Vector2();
	private boolean enteredField 	= false;
	
	private final float DISTANCE_BOUNDS = 		5.0f;
	private final float MIN_SPAWN_DISTANCE = 	5.0f;
	private final float SPAWN_SPEED = 			5.0f;

	public void setup(){
		//NO-ACTION
	}
	
	public void wakeUp(Environment environment){
		//Spears
		spears = new ArrayList<Spear>();
		for( int i = 0 ; i < numSpears ; i++){
			RuntimeObjectFactory.addToObjectPool("spear", new Spear());
		}
		
		//Scream
		RuntimeObjectFactory.addToObjectPool("scream", new GryphonScream(environment));
		
		//Shadow
		shadow = new TamerShadow(this);
		environment.addObject(shadow);
		
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: Tamer has woken up! " + this.toString());
		
		//Z-index for drawing order
		setZindex(-1);
		setGraphics("tamer");
		this.environment = environment;
		this.environment.setTamer(this);
		
		//enter the field
		mapBounds.set(environment.getMapBounds());
		mapBounds.x -= DISTANCE_BOUNDS;
		mapBounds.y -= DISTANCE_BOUNDS;
		spawnPosition.set(getPosition());
		
	}
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("animated",graphics);
		render.loadGraphics(graphics, 1, 8);
		setSize(4,2.7f);
		setRenderType(graphics);
	}
	
	@Override
	public void update(float dt){
		if(environment.getState() == Environment.TAMER_ENTER){
			solveOrientation();
			isoPosition.set(IsoHelper.twoDToTileIso(getPosition()));
			//First Check when inside mapBounds
			if(isoPosition.x > -mapBounds.x && isoPosition.x < mapBounds.x && isoPosition.y > -mapBounds.y && isoPosition.y < mapBounds.y){
				//Then check that min distance is travelled
				if (getPosition().dst(spawnPosition) > MIN_SPAWN_DISTANCE){
					enteredField = true;
				}
			}
			getPosition().add(spawnDirection.tmp().mul(SPAWN_SPEED * dt));
		}else{
			solveOrientation();
			getPosition().add(getForce().tmp().mul(dt));
			getForce().mul(0);
			
			for(int i = 0 ; i < spears.size() ; i ++){
				if(shadow.getPosition().dst(spears.get(i).getPosition()) < 1 ){
					if(spears.get(i).isAttached()){
						spears.get(i).pickUp();
						spears.remove(i);
					}
				}
			}
		}	
	}
	
	/**
	 * @param direction
	 * Joystick uses this method to move tamer around
	 */
	public void manouver(Vector2 direction){
		direction.set(checkBounds(direction));
		
		direction.rotate(45);
		float power = direction.len();
		direction.nor().mul(power * SPEED);
		if(power > 0.5){
			setForce(direction);	
			setHeading(direction);

		}
	}
	
	public Vector2 checkBounds(Vector2 movement){
		Vector2 mapBounds = environment.getMapBounds();
		help.set(environment.getTamer().getShadow().getPosition());
		
		help.set(IsoHelper.twoDToTileIso(help));
		help.add(movement);
		
		
		if(help.x > mapBounds.x / 2 || help.x < -mapBounds.x / 2){
			movementAxis.set(1,0);
			movement.sub(VectorHelper.projection(movement,movementAxis));
		}
		if(help.y > mapBounds.y / 2 || help.y < -mapBounds.y / 2){
			movementAxis.set(0,1);
			movement.sub(VectorHelper.projection(movement,movementAxis));
		}
		
		return movement;	
	}
	/**
	 * @param direction
	 * Used only to turn tamer around his position when throwing a spear
	 */
	public void turn(Vector2 direction){
		direction.rotate(45);
		getHeading().lerp(direction, AIM_SPEED);
		getHeading().nor();
	}
	
	public void throwSpear(Spear spear,ArrayList<Vector2> waypoints){
		
		//Switch to SPEAR_CAM if spear is going to hit
		ArrayList<Creature> creatures = environment.getCreatures();
		int size = creatures.size();
		for(int i = 0 ; i < size ; i ++){
				Creature targetCreature = creatures.get(i).affectedCreature(waypoints.get(0), 0.5f);
				if(targetCreature != null){
					
					Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: switched to SPEAR_CAMERA");
					environment.getStage().setCameraHolder(TamerStage.SPEAR_CAMERA);
				}
		}
		
		spears.add(spear);
		spear.setPosition(getPosition());
		spear.throwAt(waypoints);
	}
	
	public void setSpawnDirection(Vector2 spawnDirection){
		this.spawnDirection.set(spawnDirection);
	}
	
	public void useScream(GryphonScream scream){
		scream.activate();
	}
	
	public boolean hasEnteredField(){
		return enteredField;
	}

	
	public TamerShadow getShadow(){
		return shadow;
	}
	
	public Spear getActiveSpear(){
		for (int i = 0; i < spears.size(); i++) {
			if (!spears.get(i).isAttached()) return spears.get(i); 
		} return null;
	}
}
