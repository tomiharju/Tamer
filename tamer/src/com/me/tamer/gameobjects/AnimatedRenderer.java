package com.me.tamer.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Kesyttäjät
 * This class is the superclass for all objects that needs to be animated
 * This class has all the attributes needed for animation
 *
 */
public class AnimatedRenderer implements Renderer {

	private Animation animation;
	private Texture spriteSheet;
	private TextureRegion[] frames;
	private TextureRegion currentFrame;
	private boolean animate;
	private float stateTime;
	private Vector2 animPos;
	private Sprite baseGraphics;
	
	
	public AnimatedRenderer(String objectName){
		loadGraphics(objectName);
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void loadGraphics(String objectName) {
		// TODO Auto-generated method stub
		
	}
}
