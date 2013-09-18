package com.me.tamer.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
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
		
		XmlReader reader = new XmlReader();
		try {
			String objectType = null;
			
			
			FileHandle file = Gdx.files.internal("data/levels/level"+level_number+".xml");
			Element root = reader.parse(file);
			System.out.println("Starting to read objects...");
			Array<Element> objects = root.getChildrenByName("GameObject");
		
			for(Element gameobject : objects){
				//First child is always GameObject
				//Create new HashTable to hold all values for this gameobject
				objectType = gameobject.getAttribute("type");
				Hashtable<String,String> config = new Hashtable<String,String>();
				Array<Element> datavalues = gameobject.getChildrenByName("data");
				for(Element data : datavalues){
					String type = data.getAttribute("type");
					config.put(type,data.getText());
				}
			 env.addGameObject(GameObjectFactory.createGameObject(objectType,config));
			
				
			
			}
			
			//Example how to create a worm spawnpoint
			//SpawnPoint(long init_sleep,long interval,int count,int position,String spawn_type)
			//SpawnPoint spawn = new SpawnPoint(5,10,2,25,"Worm");
			//spawn.addToEnvironment(env);
			//spawn.startSpawning();
		
			
		  
		  
		  
		  
		  
		  
		  
		} catch (Exception e) {
		 e.printStackTrace();
		
		}
		
	
	}

}
