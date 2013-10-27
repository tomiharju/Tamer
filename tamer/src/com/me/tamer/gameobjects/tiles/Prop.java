package com.me.tamer.gameobjects.tiles;


import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.physics.RigidBody;

/**
 * @author tomi
 * Props are all the non-interactive gameobjects in the level, props are trees, rocks, stumps and 
 * all the other objects which only affect with their rigidbodies and graphical aspect.
 */
public class Prop extends StaticObject {
	
	
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
		size.set(size.x/40 , (size.y/40));
		this.renderType = graphics;
	}
	
}
