package com.me.tamer.gameobjects.tiles.obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.services.TextureManager.TamerTexture;
import com.me.tamer.utils.Helper;

public class SandPart extends StaticObject{

	//Used to separate animation phase from another
	//15 is the number of frames in bubble animation
	
	private float animState = 0;

	public void setup(Environment environment){
		environment.addNewObject(this);
		setZindex(1);
		setAnimState(20);
	}
	public void setGraphics(String graphics){
//		Renderer render = RenderPool.addRendererToPool("static",graphics);
//		render.loadGraphics(graphics);
//		setSize(Helper.TILESIZE.x, Helper.TILESIZE.y);
//		render.setSize(getSize());
//		this.setRenderType(graphics);
		
		//Temporary solution because the one above does not work
		graphics = graphics.split("\\.")[0];
		
		Renderer render = RenderPool.addRendererToPool("animated", graphics);
		render.loadGraphics(graphics, 1, 1);
		setSize(Helper.TILESIZE.x,Helper.TILESIZE.y);
		render.setSize(getSize());
		setRenderType(graphics);
	}
	
	public float stepAnimState(){
		animState+= Gdx.graphics.getDeltaTime();
		return animState;
	}
	/**
	 * @param framecount
	 * Sets the number of frames used in the effect for this object.
	 * Setting animstate to a random frame between 0 and framecount
	 * causes the effectanimation to play at random state for each different object.
	 * Without this, all the animations would loop synchonously which looks ugly.
	 */
	public void setAnimState(int framecount){
		animState = (float) (Math.random()*framecount);
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
