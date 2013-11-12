package com.me.tamer.gameobjects.tamer;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.RuntimeObjectFactory;


public class Tamer extends DynamicObject{
	
	private float SPEED 		= 0f;
	private final float AIM_SPEED 	= 0.001f; //heading interpolating coefficient (Ttu mikä sana XD)
	private final float BORDER_OFFSET = -5.0f;
	private TamerShadow shadow		=null;
	private GryphonScream scream 	= null;
	private Environment environment;
	private Vector2 help 			= new Vector2();
	private Vector2 movementAxis 	= new Vector2();
	
	//spear related variables
	private int numSpears 			= 3;
	private ArrayList<Spear> spears = null;
	private boolean targetFound = false;
	private Vector2 targetPoint = new Vector2();
	
	//Variables for entering the field
	private Vector2 spawnPosition 	= new Vector2();
	private Vector2 spawnDirection 	= new Vector2();
	private Vector2 isoPosition 	= new Vector2();
	private Vector2 mapBounds 		= new Vector2();
	private boolean enteredField 	= false;
	
	private final float DISTANCE_BOUNDS = 5.0f;
	private final float MIN_SPAWN_DISTANCE = 5.0f;
	private final float SPAWN_SPEED = 5.0f;
	
	
	
	private SoundManager sound;


	public void setup(){
		//NO-ACTION
	}
	
	public void wakeUp(Environment environment){
		environment.setState(Environment.TAMER_ENTER);
		//Spears
		spears = new ArrayList<Spear>();
		for( int i = 0 ; i < numSpears ; i++){
			RuntimeObjectFactory.addToObjectPool("spear", new Spear());
		}
		
		//Scream
		//RuntimeObjectFactory.addToObjectPool("scream", new GryphonScream(environment));
		scream = new GryphonScream(environment);
		
		//Shadow
		shadow = new TamerShadow(this);
		
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: Tamer has woken up! " + this.toString());
		
		//Z-index for drawing order
		setZindex(-10);
		setGraphics("tamer");
		this.environment = environment;
		this.environment.setTamer(this);
		
		//enter the field
		mapBounds.set(environment.getMapBounds());
		mapBounds.x -= DISTANCE_BOUNDS;
		mapBounds.y -= DISTANCE_BOUNDS;
		spawnPosition.set(shadow.getPosition());
		
		//sound
		sound = SoundManager.instance();
	}
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("animated",graphics);
		render.loadGraphics(graphics, 1, 8);
		setSize(5,3.1f);
		setRenderType(graphics);
	}
	
	@Override
	public void update(float dt){
		if(environment.getState() == Environment.TAMER_ENTER){
			//isoPosition.set(Helper.worldToScreen(getPosition()));
			//First Check when inside mapBounds
	//		if(isoPosition.x > -mapBounds.x && isoPosition.x < mapBounds.x && isoPosition.y > -mapBounds.y && isoPosition.y < mapBounds.y){
				//Then check that min distance is travelled
				if (shadow.getPosition().dst(spawnPosition) > MIN_SPAWN_DISTANCE){
					enteredField = true;
				}
			//}
			getPosition().add(spawnDirection.tmp().mul(SPAWN_SPEED * dt));
		}else{
			getPosition().add(getHeading().tmp().mul(SPEED * dt));
			SPEED = 0;
			
			for(int i = 0 ; i < spears.size() ; i ++){
				if(!spears.get(i).isJustDropped() && shadow.getPosition().dst(spears.get(i).getPosition()) < 1 ){
					if(spears.get(i).isAttached()){
						spears.get(i).pickUp();
						spears.remove(i);
					}
				}
			}
		}	
		
		//Update shadow
		shadow.update(dt);
		scream.update(dt);
	}
	
	@Override
	public void draw(SpriteBatch batch){
		super.draw(batch);
		shadow.draw(batch);
		scream.draw(batch);
	}
	
	/**
	 * @param direction
	 * Joystick uses this method to move tamer around
	 */
	public void manouver(Vector2 direction){
		direction.set(checkBounds(direction));
		
		direction.rotate(45);
		float power = direction.len();
		SPEED = 0.2f * power;
		
		if(power > 0.5){
			setHeading(direction);
		}
	}
	
	public Vector2 checkBounds(Vector2 movement){
		Vector2 mapBounds = environment.getMapBounds();
		help.set(environment.getTamer().getShadow().getPosition());
		
		help.set(Helper.worldToScreen(help));
		help.add(movement.tmp().mul(Gdx.graphics.getDeltaTime()));
		
		if(help.x > mapBounds.x / 2 - BORDER_OFFSET || help.x < -mapBounds.x / 2 + BORDER_OFFSET){
			movementAxis.set(1,0);
			movement.sub(Helper.projection(movement,movementAxis));
		}
		if(help.y > mapBounds.y / 2 - BORDER_OFFSET || help.y < -mapBounds.y / 2 + BORDER_OFFSET){
			movementAxis.set(0,1);
			movement.sub(Helper.projection(movement,movementAxis));
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
	
	public void throwSpear(){
		Spear spear = (Spear) RuntimeObjectFactory.getObjectFromPool("spear");
		if(spear != null){
			spear.setPosition(getPosition());
			spears.add(spear);	
		}	
		else
			System.err.println("No spears remaining");
		
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: playing throwing sound");
		sound.setVolume(0.8f);
		sound.play(TamerSound.THROW);
		
	}
	
	public void setSpawnDirection(Vector2 spawnDirection){
		this.spawnDirection.set(spawnDirection);
	}
	
	public void useScream(){
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
