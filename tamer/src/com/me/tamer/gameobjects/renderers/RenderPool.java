package com.me.tamer.gameobjects.renderers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.me.tamer.services.TextureManager.TamerTexture;
import com.me.tamer.utils.RendererFactory;

public class RenderPool {

	
	static HashMap<String, Renderer> renderpool = new HashMap<String,Renderer>();
	
	public static TextureAtlas atlas;
	
	public static HashMap<String,Renderer> getRenderpool(){
		return renderpool;
		
	}
	
	public static Renderer addRendererToPool(String rendertype, String graphics){
		if(!renderpool.containsKey(graphics)){
			Renderer renderer = RendererFactory.createRenderer(rendertype);
			renderpool.put(graphics,renderer);
		}

		return renderpool.get(graphics);
	}
	
	public static void removeRenderer(String renderer){
		renderpool.remove(renderer);
	}
	
	public static Renderer getRenderer(String type){
		return renderpool.get(type);
	}
}
