package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.core.TamerStage;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.AntOrc;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.creatures.Worm;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.gameobjects.tamer.Tamer;
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
	private boolean isTamerSpawn = false;
	//IMPORTANT: spawn number is used to distinguish spawns from each other.
	private int spawnId = 0;
	
	//EXPERIMENTAL STUFF
	private ArrayList<Creature> creatures;
	
	public SpawnPoint(){
		creatures = new ArrayList<Creature>();
	}
	public void setup(Environment environment){
		environment.addNewObject(this);
		startSpawning();
		setZindex(1);
	}
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setSize(new Vector2(1,0.5f));
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
		
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: SPAWN ANGLE IS "+ spawnVelocity.toString());
		
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
		worm.setPosition(getCenterPosition());
		worm.setVelocity(spawnVelocity);
	}
	public void addAnt(AntOrc ant){
		spawnType = "ant";
		RuntimeObjectFactory.addToObjectPool("ant"+spawnId,(GameObject)ant);
		creatures.add(ant);
		ant.setPosition(getCenterPosition());
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
		Gdx.app.debug(TamerGame.LOG, this.getClass()
				.getSimpleName() + " :: Spawn number "+spawnId + " Started spawning");
		startSpawning();
	}
	public void startSpawning(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					if (TamerStage.gameState == TamerStage.GAME_RUNNING){
						Thread.sleep(initialSleep);
						if(isTamerSpawn){
							RuntimeObjectFactory.getObjectFromPool("tamer");
							Gdx.app.debug(TamerGame.LOG, this.getClass()
									.getSimpleName() + " :: Tamer entered");
						}
						while(numCreated < spawnCount){
							Thread.sleep(sleepTime);
							numCreated++;
							//Add newly created worm to main gameobject list
							if(spawnType.equalsIgnoreCase("worm")){
								Gdx.app.debug(TamerGame.LOG, this.getClass()
										.getSimpleName() + " :: Worm entered");
								RuntimeObjectFactory.getObjectFromPool("worm"+spawnId);
							}else if(spawnType.equalsIgnoreCase("ant"))
								RuntimeObjectFactory.getObjectFromPool("ant"+spawnId);
							//Sleep for the actual spawn interval
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}		
		}).start();
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
			isTamerSpawn = true;
			Tamer tamer = new Tamer();
			tamer.setPosition(getCenterPosition());
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
