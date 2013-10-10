package com.me.tamer.gameobjects.renders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.me.tamer.utils.RendererFactory;

public class RenderPool {

	
	static HashMap<String, Renderer> renderpool = new HashMap<String,Renderer>();
	
	
	public static HashMap<String,Renderer> getRenderpool(){
		return renderpool;
	}
	
	public static void addRendererToPool(String rendertype, String graphics){
		if(!renderpool.containsKey(graphics)){
			Renderer renderer = RendererFactory.createRenderer(rendertype);
			renderer.loadGraphics(graphics);
			renderpool.put(graphics,renderer);
		}
	}
	
	public static void removeRenderer(String renderer){
		renderpool.remove(renderer);
	}
	
	public static Renderer getRenderer(String type){
		return renderpool.get(type);
	}
}
