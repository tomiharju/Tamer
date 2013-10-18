package com.me.tamer.gameobjects.tiles;

import com.me.tamer.gameobjects.Level;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;

public class SandPart extends StaticObject{
	
	
	public void setup(Level level){
		level.addNewObject(this);
		level.getObstacles().add(this);
	}
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("animated",graphics);
		render.loadGraphics(graphics);
		setSize("1:0.5");
		this.renderType = graphics;
		
	}
}
