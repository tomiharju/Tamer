package com.me.tamer.gameobjects;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.me.tamer.core.TamerGame;
import com.me.tamer.core.TamerStage;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.gameobjects.tamer.Tamer;
import com.me.tamer.gameobjects.tiles.obstacles.Obstacle;
import com.me.tamer.physics.Contact;
import com.me.tamer.physics.ContactPool;
import com.me.tamer.physics.RigidBody;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.ui.ControlContainer;
import com.me.tamer.utils.DrawOrderComparator;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RuntimeObjectFactory;
import com.me.tamer.utils.Helper;

public class Environment extends Actor{

	private TamerStage stage;
	private ControlContainer controls;
	
	private DrawOrderComparator comparator = null;
	
	//Help vectors
	private Vector2 help = new Vector2();
	private Vector2 movementAxis = new Vector2();
	
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
	
	//Physical contact list
	private ArrayList<Contact> contacts;
	private ArrayList<RigidBody> rigidbodies;
	
	//Physics optimization variables
	Vector2 impulseA = new Vector2();
	Vector2 impulseB = new Vector2();
	Vector2 bVelocity = new Vector2();
	Vector2 normal = new Vector2();
	
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
	
	//inputs for aiming
	private boolean aimMode = false;
		
	public Environment(){	
		gameobjects 	= new ArrayList<GameObject>();
		carbages 		= new ArrayList<GameObject>();
		newobjects 		= new ArrayList<GameObject>();
		obstacles 		= new ArrayList<Obstacle>();
		creatures		= new ArrayList<Creature>();
		comparator 		= new DrawOrderComparator();
		RuntimeObjectFactory.createLinkToLevel(this);
		
		controls = ControlContainer.instance();
		
		ContactPool.createPool(100);
		
		sound = SoundManager.instance();
		
		//Create listener for aim-mode
		setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		createInputListener();
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
		resolveObstacles(dt);
		int numObjects = gameobjects.size();
		
		switch (state){
			case(NORMAL):
				
				//enable controls
				controls.setInputDisabled(false);
			
				for(int k = 0 ; k < numObjects ; k++){
					gameobjects.get(k).update(dt);
				}
				break;
			case(TAMER_ENTER):
							//disable controls
				controls.setInputDisabled(true);
			
				for(int k = 0 ; k < numObjects ; k++){
					//System.out.println(gameobjects.get(k));
					if (gameobjects.get(k).getClass()==Tamer.class){
						gameobjects.get(k).update(dt);
						if (((Tamer)tamer).hasEnteredField()){
							Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
									+ " :: setting state to NORMAL");
							state = NORMAL;
							
							Gdx.app.log(TamerGame.LOG, this.getClass()
									.getSimpleName() + " :: Playing sound entering");
							sound.play(TamerSound.OPENING);
						}
					}		
				}
				//System.out.println("-------------");
				break;
			case (SPEAR_TIME):
				for(int k = 0 ; k < numObjects; k++){
					if (gameobjects.get(k).getClass()==Spear.class)gameobjects.get(k).update(dt);
				}
				break;
			default:
				Gdx.app.error(TamerGame.LOG, this.getClass().getSimpleName()
						+ " :: state default case");
				break;
		}
	}

	public void draw(SpriteBatch batch, float parentAlpha){
		
		batch.setProjectionMatrix(stage.getCamera().combined); 
		int numObjects = gameobjects.size();
		sortDrawOrder(numObjects);
		for(int k = 0 ; k < numObjects ; k++)
			gameobjects.get(k).draw(batch);
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
	
	public void setAimMode(boolean b){
		aimMode = b;
	}
	
	public void createInputListener(){
		this.addListener(new InputListener(){
			Vector2 input = new Vector2();
			Vector2 waypoint1 = new Vector2(),waypoint2 = new Vector2(),waypoint3 = new Vector2(),targetPoint = new Vector2();
			private ArrayList<Vector2> waypoints = new ArrayList<Vector2>();
			
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		
				if (aimMode){
					input.set(x - Gdx.graphics.getWidth() / 2,y - Gdx.graphics.getHeight() / 2);
					targetPoint.set(input);
					targetPoint = Helper.screenToWorld(targetPoint);
					//this is where spear ends up
					help.set( ((Tamer)tamer).getShadow().getPosition() );
					waypoint1.set(help.add(targetPoint));
					
					help.set( ((Tamer)tamer).getPosition() );
					waypoint2.set(help.add(targetPoint));
					//Set camera to follow way point 2
					//cameraPoint.set(waypoint2);
					
					help.set( ((Tamer)tamer).getPosition().tmp().add(-3,3) );
					waypoint3.set(help.add(targetPoint.tmp().mul(0.8f)));
					
					waypoints.clear();
					waypoints.add(waypoint1);
					waypoints.add(waypoint2);
					waypoints.add(waypoint3);
					
					Spear spear = (Spear) RuntimeObjectFactory.getObjectFromPool("spear");
					if(spear != null)
						((Tamer)tamer).throwSpear(spear, waypoints );
					else
						System.err.println("No spears remaining");
				
					System.out.println(targetPoint);
						
					return true;
				}else
					return false;
			}
		});
	}
	
	/**
	 * @param dt
	 * Uses rigidbodies to generate Contact objects
	 * Uses contact objects to calculate collision responses
	 * Resolves each collision by adding proper forces.
	 */
	public void resolveCollisions(float dt){
		contacts.clear();
		int numObjects = gameobjects.size();
		int numBodies = rigidbodies.size();
		for(int k = 0 ; k < numObjects ; k++)
			gameobjects.get(k).resolveForces(dt);
		
		/*
		for(int i = 0 ; i < numBodies ; i ++){
			if(!rigidbodies.get(i).isDynamic())
				continue;
			RigidBody firstbody = rigidbodies.get(i);
			for(int j = 0 ; j < numBodies ; j++){
				RigidBody secondBody = rigidbodies.get(j);
				if(secondBody != firstbody){
					Contact c = firstbody.generateContact(secondBody);
					if(c != null)
						contacts.add(c);
				}
			}
		}
		
		if(contacts.size() > 0){
			int numContacts = contacts.size();
			for(int i = 0 ; i < numContacts ; i++){
				Contact c = contacts.get(i);
				normal.set(c.getN());
				RigidBody b = c.getObjB();
				RigidBody a = c.getObjA();
				float dist = c.getDist();
				//Relative velocity ( vector )
			
				Vector2 rv = b.getVelocity().tmp().sub(a.getVelocity());
				//Relative normal velocity ( scalar )
				float relNv = rv.dot(normal);
			
				float remove = relNv + dist / dt;
				float imp = remove / ( b.getInvMass() + a.getInvMass() );
				imp = Math.min(imp, 0);
				Vector2 impulse = normal.mul(imp);
			
				impulseA.set(impulse);
				impulseB.set(impulse);
				impulseA.mul(a.getInvMass());
				impulseB.mul(b.getInvMass());
	
				a.getVelocity().add(impulseA);
				b.getVelocity().sub(impulseB);
	
				//!!!PUT CONTACT BACK INTO POOL!!!
				ContactPool.restore(c);
			}	
		}	
		*/	
	}
	
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

	public synchronized void addNewObjects(){
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

		//Create new contact pool
		ContactPool.createPool(100);
	}
	
	/**
	 * @param obj
	 * Levelcreator calls this function to add new objects
	 */
	public void addObject(GameObject obj){
		gameobjects.add(obj);
	}
	
	public void addRigidBody(RigidBody body){
		rigidbodies.add(body);
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
	}
	
	public void dispose(){
		gameobjects.clear();
		carbages.clear();
		newobjects.clear();
		rigidbodies.clear();
		tamer = null;
		obstacles.clear();
	}
	
	public boolean checkInsideBounds(Vector2 pos, float offset){
		help.set(Helper.worldToScreen(pos));;

		if(help.x < mapBounds.x / 2 - offset && help.x > -mapBounds.x / 2 + offset && help.y < mapBounds.y / 2 - offset && help.y > -mapBounds.y / 2 + offset){
			return true;
		}
		
		return false;	
	}

	public Vector2 getMapBounds(){
		return mapBounds;
	}

	public ArrayList<RigidBody> getRigidBodies(){
		return rigidbodies;
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
