package com.me.tamer.gameobjects.renders;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.GameObject;

/**
 * @author Kesyttäjät
 * This class is the superclass for all objects that needs to be animated
 * This class has all the attributes needed for animation
 *
 */

public class AnimatedRenderer implements Renderer {

	private Sprite sprite ;
	//private Animation animation;
	private Animation[] animations;
	private int currentAnimation = 0;
	
	private Texture spriteSheet;
	private TextureRegion[][] frames;
	
	private TextureRegion currentFrame;
	private boolean animate;
	private float stateTime;
	private float animSpeed = 0.025f;
	private Vector2 animPos;
	private Vector2 size;
	private String type;
	
	public AnimatedRenderer(){
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
		//TODO: ANIMATED DRAWING
		
		//stateTime += Gdx.graphics.getDeltaTime();
		//currentFrame = animations[currentAnimation].getKeyFrame(stateTime,true);
		//batch.draw(currentFrame,size.x,size.y);
		
	}



	@Override
	public void loadGraphics(String graphicsName) {
		sprite 	= new Sprite(new Texture(Gdx.files.internal("data/graphics/"+graphicsName+".png")));
		if(sprite == null)
			throw new IllegalArgumentException("Could not load sprite!");
	}
	public void loadAnimation(String animName,int FRAME_COLS,int FRAME_ROWS){
		spriteSheet = new Texture(Gdx.files.internal("data/graphics/animations/"+animName+".png"));
		frames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 
				FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);          
		
		/*
		frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                    frames[index++] = tmp[i][j];
            }
		}
		 */
		
		animations = new Animation[FRAME_ROWS];
		for (int i = 0; i < FRAME_ROWS; i++) {
			animations[i] = new Animation(animSpeed,frames[i]);
		}

		stateTime = 0f;
	}
	public void setAnimSpeed(float speed){
		animSpeed = speed;
	}
	@Override
	public void setSize(float w, float h) {
		sprite.setSize(w, h);
		
	}

	@Override
	public void setPosition(Vector2 pos) {
		sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y - sprite.getHeight() /2  );
		
	}

	@Override
	public void setRenderType(String type) {
		this.type = type;
		
	}

	@Override
	public void setOrientation(int orientation) {
		this.currentAnimation = 0; //orientation;
	}
}
