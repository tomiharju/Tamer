package com.me.tamer.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.superclasses.GameObject;



public class GameObjectFactory {
	
	
	
	public static GameObject createGameObject(String className, LinkedHashMap<String,String> propertyConfig){
		try {
			
			Class<?> objectClass = Class.forName(className);
			Constructor<?> constructor = objectClass.getConstructor();
			GameObject object = (GameObject) constructor.newInstance(new Object[]{});
			
			Gdx.app.debug(TamerGame.LOG, " :: Starting to create new gameobject {"+object.getClass().getSimpleName()+"}");
			//--SET PROPERTIES--//
			Iterator<Entry<String, String>> propertyIt = propertyConfig.entrySet().iterator();
			while (propertyIt.hasNext()) {
				Entry<String, String> entry = propertyIt.next();
			  try{
				  Method setter = objectClass.getMethod("set"+entry.getKey(),String.class);
				  Gdx.app.debug(TamerGame.LOG, " :: Setting value... ["+entry.getKey()+"] -> " + entry.getValue());
				  setter.invoke(object, entry.getValue());
			  }catch(NoSuchMethodException e){
				  Gdx.app.debug(TamerGame.LOG, " :: Trying to set invalid object property ["+entry.getKey()+"] " + e.getMessage());
			  }catch(InvocationTargetException i){
				  Gdx.app.debug(TamerGame.LOG, " :: set" + entry.getKey() +" Failed to run succesfully\n" + i.getMessage());
			  }
			 
			}
			Gdx.app.debug(TamerGame.LOG, " :: GameObject created {" + object.getClass().getSimpleName() + "} In memory as "+object.toString()+" \n");
		
			return object;
			
			
			
			
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException c){
			System.err.println("Unknown object type "+className+"\n");
		} catch(Exception e){
			e.printStackTrace();
			
		}
		//If error
		return null;
	
		
		
		
	}

}
