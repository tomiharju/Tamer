package com.me.tamer.utils;

import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
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
			
			String objectType;
			Array<Element> properties;
			Array<Element> subObjects;
			LinkedHashMap<String,String> propertyConfig = new LinkedHashMap<String,String>();
			
			//Settings
			Element settings = root.getChildByName("Settings");
			properties = settings.getChildrenByName("property");
			
			for(Element property : properties){
				//Get the setter method, "set + type in xml"
				Method method = level.getClass().getMethod("set" +property.getAttribute("type"), String.class);
				method.invoke(level, (Object)property.getText());	
			}
			
			//GameObjects
		
			Array<Element> objects = root.getChildrenByName("GameObject");
			
			for(Element gameobject : objects){
				//First child is always GameObject
				//Create new LinkedHashMap ( it keeps input order ) to hold all values for this gameobject
				objectType = gameobject.getAttribute("type");
				//Read all properties ( non sub-object data )
				properties = gameobject.getChildrenByName("property");
				//Add found values to config map
				for(Element property : properties){
					propertyConfig.put(property.getAttribute("type"),property.getText());
				}
				//Create new gameobject
				GameObject objectToAdd = GameObjectFactory.createGameObject(objectType,propertyConfig);
				//If object has subobjects, create them aswell
				subObjects = gameobject.getChildrenByName("SubObject");
				
			
				level.addObject(objectToAdd);
				
				if(subObjects.size != 0){
					System.out.println("Creating Sub-objects for {"+objectToAdd.getClass().getSimpleName()+"}");
					addSubObjects(gameobject, objectToAdd, objectToAdd.getClass().getName());
				}
				
			}
			
			//Once xml file is completely read, and all objects are added, return new fresh level.
			//Setup objects adds all obstacles to specific obstacle list, used later for sandpit resolution etc.
			level.setupObjects();
			
			return level;	  
		  
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		 return null;
		
		}
		
	
	}
	
	public static void addSubObjects(Element currentXmlLevel, GameObject owner, String ownerClassName){
		String objectType;
		Array<Element> objects;
		Array<Element> subObjects;
		Array<Element> properties;
		LinkedHashMap<String,String> propertyConfig = new LinkedHashMap<String,String>();
	
		//SubObjects
		try{
			objects = currentXmlLevel.getChildrenByName("SubObject");
			if (objects != null){
				for(Element object : objects){
					
					objectType = object.getAttribute("type");
					
					properties = object.getChildrenByName("property");
					for(Element property : properties){
						propertyConfig.put(property.getAttribute("type"),property.getText());
					}
					
					GameObject objectToAdd = GameObjectFactory.createGameObject(objectType,propertyConfig);
					
					String objectClassName = object.getAttribute("type");
					String[] objectClassNameSplitted = objectClassName.split("\\.");
					String objectClassNameTrimmed = objectClassNameSplitted[(objectClassNameSplitted.length-1)];
						
					Class<?> ownerClass = Class.forName(ownerClassName);
					
					Method adder = ownerClass.getMethod("add"+objectClassNameTrimmed,objectToAdd.getClass());
					
					adder.invoke(owner, objectToAdd);
					
					subObjects = object.getChildrenByName("SubObject");
					if(subObjects != null){
						addSubObjects(object, objectToAdd, objectToAdd.getClass().getName());
					}
				}
			}
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
}
