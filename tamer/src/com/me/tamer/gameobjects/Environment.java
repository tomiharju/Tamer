package com.me.tamer.gameobjects;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.me.tamer.core.TamerGame;
import com.me.tamer.core.TamerStage;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.gameobjects.tamer.Tamer;
import com.me.tamer.gameobjects.tiles.TileMap;
import com.me.tamer.gameobjects.tiles.obstacles.Obstacle;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.ui.ControlContainer;
import com.me.tamer.utils.DrawOrderComparator;
import com.me.tamer.utils.EventPool;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.RuntimeObjectFactory;


public class Environment extends Actor{

	private TamerStage stage;
	private ControlContainer controls;
	private DrawOrderComparator comparator = null;
	//Help vectors
	private Vector2 help = new Vector2();
	//Settings
	private Vector2 mapBounds = null;
	private Vector2 cameraBounds = null;
	//Gameobject data
	private ArrayList<GameObject> gameobjects 	= null;
	private ArrayList<GameObject> carbages		= null;
	private ArrayList<GameObject> newobjects 	= null;
	private DynamicObject tamer 				= null;
	private ArrayList<Obstacle> obstacles 		= null;
	private ArrayList<Creature> creatures		= null;

	//Optimization variables
	Vector2 tamerpos = new Vector2();

	//Drawing-order
	private int loopCount = 0;
	private int sortRate = 6;
	
	//States during GAME_RUNNING
	public static final int NORMAL = 0;
	public static final int TAMER_ENTER = 1;
	public static final int SPEAR_TIME = 2;
	private int state = 0;
	
	//SoundManager
	SoundManager sound;
		
	public Environment(){	
		gameobjects 	= new ArrayList<GameObject>();
		carbages 		= new ArrayList<GameObject>();
		newobjects 		= new ArrayList<GameObject>();
		obstacles 		= new ArrayList<Obstacle>();
		creatures		= new ArrayList<Creature>();
		comparator 		= new DrawOrderComparator();
		RuntimeObjectFactory.createLinkToLevel(this);
		
		controls = ControlContainer.instance();
		sound = SoundManager.instance();
		
		RenderPool.createAtlas();
	}
	
	public void setStage(TamerStage stage){
		this.stage = stage;
	}
	
	/**
	 * @param dt
	 * General update loop
	 */
	public void act(float dt){
		runCarbageCollection();
		addNewObjects();
		stepTimers(dt);
		resolveObstacles(dt);

		int numObjects = gameobjects.size();
		
		switch (state){
			case(NORMAL):
				for(int k = 0 ; k < numObjects ; k++){
					gameobjects.get(k).update(dt);
				}
				break;
			case(TAMER_ENTER):
				if(tamer != null)tamer.update(dt);
				if(((Tamer) tamer).hasEnteredField()){
					controls.enableInput();
					state = NORMAL;
					sound.play(TamerSound.OPENING);
				}
				break;
			case (SPEAR_TIME):
				for(int k = 0 ; k < numObjects; k++){
					if (gameobjects.get(k).getClass() == Spear.class)gameobjects.get(k).update(dt);
				}
				break;
			default:
				break;
		}
		
	}

	public void draw(SpriteBatch batch, float parentAlpha){
		
		batch.setProjectionMatrix(stage.getCamera().combined); 
		int numObjects = gameobjects.size();
		sortDrawOrder(numObjects);
		for(int k = 0 ; k < numObjects ; k++){
			Vector2 pos = gameobjects.get(k).getPosition();
			if(gameobjects.get(k).getClass() == TileMap.class)
				gameobjects.get(k).draw(batch);
			else if(pos.x > tamerpos.x - Helper.VIRTUAL_SIZE_X *1.9 && pos.x < tamerpos.x + Helper.VIRTUAL_SIZE_X *1.9
					&& pos.y > tamerpos.y - Helper.VIRTUAL_SIZE_Y *1.1 && pos.y < tamerpos.y + Helper.VIRTUAL_SIZE_Y *1.1){
				
				gameobjects.get(k).draw(batch);
			}
		}
	}
	
	public void debugDraw(ShapeRenderer sr){

		sr.setProjectionMatrix(stage.getCamera().combined);
		
		int size = gameobjects.size();
		for(int i = 0 ; i < size ; i++)
			if(gameobjects.get(i).getDebug()){
				gameobjects.get(i).debugDraw(sr);
			}
	}
	
	public void sortDrawOrder(int numObjects){
		if(loopCount % sortRate == 0){
			if(numObjects > 1){
				Collections.sort(gameobjects, comparator);
				loopCount = 0;
			}
		} loopCount++;
	}
	
	
	
	public void stepTimers(float dt){
		EventPool.step(dt);
	}
	
	/**
	 * @param dt
	 * "Resolves" each gamemeobject which impelent Obstalce interface
	 * Resolve is currently used to detect and respond to collisions,
	 * and quicksand uses it for worm pulling.
	 * 
	 */
	public void resolveObstacles(float dt){
		int size = obstacles.size();
		for(int i = 0 ; i < size ; i++){
			obstacles.get(i).resolve(creatures);
		}

	}
	public void runCarbageCollection(){
		int size = gameobjects.size();
		for( int i = 0 ; i < size ; i ++)
			if(gameobjects.get(i).isCarbage()){
				gameobjects.get(i).dispose(this);
				carbages.add(gameobjects.get(i));
			}
		
		if(carbages.size() > 0){
			gameobjects.removeAll(carbages);
			carbages.clear();
			Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: Gameobjects after carbage collection "+gameobjects.size());
		}
	}

	public void addNewObjects(){
		if(newobjects.size() > 0){
			for(GameObject go : newobjects){
				go.wakeUp(this);
				gameobjects.add(go);
			}
			newobjects.clear();
			}	
	}
	
	public void addNewObject(GameObject obj){
		newobjects.add(obj);
	}
	
	/**
	 * Looks through all added gameobjects, and adds each obstacle to separate obstacle list
	 * We use this obstacle list to apply effects on worms ( for example we check if worms is inside quicksand tile, and then apply force to it)
	 */
	public void setupObjects(){
		//Call setup method which then adds that objects properties to level datastructures
		//For example calling setup on worm, it creates all the parts.
		for(GameObject go : gameobjects){
				go.setup(this);
		}
}
	
	/**
	 * @param obj
	 * Levelcreator calls this function to add new objects
	 */
	public void addObject(GameObject obj){
		gameobjects.add(obj);
	}
	
	public void addObstacle(Obstacle obstacle){
		this.obstacles.add(obstacle);
	}
	
	/**
	 * LevelCreator calls this to set Camera borders, could be expanded later if more settings needed
	 */
	public void setMapSize(String value){
		//DO WE NEED THIS?
	}
	
	public void setMapBounds(String value){
		String[] values = value.split(":");
		mapBounds = new Vector2(Float.parseFloat(values[0]), Float.parseFloat(values[1]));
	}
	
	/**
	 * 
	 */
	public void setTamer(Tamer tamer){
		this.tamer = tamer;
		tamerpos = tamer.getPosition();
	}
	
	public void dispose(){
		for(GameObject go : gameobjects){
			go.dispose(this);
		}
		gameobjects.clear();
		carbages.clear();
		newobjects.clear();
		tamer = null;
		obstacles.clear();
	}
	
	public boolean checkInsideBounds(Vector2 pos, float offset){
		help.set(Helper.worldToScreen(pos));

		if(help.x < mapBounds.x / 2 - offset && help.x > -mapBounds.x / 2 + offset && help.y < mapBounds.y / 2 - offset && help.y > -mapBounds.y / 2 + offset){
			return true;
		}
		
		return false;	
	}

	public Vector2 getMapBounds(){
		return mapBounds;
	}
	
	public Vector2 getCamBounds(){
		return cameraBounds;
	}
	
	public ArrayList<Creature> getCreatures(){
		return creatures;
	}
	public ArrayList<Obstacle> getObstacles(){
		return obstacles;
	}
	
	public Tamer getTamer(){
		return (Tamer) tamer;
	}
	
	public TamerStage getStage(){
		return stage;
	}
	
	public void setState(int s){
		state = s;
	}
	
	public int getState(){
		return state;
	}
}
