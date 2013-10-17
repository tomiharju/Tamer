package com.me.tamer.gameobjects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.superclasses.Creature;
import com.me.tamer.gameobjects.tiles.ObstacleTile;
import com.me.tamer.physics.Contact;
import com.me.tamer.physics.ContactPool;
import com.me.tamer.physics.RigidBody;
import com.me.tamer.utils.DrawOrderComparator;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RuntimeObjectFactory;

public class Level {

	//Settings

	private Vector2 mapBounds = null;
	private Vector2 cameraBounds = null;
	private Vector2 camBoundsOffset = null;
	
	//Gameobject data

	private ArrayList<GameObject> gameobjects = null;
	private ArrayList<GameObject> carbages	= null;
	private ArrayList<GameObject> newobjects = null;
	private DynamicObject 	tamer = null;
	private ArrayList<ObstacleTile> obstacles = null;
	private ArrayList<Creature> creatures	= null;
	
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
		
	public Level(){
		gameobjects 	= new ArrayList<GameObject>();
		carbages 		= new ArrayList<GameObject>();
		newobjects 		= new ArrayList<GameObject>();
		obstacles 		= new ArrayList<ObstacleTile>();
		creatures		= new ArrayList<Creature>();
		contacts 		= new ArrayList<Contact>();
		rigidbodies		= new ArrayList<RigidBody>();
		RuntimeObjectFactory.createLinkToLevel(this);

	}
	
	/**
	 * @param dt
	 * General update loop
	 */
	public void update(float dt){
		runCarbageCollection();
		addNewObjects();
		resolveCollisions(dt);
		int numObjects = gameobjects.size();
		for(int k = 0 ; k < numObjects ; k++)
			gameobjects.get(k).update(dt);
	}
	/**
	 * @param batch
	 * General drawing loop
	 */
	public void draw(SpriteBatch batch){
		int numObjects = gameobjects.size();
		sortDrawOrder(numObjects);
		for(int k = 0 ; k < numObjects ; k++)
			gameobjects.get(k).draw(batch);
	}
	
	
	public void debugDraw(ShapeRenderer sr){
		for(GameObject o : gameobjects)
			if(o.getDebug()){
				o.debugDraw(sr);
			}
	}
	
	public void sortDrawOrder(int numObjects){
		if(loopCount % sortRate == 0){
			if(numObjects > 1){
				Collections.sort(gameobjects, new DrawOrderComparator());
				/*for (GameObject g : gameobjects){
					//if(g.getPosition() != null)
					//System.out.println(g.getClass().getName() +"..." + g.getPosition().y);
					System.out.println(g.getClass().getName() +"..." + g.getZIndex());
				}
				System.out.println("-------------------------------");*/
			}
		} loopCount++;
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
			System.out.println("Gameobjects after carbage collection "+gameobjects.size());
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
	
	/**
	 * Looks through all added gameobjects, and adds each obstacle to separate obstacle list
	 * We use this obstacle list to apply effects on worms ( for example we check if worms is inside quicksand tile, and then apply force to it)
	 */
	public void setupObjects(){
		//Call setup method which then adds that objects properties to level datastructures
		//For example calling setup on worm, it creates all the parts.
		for(GameObject go : gameobjects){
				go.setup();
		}
		//Create dummytamer for UiElements to work
		tamer = new Tamer();
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
	
	public void addNewObject(GameObject obj){
		newobjects.add(obj);
	}
	
	public void addRigidBody(RigidBody body){
		rigidbodies.add(body);
	}
	
	
	/**
	 * LevelCreator calls this to set Camera borders, could be expanded later if more settings needed
	 */
	public void setMapSize(String value){
		
		String[] values =value.split(":");
		
		mapBounds = new Vector2(Float.parseFloat(values[0]), Float.parseFloat(values[1]));
//		mapBounds.rotate(45);
//		cameraBounds = new Vector2(mapBounds.x  , mapBounds.y);
//		System.err.println(mapBounds.toString());
//		camBoundsOffset = new Vector2(Float.parseFloat(values[2]), Float.parseFloat(values[3]));
	}
	
	/**
	 * Creates tamer and sets starting position
	 */
	public void setTamer(Tamer tamer){
		this.tamer = tamer;
		gameobjects.add(tamer);
	}
	
	
	public void dispose(){
		gameobjects.clear();
		carbages.clear();
		newobjects.clear();
		rigidbodies.clear();
		tamer = null;
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
	
	public Vector2 getCamBoundsOffset(){
		return camBoundsOffset;
	}
	
	public ArrayList<Creature> getCreatures(){
		return creatures;
	}
	
	public Tamer getTamer(){
		return (Tamer) tamer;
	}
	
}
