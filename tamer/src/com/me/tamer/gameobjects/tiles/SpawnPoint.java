package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.me.tamer.utils.EventPool;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.RuntimeObjectFactory;
import com.me.tamer.utils.tEvent;

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
	private boolean isTamerSpawn = false;
	//IMPORTANT: spawn number is used to distinguish spawns from each other.
	private int spawnId = 0;
	private final int TAMER_SPAWN_TIME = 3;
	private Hud hud;
		
	//EXPERIMENTAL STUFF
	private Tamer tamer;
	private Environment environment;
	
	public SpawnPoint(){
		hud = Hud.instance();
	}
	
	public void setup(Environment environment){
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: started spawning");
		environment.addNewObject(this);
		this.environment = environment;
		EventPool.addEvent(new tEvent(this,"spawnTamer",TAMER_SPAWN_TIME,1));
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
		this.sleepTime = Integer.parseInt(time);
	}
	
	public void setInitialSleepTime(String time){
		this.initialSleep = Integer.parseInt(time);
	}
	
	public void addWorm(Worm worm){
		//update hud when worm is added
		hud.updateLabel(Hud.LABEL_REMAINING,1);
		
		spawnType = "worm";
		RuntimeObjectFactory.addToObjectPool("worm"+spawnId,(GameObject)worm);
		worm.setPosition(getPosition());
		worm.setVelocity(spawnVelocity);
	}
	public void addAnt(AntOrc ant){
		spawnType = "ant";
		RuntimeObjectFactory.addToObjectPool("ant"+spawnId,(GameObject)ant);
		ant.setPosition(getCenterPosition());
		ant.setVelocity(spawnVelocity);
	}


	
	
	/**
	 *	After initial sleep, this method is called every "sleepTime" interval 
	 */
	public void spawnCreature(){
		if(spawnType.equalsIgnoreCase("worm")){
			RuntimeObjectFactory.getObjectFromPool("worm"+spawnId);
		}else if(spawnType.equalsIgnoreCase("ant"))
			RuntimeObjectFactory.getObjectFromPool("ant"+spawnId);
	}
	/**
	 * This method is called after "initialSleep"
	 */
	public void spawnFirstCreature(){
		if(spawnType.equalsIgnoreCase("worm")){
			RuntimeObjectFactory.getObjectFromPool("worm"+spawnId);
		}else if(spawnType.equalsIgnoreCase("ant"))
			RuntimeObjectFactory.getObjectFromPool("ant"+spawnId);
		
		//Add new event into pool which will spawn the rest of the worms ( -1 because this method already spawned one )
		EventPool.addEvent(new tEvent(this,"spawnCreature",sleepTime,spawnCount-1));
	}
	public void spawnTamer(){
		if(isTamerSpawn){
			RuntimeObjectFactory.getObjectFromPool("tamer");
			Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: set state to TAMER_ENTER");
		}
		//Once tamer has spawned, add new event which will sleep the "initial sleep" time
		EventPool.addEvent(new tEvent(this,"spawnFirstCreature",initialSleep,1));
	}
	
	
	public void setSpawnId(String number){
		this.spawnId = Integer.parseInt(number);
	}

	public void setInitialSleep(String initialSleep) {
		this.initialSleep = Integer.parseInt(initialSleep);
	}

	public void setTamerSpawn(String flag){
		int value = Integer.parseInt(flag);
		if(value == 1){
			isTamerSpawn = true;
			tamer = new Tamer();
			
			tamer.getShadow().setPosition(getCenterPosition());
			tamer.setSpawnDirection(spawnVelocity);
			tamer.setHeading(spawnVelocity.tmp().nor());

			RuntimeObjectFactory.addToObjectPool("tamer",(GameObject)tamer);
		}
	}

	public Vector2 getSpawnVelocity(){
		return spawnVelocity;
	}

	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getDebug() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void wakeUp(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(Environment level) {
		// TODO Auto-generated method stub
		
	}
}
