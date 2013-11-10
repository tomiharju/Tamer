package com.me.tamer.gameobjects.tiles;

import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.utils.Helper;
/**
 * @author tomi Decor are all the non-interactive gameobjects in the level,
 *         props are trees, rocks, stumps and all the other objects which only
 *         affect with their rigidbodies and graphical aspect.
 */
public class Decor extends StaticObject{
	
	public void setup(Environment level){
		level.addNewObject(this);
	}

	public void setPixelsX(String pixels){
		float x = Float.parseFloat(pixels);
		setSize(x / Helper.TILE_WIDTH,getSize().y );
	}
	public void setPixelsY(String pixels){
		float y = Float.parseFloat(pixels);
		setSize(getSize().x ,y / Helper.TILE_WIDTH);
	}
	
	
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setSize(getSize());
		setRenderType(graphics);
	}

	
	

	
	
}
