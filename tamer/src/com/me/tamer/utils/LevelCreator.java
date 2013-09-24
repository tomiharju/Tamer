package com.me.tamer.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.me.tamer.core.Environment;
import com.me.tamer.gameobjects.Level;
import com.me.tamer.gameobjects.renders.Renderer.RenderType;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.SpawnPoint;

/**
 * @author tharju
 * Utility class that handles the environment creation
 * Is responsible for creating gameobjects via GameObjectFactory
 * Reads level data from level[x].properties
 */
public class LevelCreator {
	
	
	

	/**
	 * @param level_number self explanatory
	 * @param env link to environment object, needed to be able to add objects to environments gameobject list
	 * EnvironmentCreator.create reads an xml file from tamer-android/assets/data/levels/level+lvlnumber.xml
	 * Creates a hashtable to hold configurations for each object read from xml file
	 * Calls GameObjectFactory.createGameObject with desired object type, and read configuration map.
	 */
	public static Level create(int level_number){
		
		Level level = new Level();
		
		XmlReader reader = new XmlReader();
		try {
			FileHandle file = Gdx.files.internal("data/levels/level"+level_number+".xml");
			Element root = reader.parse(file);
			
			Array<Element> objects = root.getChildrenByName("GameObject");
			
			for(Element gameobject : objects){
				//First child is always GameObject
				//Create new LinkedHashMap ( it keeps input order ) to hold all values for this gameobject
				String objectType = gameobject.getAttribute("type");
				
				LinkedHashMap<String,String> config = new LinkedHashMap<String,String>();
				Array<Element> datavalues = gameobject.getChildrenByName("data");
				for(Element data : datavalues){
					config.put(data.getAttribute("type"),data.getText());
				}
		
				level.addObject(GameObjectFactory.createGameObject(objectType,config));
			
				
			
			}
			
			//Once xml file is completely read, and all objects are added, return new fresh level.
			//Setup objects adds all obstacles to specific obstacle list, used later for sandpit resolution etc.
			level.setupObjects();
			return level;
		  
		  
		  
		} catch (Exception e) {
		 e.printStackTrace();
		 return null;
		
		}
		
	
	}

}
