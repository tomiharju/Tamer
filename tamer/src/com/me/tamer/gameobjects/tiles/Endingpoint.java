package com.me.tamer.gameobjects.tiles;

import com.badlogic.gdx.Gdx;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.physics.RigidBody;

public class Endingpoint extends StaticObject{
	public void setup(Environment level){
		level.addNewObject(this);
		setZindex(0);

	}

	@Override
	public RigidBody getRigidBody(){
		return body;
	}
	public void setPixelsX(String pixels){
		float x = Float.parseFloat(pixels);
		size.set(x,size.y);
	}
	public void setPixelsY(String pixels){
		float y = Float.parseFloat(pixels);
		size.set(size.x,y);
	}
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setSize("1:0.5");
		this.renderType = graphics;
	}
}
