package com.me.tamer.gameobjects;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.Level;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.renders.Renderer.RenderType;
import com.me.tamer.gameobjects.superclasses.Creature;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.utils.GameObjectFactory;
import com.me.tamer.utils.RuntimeObjectFactory;

/**
 * @author tharju
 * Spawn point object for worms.
 * Knows how many worms to spawn, and the time interval to spawn them
 */
public class SpawnPoint extends StaticObject{
	private String spawnType;
	private Vector2 spawnVelocity;
	private int initialSleep;
	private int sleepTime;
	private int spawnCount;
	private int numCreated = 0;
	
	//IMPORTANT: spawn number is used to distinguish spawns from each other.
	private int spawnId = 0;
	
	//EXPERIMENTAL STUFF
	private ArrayList<Creature> creatures;
	
	public SpawnPoint(){
		creatures = new ArrayList<Creature>();
	}
	public void setup(Environment level){
		level.addNewObject(this);
		startSpawning();
	}
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setSize("1:1");
		this.renderType = graphics;
	}
	public void setSpawnCount(String count){
		this.spawnCount = Integer.parseInt(count);
	}
	public void setSpawnDirection(String vel){
		float angle = Float.parseFloat(vel);
		angle += 45;
		this.spawnVelocity = new Vector2(1,0);
		this.spawnVelocity.setAngle(angle);
		System.out.println("SPAWN ANGLE IS "+ spawnVelocity.toString());
		
	}
	
	public void setSleepTime(String time){
		this.sleepTime = Integer.parseInt(time)*1000;
	}
	
	public void setInitialSleepTime(String time){
		this.initialSleep = Integer.parseInt(time)*1000;
	}
	
	public void addWorm(Worm worm){
		spawnType = "worm";
		RuntimeObjectFactory.addToObjectPool("worm"+spawnId,(GameObject)worm);
		creatures.add(worm);
		worm.setPosition(position);
		worm.setVelocity(spawnVelocity);
	}
	public void addAnt(AntOrc ant){
		spawnType = "ant";
		RuntimeObjectFactory.addToObjectPool("ant"+spawnId,(GameObject)ant);
		creatures.add(ant);
		ant.setPosition(position);
		ant.setVelocity(spawnVelocity);
	}

	/**
	 * @param init_sleep how long till the first spawn
	 * @param interval interval between spawning
	 * @param count how many spawns in total
	 * @param position grid number, which is turned into screen coordinate
	 * @param spawn_type is the object type to spawn. Currently worm or ant
	 */
	public void setup(){
		System.out.println("Spawn number "+spawnId + " Started spawning!");
		startSpawning();
	}
	public void startSpawning(){
		System.out.println("Started to spawn");
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Thread.sleep(initialSleep);
					RuntimeObjectFactory.getObjectFromPool("tamer");
					System.out.println("TAMER HAS ENTERED THE FIELD!");
					while(numCreated < spawnCount){
						Thread.sleep(sleepTime);
						numCreated++;
						//Add newly created worm to main gameobject list
						if(spawnType.equalsIgnoreCase("worm")){
							System.out.println("ANOTHER WORM ENTERED");
							RuntimeObjectFactory.getObjectFromPool("worm"+spawnId);
						}else if(spawnType.equalsIgnoreCase("ant"))
							RuntimeObjectFactory.getObjectFromPool("ant"+spawnId);
						//Sleep for the actual spawn interval
					
					}
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}).start();
	}
	


	public void setLevel(Environment level) {
		//this.level = level;
	}

	
	public void setSpawnId(String number){
		this.spawnId = Integer.parseInt(number);
	}

	public void setInitialSleep(String initialSleep) {
		this.initialSleep = Integer.parseInt(initialSleep)*1000;
	}

	
	public void setTamerSpawn(String flag){
		int value = Integer.parseInt(flag);
		if(value == 1){
			Tamer tamer = new Tamer();
			tamer.setPosition(position);
			tamer.setVelocity(spawnVelocity);
			RuntimeObjectFactory.addToObjectPool("tamer",(GameObject)tamer);
		}
	}
	/*public void setSpawnVelocity(String vel){
		this.spawnVelocity = vel;
	}*/
	public Vector2 getSpawnVelocity(){
		return spawnVelocity;
	}
	


}
