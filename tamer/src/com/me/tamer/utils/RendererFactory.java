package com.me.tamer.utils;

import com.me.tamer.gameobjects.renders.AnimatedRenderer;
import com.me.tamer.gameobjects.renders.EffectRenderer;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.renders.StaticRenderer;
import com.me.tamer.gameobjects.renders.Renderer.RenderType;

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
