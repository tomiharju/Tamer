package com.me.tamer.gameobjects;

import java.util.LinkedHashMap;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.Environment;
import com.me.tamer.gameobjects.renders.Renderer.RenderType;
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
	private Level level;
	private String spawnType;
	private String spawnVelocity;
	private int initialSleep;
	private int interval;
	private int spawnCount;
	private int numCreated = 0;
	

	/**
	 * @param init_sleep how long till the first spawn
	 * @param interval interval between spawning
	 * @param count how many spawns in total
	 * @param position grid number, which is turned into screen coordinate
	 * @param spawn_type is the object type to spawn. Currently worm or ant
	 */
	
	public void startSpawning(final Level level){
		this.level = level;
		final SpawnPoint spawn = this;
		System.out.println("Started to spawn");
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					while(numCreated < spawnCount){
						numCreated++;
						//Wait for first spawn
						Thread.sleep(initialSleep);
						//Set initial sleep to 0 to prevent further delay
						initialSleep = 0;
						//Add newly created worm to main gameobject list
						if(spawnType.equalsIgnoreCase("worm")){
							level.addNewObject(RuntimeObjectFactory.getObjectFromPool("worm"));
						}else if(spawnType.equalsIgnoreCase("ant"))
							level.addNewObject(RuntimeObjectFactory.getObjectFromPool("worm"));
						//Sleep for the actual spawn interval
						Thread.sleep(interval);
					}
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}).start();
	}
	


	public void setLevel(Level level) {
		this.level = level;
	}

	public void setSpawnType(String spawnType) {
		this.spawnType = spawnType;
	}

	public void setInitialSleep(String initialSleep) {
		this.initialSleep = Integer.parseInt(initialSleep)*1000;
	}

	public void setInterval(String interval) {
		this.interval =Integer.parseInt(interval)*1000;
	}

	public void setSpawnCount(String spawncount) {
		this.spawnCount = Integer.parseInt(spawncount);
		if(spawnType.equalsIgnoreCase("worm"))
			for( int i = 0 ; i < this.spawnCount ; i++)
				RuntimeObjectFactory.addToObjectPool("worm",new Worm(this));
	}
	
	public void setSpawnVelocity(String vel){
		this.spawnVelocity = vel;
	}
	public String getSpawnVelocity(){
		return spawnVelocity;
	}



}
