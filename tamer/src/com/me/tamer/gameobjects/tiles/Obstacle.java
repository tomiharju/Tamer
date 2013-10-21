package com.me.tamer.gameobjects.tiles;


import com.badlogic.gdx.Gdx;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.physics.RigidBody;

public class Obstacle extends StaticObject {
	
	
	public void setup(Environment level){
		level.addNewObject(this);
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
		float ASPECT_RATIO = (float)Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
		System.out.println("Aspect ratio is " + ASPECT_RATIO);
		size.set(size.x/40 , (size.y/40));
		
		setSize(size.x+":"+size.y);
		this.renderType = graphics;
	}
	
}
