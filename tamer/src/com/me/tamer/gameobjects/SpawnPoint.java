package com.me.tamer.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.Environment;
import com.me.tamer.gameobjects.Renderer.RenderType;
import com.me.tamer.utils.GameObjectFactory;

/**
 * @author tharju
 * Spawn point object for worms.
 * Knows how many worms to spawn, and the time interval to spawn them
 */
public class SpawnPoint extends StaticObject{
	private Environment env;
	private String spawn_type;
	private int initial_sleep;
	private int interval;
	private int worm_count;
	private int num_created = 0;
	private Vector2 position;

	/**
	 * @param init_sleep how long till the first spawn
	 * @param interval interval between spawning
	 * @param count how many spawns in total
	 * @param position grid number, which is turned into screen coordinate
	 * @param spawn_type is the object type to spawn. Currently worm or ant
	 */
	public SpawnPoint(int init_sleep,int interval,int count,int position,String spawn_type){
		this.spawn_type = spawn_type;
		this.initial_sleep = init_sleep;
		this.interval = interval;
		this.worm_count = count;
		//TODO: get position from some util isometric helper class
		this.position = new Vector2();
	}
	public void addToEnvironment(Environment env){
		this.env = env;
	}
	public void startSpawning(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					while(num_created < worm_count){
						num_created++;
						//Wait for first spawn
						Thread.sleep(initial_sleep);
						//Set initial sleep to 0 to prevent further delay
						initial_sleep = 0;
						//Add newly created worm to main gameobject list
						env.addGameObject(GameObjectFactory.createGameObject("com.me.tamer.gameobjects."+spawn_type, RenderType.ANIMATED));
						//Sleep for the actual spawn interval
						Thread.sleep(interval);
					}
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
	}

}
