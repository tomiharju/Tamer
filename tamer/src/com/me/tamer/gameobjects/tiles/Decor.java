package com.me.tamer.gameobjects.tiles;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.services.TextureManager.TamerTexture;
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
//		Renderer render = RenderPool.addRendererToPool("static",graphics);
//		render.loadGraphics(graphics);
//		setSize(getSize());
//		render.setSize(getSize());
//		setRenderType(graphics);
		
		//Temporary solution because the one above does not work
		graphics = graphics.split("\\.")[0];
		
		Renderer render = RenderPool.addRendererToPool("animated", graphics);
		render.loadGraphics(graphics, 1, 1);
		setSize(getSize());
		render.setSize(getSize());
		setRenderType(graphics);
	}



	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getDebug() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void wakeUp(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGraphics(TamerTexture tex) {
		// TODO Auto-generated method stub
		
	}

	
	

	
	
}
