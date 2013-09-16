package com.me.tamer.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.me.tamer.gameobjects.GameObject;
import com.me.tamer.gameobjects.Renderer.RenderType;

/**
 * @author tharju
 * Utility class that handles the environment creation
 * Is responsible for creating gameobjects via GameObjectFactory
 * Reads level data from level[x].properties
 */
public class EnvironmentCreator {
	
	
	

	public static ArrayList<GameObject> create(int level_number){
		Properties properties = new Properties();
		ArrayList<GameObject> objects = new ArrayList<GameObject>();
		try {
			FileHandle file  = Gdx.files.internal("data/levels/level"+level_number+".properties");
			properties.load(file.read());
			int num_worms = Integer.parseInt(properties.getProperty("num_worms"));
			int num_quicksand = Integer.parseInt(properties.getProperty("num_quicksand"));
			
			//Create worms
			for(int i = 0 ; i < num_worms ; i++){
				
				objects.add(GameObjectFactory.createGameObject("com.me.tamer.gameobjects.Worm", RenderType.ANIMATED));
			}
		
			//Return fresh new objects to be added into main gameobject list.
			return objects;
		  
		  
		  
		  
		  
		  
		  
		} catch (Exception e) {
		  System.out.println(e.getMessage());
		  return null;
		}
		
	
	}

}
