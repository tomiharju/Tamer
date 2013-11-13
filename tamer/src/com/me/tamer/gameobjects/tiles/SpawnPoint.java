package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.Hud;
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
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.RuntimeObjectFactory;

/**
 * @author tharju
 * Spawn point object for worms.
 * Knows how many worms to spawn, and the time interval to spawn them
 */
public class SpawnPoint extends StaticObject{
	
	private final int TAMER_ENTER_DELAY = 2; 
	private String spawnType;
	private Vector2 spawnVelocity;
	private int initialSleep;

	private int sleepTime;
	private int spawnCount;
	private int numCreated = 0;
	private boolean isTamerSpawn = false;
	//IMPORTANT: spawn number is used to distinguish spawns from each other.
	private int spawnId = 0;
	private Thread spawn_thread = null;
	private Hud hud;
		
	//EXPERIMENTAL STUFF
	private ArrayList<Creature> creatures;
	private Tamer tamer;
	private Environment environment;
	
	public SpawnPoint(){
		creatures = new ArrayList<Creature>();
		hud = Hud.instance();
	}
	
	public void setup(Environment environment){
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: started spawning");
		environment.addNewObject(this);
		this.environment = environment;
		startSpawning();
		setZindex(1);
		
	}
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setSize(Helper.TILESIZE);
		this.setRenderType(graphics);
	}
	
	public void setSpawnCount(String count){
		this.spawnCount = Integer.parseInt(count);
	}
	
	public void setSpawnDirection(String vel){
		float angle = Float.parseFloat(vel);
		angle += 45;
		this.spawnVelocity = new Vector2(1,0);
		this.spawnVelocity.setAngle(angle);		
	}
	
	public void setSleepTime(String time){
		this.sleepTime = Integer.parseInt(time)*1000;
	}
	
	public void setInitialSleepTime(String time){
		this.initialSleep = Integer.parseInt(time)*1000;
	}
	
	public void addWorm(Worm worm){
		//update hud when worm is added
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: Updating label remaining");
		hud.updateLabel(Hud.LABEL_REMAINING,1);
		
		spawnType = "worm";
		RuntimeObjectFactory.addToObjectPool("worm"+spawnId,(GameObject)worm);
		creatures.add(worm);
		worm.setPosition(getPosition());
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

	public void startSpawning(){
		spawn_thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Thread.sleep(TAMER_ENTER_DELAY);
					if (TamerStage.gameState == TamerStage.GAME_RUNNING){
						if(isTamerSpawn){
							RuntimeObjectFactory.getObjectFromPool("tamer");
							Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: set state to TAMER_ENTER");
							environment.setState(Environment.TAMER_ENTER);
						}
					Thread.sleep(initialSleep);
					
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
					e.printStackTrace();
				}	
			}		
		});
		spawn_thread.start();
	}
	
	public void dispose(){
		spawn_thread.interrupt();
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
			tamer = new Tamer();
			
			tamer.setPosition(getCenterPosition());
			tamer.setSpawnDirection(spawnVelocity);
			tamer.setHeading(spawnVelocity.tmp().nor());

			RuntimeObjectFactory.addToObjectPool("tamer",(GameObject)tamer);
		}
	}

	public Vector2 getSpawnVelocity(){
		return spawnVelocity;
	}
}
