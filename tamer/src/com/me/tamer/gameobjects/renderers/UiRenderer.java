package com.me.tamer.gameobjects.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.me.tamer.core.TamerStage;
import com.me.tamer.services.TextureManager.TamerAnimations;

/**
 * @author ville
 *
 */
public class UiRenderer implements Renderer {

	private String type;
	private AssetManager assetManager;
	
	public UiRenderer(){
		TamerStage stage = TamerStage.instance();
		assetManager = stage.getGame().getAssetManager();
	}
	
	private Sprite sprite;
	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
		
	}
	
	/* 
	 *Old stuff
	 */
	@Override
	public void loadGraphics(String graphicsName) {
		sprite = new Sprite( assetManager.get("data/graphics/sheetData", TextureAtlas.class).findRegion(graphicsName) );
		sprite.setColor(Color.WHITE);
	}
	
	
	
	@Override
	public void setSize(float w, float h) {
		sprite.setSize(w, h);
		
	}
	@Override
	public void setSize(Vector2 size) {
		sprite.setSize(size.x,size.y);
		
	}
	@Override
	public void setPosition(Vector2 pos) {
		sprite.setPosition(pos.x - sprite.getWidth()/2, pos.y - sprite.getHeight() / 2 );
		
	}
	public void setPosition2(Vector2 pos){
		sprite.setPosition(pos.x - sprite.getWidth()/2, pos.y - 0.5f );

	}
	@Override
	public void setOrientation(int orientation) {
		// TODO Auto-generated method stub
		
	}
	
	public void setColor(float r, float g, float b,float a){
		sprite.setColor(r, g, b,a);
	}
	public void resetColor(){
		sprite.setColor(0,0,0,0.1f);
	}

	@Override
	public void loadGraphics(String animName, int FRAME_COLS, int FRAME_ROWS) {
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

	@Override
	public void loadEffect(String animName, int FRAME_COLS, int FRAME_ROWS,
			boolean looping, float speed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadGraphics(TamerAnimations animName, int FRAME_COLS,
			int FRAME_ROWS) {
		// TODO Auto-generated method stub
		
	}


	
}
