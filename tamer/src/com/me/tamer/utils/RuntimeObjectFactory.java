package com.me.tamer.utils;

import java.util.HashMap;
import java.util.LinkedList;
import com.me.tamer.gameobjects.superclasses.GameObject;

public class RuntimeObjectFactory {

	public static HashMap<String,LinkedList<GameObject>> objectPool = new HashMap<String,LinkedList<GameObject>>();
	
	public static void addToObjectPool(String type, GameObject obj){
		obj.setup();
		if(objectPool.containsKey(type))
			objectPool.get(type).add(obj);
		else{
			objectPool.put(type,new LinkedList<GameObject>());
			objectPool.get(type).add(obj);
		}
		
	}
	public static synchronized GameObject getObjectFromPool(String type){
	
		LinkedList<GameObject> pool = objectPool.get(type);
		if( pool != null && !pool.isEmpty()){
			return pool.pop();
			
		}
		else
			throw new IllegalArgumentException("Trying to get invalid type object from pool!");
			
	}
	
	
}
