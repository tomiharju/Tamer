package com.me.tamer.gameobjects.tiles;


import com.me.tamer.gameobjects.Level;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.physics.RigidBody;

public class Obstacle extends StaticObject {

	
	public void setup(Level level){
		level.addNewObject(this);
	}

	@Override
	public RigidBody getRigidBody(){
		return body;
	}

	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setSize("2:5");
		this.renderType = graphics;
	}
	
}
