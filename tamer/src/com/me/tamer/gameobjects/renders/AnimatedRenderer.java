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
	private ArrayList<Animation> animations;
	private int currentAnimation = 0;
	
	private Texture spriteSheet;
	private TextureRegion[][] frames;
	
	private TextureRegion currentFrame;
	private float stateTime;
	private float animationDuration = 3;
	private Vector2 size = new Vector2();
	private Vector2 pos = new Vector2();
	private float angle;
	
	public AnimatedRenderer(){
		animations = new ArrayList<Animation>();
	}

	@Override
	public void draw(SpriteBatch batch) {	
		stateTime += Gdx.graphics.getDeltaTime();
		
		if (!animations.isEmpty()){
			currentFrame = animations.get(currentAnimation).getKeyFrame(stateTime,true);
			batch.draw(currentFrame,pos.x - size.x / 2,pos.y - size.y /2, size.x, size.y);
			//batch.draw( currentFrame, pos.x - size.x / 2, pos.y - size.y /2, 0, 0, size.x, size.y, 1, 1, angle);
		}
	}

	@Override
	public void loadGraphics(String graphicsName) {
		
		sprite 	= new Sprite(new Texture(Gdx.files.internal("data/graphics/"+graphicsName+".png")));
		if(sprite == null)
			throw new IllegalArgumentException("Could not load sprite!");
		
		
		animations.add(new Animation(animationDuration,sprite));
	}
	
	@Override
	public void loadGraphics( String animName,int FRAME_COLS,int FRAME_ROWS ){
		//loadGraphics from spritesheet
		spriteSheet = new Texture(Gdx.files.internal("data/graphics/animations/"+animName+".png"));
		frames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 
				FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);          
		
		for (int i = 0; i < FRAME_ROWS; i++) {
			animations.add(new Animation(animationDuration,frames[i]));
		}

		stateTime = 0f;
	}
	
	public void setAnimSpeed(float speed){
		animationDuration = speed;
	}
	
	@Override
	public void setSize(float w, float h) {
		this.size.set(w,h);
	}
	@Override
	public void setSize(Vector2 size) {
		this.size.set(size);
		
	}
	@Override
	public void setPosition(Vector2 pos) {
		this.pos.set(pos);	
	}

	

	@Override
	public void setOrientation(int orientation) {
		this.currentAnimation = orientation;
	}
	
	public void setAngle(float a){
		this.angle = a;
	}

	@Override
	public void setBounds(float x, float y, float width, float height) {
		// TODO Auto-generated method stub
		
	}

	
}
