package com.me.tamer.utils;

import java.util.HashMap;
import java.util.LinkedList;

import com.me.tamer.gameobjects.Level;
import com.me.tamer.gameobjects.superclasses.GameObject;

public class RuntimeObjectFactory {

	public static HashMap<String,LinkedList<GameObject>> objectPool = new HashMap<String,LinkedList<GameObject>>();
	public static Level level = null;
	public static void addToObjectPool(String type, GameObject obj){
		obj.setup();
		if(objectPool.containsKey(type))
			objectPool.get(type).add(obj);
		else{
			objectPool.put(type,new LinkedList<GameObject>());
			objectPool.get(type).add(obj);
		}
		
	}
	/**
	 * Call this method when ever you want to put certain type of gameobject into play
	 * Adds this new object to active gameobject list at level
	 * @param object type ( "spear", "Tree1" ) 
	 * 
	 */
	public static synchronized GameObject getObjectFromPool(String type){
	
		LinkedList<GameObject> pool = objectPool.get(type);
		if( pool != null && !pool.isEmpty()){
			GameObject object = pool.pop();
			level.addNewObject(object);
			return object;
			
		}
		else{
			System.err.println("No object labeled "+type+ " found in pool!");
			return null;
		}
	}
	
	public static void createLinkToLevel(Level lvl){
		level = lvl;
	}
	
}
