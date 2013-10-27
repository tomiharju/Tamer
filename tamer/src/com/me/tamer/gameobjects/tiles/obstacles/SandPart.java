package com.me.tamer.gameobjects.tiles.obstacles;

import com.me.tamer.gameobjects.Environment;

import com.badlogic.gdx.math.Vector2;

import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;

public class SandPart extends StaticObject{
	
	private boolean isEntered = false;
	
	public void setup(Environment environment){
		environment.addNewObject(this);
		setZindex(1);
	}
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setSize(new Vector2(1,0.5f));
		this.renderType = graphics;
		
	}

}
