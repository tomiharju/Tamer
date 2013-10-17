package com.me.tamer.gameobjects;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.Environment;
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
	public void setSpawnCount(String count){
		this.spawnCount = Integer.parseInt(count);
	}
	public void setSpawnVelocity(String vel){
		String[] values = vel.split(":");
		float x = Float.parseFloat(values[0]);
		float y = Float.parseFloat(values[1]);
		this.spawnVelocity = new Vector2(x,y);
	}
	public void setSleepTime(String time){
		this.sleepTime = Integer.parseInt(time)*1000;
	}
	public void setInitialSleepTime(String time){
		this.initialSleep = Integer.parseInt(time)*1000;
	}
	
	public void addWorm(Creature worm){
		RuntimeObjectFactory.addToObjectPool("worm"+spawnId,(GameObject)worm);
		creatures.add(worm);
		worm.setPosition(position);
		worm.setVelocity(spawnVelocity);
	}
	public void addAnt(Creature ant){
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
					while(numCreated < spawnCount){
						numCreated++;
						//Add newly created worm to main gameobject list
						if(spawnType.equalsIgnoreCase("worm")){
							RuntimeObjectFactory.getObjectFromPool("worm"+spawnId);
						}else if(spawnType.equalsIgnoreCase("ant"))
							RuntimeObjectFactory.getObjectFromPool("ant"+spawnId);
						//Sleep for the actual spawn interval
						Thread.sleep(sleepTime);
					}
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}).start();
	}
	


	public void setLevel(Level level) {
		//this.level = level;
	}

	public void setSpawnType(String spawnType) {
		this.spawnType = spawnType;
	}
	public void setSpawnId(String number){
		this.spawnId = Integer.parseInt(number);
	}

	public void setInitialSleep(String initialSleep) {
		this.initialSleep = Integer.parseInt(initialSleep)*1000;
	}

	
	public void setTamer(){
		Tamer tamer = new Tamer();
		tamer.setPosition(position);
		tamer.setVelocity(spawnVelocity);
		RuntimeObjectFactory.addToObjectPool("tamer",(GameObject)tamer);
	}
	/*public void setSpawnVelocity(String vel){
		this.spawnVelocity = vel;
	}*/
	public Vector2 getSpawnVelocity(){
		return spawnVelocity;
	}
	


}
