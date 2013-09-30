package com.me.tamer.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.me.tamer.gameobjects.superclasses.GameObject;



public class GameObjectFactory {
	
	
	
	public static GameObject createGameObject(String className,LinkedHashMap<String,String> configuration){
		try {
			
			Class<?> objectClass = Class.forName(className);
			Constructor<?> constructor = objectClass.getConstructor();
			GameObject object = (GameObject) constructor.newInstance(new Object[]{});
			
			Iterator<Entry<String, String>> it = configuration.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
			  try{
				  Method setter = objectClass.getMethod("set"+entry.getKey(),String.class);
				  System.out.println("Setting value... ["+entry.getKey()+"] -> " + entry.getValue());
				  setter.invoke(object, entry.getValue());
			  }catch(NoSuchMethodException e){
				  System.err.println("Trying to set invalid object property ["+entry.getKey()+"]");
			  }catch(InvocationTargetException i){
				  System.err.println("set" + entry.getKey() +" Failed to run succesfully\n" + i.getMessage());
			  }
			 
			}
			System.out.println("Object added: " + object.getClass().getSimpleName() + " In memory as "+object.toString());
			return object;
			
			
			
			
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException c){
			System.err.println("Unknown object type\n");
		} catch(Exception e){
			e.printStackTrace();
			
		}
		//If error
		return null;
	
		
		
		
	}

}
