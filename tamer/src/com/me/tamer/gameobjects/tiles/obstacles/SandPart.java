package com.me.tamer.gameobjects.tiles.obstacles;

import com.badlogic.gdx.Gdx;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.utils.Helper;

public class SandPart extends StaticObject{

	//Used to separate animation phase from another
	//15 is the number of frames in bubble animation
	
	private float animState = (float) (Math.random() * 15);
	
	public void setup(Environment environment){
		environment.addNewObject(this);
		setZindex(1);
	}
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setSize(Helper.TILESIZE);
		this.setRenderType(graphics);	
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
}
