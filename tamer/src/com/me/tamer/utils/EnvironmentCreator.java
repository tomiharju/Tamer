package com.me.tamer.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.me.tamer.Environment;
import com.me.tamer.gameobjects.GameObject;
import com.me.tamer.gameobjects.Renderer.RenderType;
import com.me.tamer.gameobjects.SpawnPoint;

/**
 * @author tharju
 * Utility class that handles the environment creation
 * Is responsible for creating gameobjects via GameObjectFactory
 * Reads level data from level[x].properties
 */
public class EnvironmentCreator {
	
	
	

	public static void create(int level_number,Environment env){
		Properties properties = new Properties();
		ArrayList<GameObject> objects = new ArrayList<GameObject>();
		try {
			FileHandle file  = Gdx.files.internal("data/levels/level"+level_number+".properties");
			properties.load(file.read());
			int num_worms = Integer.parseInt(properties.getProperty("num_worms"));
				//Example how to create a worm spawnpoint
			//SpawnPoint(long init_sleep,long interval,int count,int position,String spawn_type)
			SpawnPoint spawn = new SpawnPoint(5,10,2,25,"Worm");
			spawn.addToEnvironment(env);
			spawn.startSpawning();
		
			
		  
		  
		  
		  
		  
		  
		  
		} catch (Exception e) {
		  System.out.println(e.getMessage());
		
		}
		
	
	}

}
