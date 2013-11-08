package com.me.tamer.gameobjects.renders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class EffectRenderer implements Renderer{

	private Animation effectAnimation;
	private Texture spriteSheet;
	private TextureRegion[] effectFrames;
	private TextureRegion currentFrame;
	private float stateTime;
	private Vector2 size = new Vector2();
	private Vector2 pos = new Vector2();
	
	
	
	@Override
	public void draw(SpriteBatch batch) {
		
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = effectAnimation.getKeyFrame(stateTime,false);
			batch.draw(currentFrame,pos.x - size.x / 2, pos.y - size.y / 2,size.x, size.y);
	}

	@Override
	public void loadGraphics(String objectName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadGraphics(String animName, int FRAME_COLS, int FRAME_ROWS) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadEffect(String animName, int FRAME_COLS, int FRAME_ROWS) {
		 spriteSheet = new Texture(Gdx.files.internal("data/graphics/animations/"+animName+".png"));
		 TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 
					FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);    
		effectFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS]; 
        int index = 0;
         for (int i = 0; i < FRAME_ROWS; i++) {
                 for (int j = 0; j < FRAME_COLS; j++) {
                         effectFrames[index++] = tmp[i][j];

                 }
         }
        effectAnimation = new Animation(0.05f, effectFrames);
		
	}
	
	public boolean effectFinished(){
		return effectAnimation.isAnimationFinished(stateTime);
	}
	public void resetEffect(){
		stateTime = 0;
	}

	@Override
	public void setSize(float w, float h) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAngle(float angle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBounds(float x, float y, float width, float height) {
		// TODO Auto-generated method stub
		
	}

}
