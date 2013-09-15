package com.me.tamer.utils;

import java.lang.reflect.Constructor;

import com.me.tamer.gameobjects.GameObject;
import com.me.tamer.gameobjects.Renderer;
import com.me.tamer.gameobjects.Renderer.RenderType;



public class GameObjectFactory {
	
	
	
	public static GameObject createGameObject(String className, RenderType rendType){
		try {
			Class<?> objectClass = Class.forName(className);
			Constructor<?> constructor = objectClass.getConstructor();
			GameObject object = (GameObject) constructor.newInstance(new Object[]{});
			Renderer renderer = RendererFactory.createRenderer(rendType,object.getClass().getSimpleName());
			object.setRenderer(renderer);
			return object;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	
		
		
		
	}

}
