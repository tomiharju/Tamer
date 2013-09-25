package com.me.tamer.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.tiles.ObstacleTile;
import com.me.tamer.physics.Contact;
import com.me.tamer.physics.RigidBody;

public class Level {

	//Settings
	private Vector2 camBounds = null;
	private Vector2 camBoundsOffset = null;
	
	//Gameobject data
	private ArrayList<GameObject> gameobjects = null;
	private ArrayList<GameObject> carbages	= null;
	private ArrayList<GameObject> newobjects = null;
	private DynamicObject 	tamer = null;
	private ArrayList<ObstacleTile> obstacles = null;
	private ArrayList<Worm> worms		= null;
	
	//Physical contact list
	private ArrayList<Contact> contacts;
	private ArrayList<RigidBody> rigidbodies;
	
	public Level(){
		gameobjects 	= new ArrayList<GameObject>();
		carbages 		= new ArrayList<GameObject>();
		newobjects 		= new ArrayList<GameObject>();
		obstacles 		= new ArrayList<ObstacleTile>();
		worms			= new ArrayList<Worm>();
		contacts 		= new ArrayList<Contact>();
		rigidbodies		= new ArrayList<RigidBody>();
	}
	
	/**
	 * @param dt
	 * General update loop
	 */
	public void update(float dt){
		runCarbageCollection();
		addNewObjects();
		resolveObstacles();
		resolveCollisions(dt);
		for(GameObject o : gameobjects)
			o.update(dt);
	}
	/**
	 * @param batch
	 * General drawing loop
	 */
	public void draw(SpriteBatch batch){
		for(GameObject o : gameobjects)
			o.draw(batch);
	}
	
	public void resolveObstacles(){
		for(ObstacleTile ot : obstacles)
			for(Worm w : worms)
				ot.resolveTile(w);
				
	}
	
	/**
	 * @param dt
	 * Uses rigidbodies to generate Contact objects
	 * Uses contact objects to calculate collision responses
	 * Resolves each collision by adding proper forces.
	 */
	public void resolveCollisions(float dt){
		contacts.clear();
		
		for(RigidBody body : rigidbodies){
			if(!body.isDynamic())
				continue;
			RigidBody firstbody = body;
			for(RigidBody anotherbody : rigidbodies){
				if(anotherbody != firstbody){
					Contact c = firstbody.generateContact(anotherbody);
					if(c != null)
						contacts.add(c);
				}
			}
		}
		
		if(contacts.size() > 0){
			for(Contact c : contacts){
				Vector2 normal = c.getN();
				RigidBody b = c.getObjB();
				RigidBody a = c.getObjA();
				//Relative velocity ( vector )
				Vector2 rv = b.getVelocity().cpy().sub(a.getVelocity());
				//Relative normal velocity ( scalar )
				float relNv = rv.dot(normal);
				
				float remove = relNv + c.getDist() / dt;
				
				float imp = remove / ( b.getInvMass() + a.getInvMass() );
				float newImp = Math.min(imp, 0);
				Vector2 addA = normal.cpy().mul(newImp);
				Vector2 addB = normal.cpy().mul(newImp);
				
				addA.mul(a.getInvMass());
				addB.mul(b.getInvMass());
				
				a.getVelocity().add(addA);
				b.getVelocity().sub(addB);
				if(c.getDist()<0.1){
					Vector2 leftNor = normal.cpy().rotate(90);
					Vector2 rightNor = normal.cpy().rotate(-90);
					float dotleft = b.getVelocity().dot(leftNor);
					float dotRight = b.getVelocity().dot(rightNor);
					if(dotleft > 0){
						b.getOwner().setHeading(leftNor);
						a.getOwner().setHeading(rightNor);
					}
					else if(dotRight > 0){
						b.getOwner().setHeading(rightNor);
						a.getOwner().setHeading(leftNor);
					}
				}
				
			}
			
			
			
			
		}
		
		
		
		
	}
	
	
	
	public void runCarbageCollection(){
		for(GameObject o : gameobjects)
			if(o.isCarbage())
				carbages.add(o);
		if(carbages.size() > 0){
			gameobjects.removeAll(carbages);
			carbages.clear();
		}
	}
	public void addNewObjects(){
		if(newobjects.size() > 0){
			for(GameObject go : newobjects){
				go.setup();
				if(go.getRigidBody() !=null )
					rigidbodies.add(go.getRigidBody());
			}
			gameobjects.addAll(newobjects);
			newobjects.clear();
		}
		
	}
	
	
	
	/**
	 * Looks through all added gameobjects, and adds each obstacle to separate obstacle list
	 * We use this obstacle list to apply effects on worms ( for example we check if worms is inside quicksand tile, and then apply force to it)
	 */
	public void setupObjects(){
		for(GameObject go : gameobjects){
			if(go instanceof ObstacleTile)
				obstacles.add((ObstacleTile) go);
			if(go instanceof SpawnPoint)
				((SpawnPoint) go).startSpawning(this);
			if(go.getRigidBody() != null)
				rigidbodies.add(go.getRigidBody());
		}
		//Create and set tamer into level
		setTamerPos("0:0");
		
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
	
	/**
	 * LevelCreator calls this to set Camera borders, could be expanded later if more settings needed
	 */
	public void setCameraBounds(String value){
		
		String[] values =value.split(":");
		
		camBounds = new Vector2(Float.parseFloat(values[0]), Float.parseFloat(values[1]));
		camBoundsOffset = new Vector2(Float.parseFloat(values[2]), Float.parseFloat(values[3]));
		
		System.out.println("Cambounds: " +camBounds.x +", " +camBounds.y);
	}
	
	public Vector2 getCamBounds(){
		return camBounds;
	}
	
	public Vector2 getCamBoundsOffset(){
		return camBoundsOffset;
	}
	
	/**
	 * Creates tamer and sets starting position
	 */
	public void setTamerPos(String pos){
		tamer = new Tamer();
		tamer.setRenderer("static:tamer");
		tamer.setSize("1:1");
		tamer.setPosition(pos);
		tamer.setVelocity("0:0");
		tamer.setForce("0:0");
		tamer.setMass("10");
		tamer.setRigidBody("circle");
		gameobjects.add(tamer);
	}
	public Tamer getTamer(){
		return (Tamer) tamer;
	}
	public void dispose(){
		gameobjects.clear();
		carbages.clear();
		newobjects.clear();
		rigidbodies.clear();
		tamer = null;
	}
	
	
}
