package com.me.tamer.utils;

import com.me.tamer.gameobjects.AnimatedRenderer;
import com.me.tamer.gameobjects.Renderer;
import com.me.tamer.gameobjects.Renderer.RenderType;
import com.me.tamer.gameobjects.StaticRenderer;

public class RendererFactory {
	
	public static Renderer createRenderer(RenderType type,String objectName){
		switch(type){
		case ANIMATED:
			return new AnimatedRenderer(objectName);
		case STATIC:
			return new StaticRenderer(objectName);
		default:
			return null;
		
		}
	}

}
