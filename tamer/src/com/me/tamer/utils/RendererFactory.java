package com.me.tamer.utils;

import com.me.tamer.gameobjects.AnimatedRenderer;
import com.me.tamer.gameobjects.Renderer;
import com.me.tamer.gameobjects.Renderer.RenderType;
import com.me.tamer.gameobjects.StaticRenderer;

public class RendererFactory {
	
	public static Renderer createRenderer(String type) throws IllegalArgumentException{
		if(type.equalsIgnoreCase("static"))
			return new StaticRenderer();
		else if(type.equalsIgnoreCase("animated"))
			return new AnimatedRenderer();
		else
			throw new IllegalArgumentException("Rendertype unknown");
	}			

}
