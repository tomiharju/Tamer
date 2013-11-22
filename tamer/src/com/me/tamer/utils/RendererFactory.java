package com.me.tamer.utils;

import com.me.tamer.gameobjects.renderers.AnimatedRenderer;
import com.me.tamer.gameobjects.renderers.EffectRenderer;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.renderers.StaticRenderer;

public class RendererFactory {
	
	public static Renderer createRenderer(String type) throws IllegalArgumentException{
		if(type.equalsIgnoreCase("static"))
			return new StaticRenderer();
		else if(type.equalsIgnoreCase("animated"))
			return new AnimatedRenderer();
		else if(type.equalsIgnoreCase("effect")){
			return new EffectRenderer();
		}
		else
			throw new IllegalArgumentException("Rendertype unknown");
	}			

}
