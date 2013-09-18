package com.me.tamer.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Kesyttäjät
 * This class is the superclass for statically drawn objects
 * This class only has a static Sprite object with attached texture on it.
 *
 */
public class StaticRenderer implements Renderer {

	private Sprite sprite;
	
	public StaticRenderer(){
	
	}
	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
		
	}
	
	@Override
	public void loadGraphics(String graphicsName) {
		sprite 	= new Sprite(new Texture(Gdx.files.internal("data/graphics/"+graphicsName)));
		sprite.setPosition(4, 4);
		
	}
	@Override
	public void setSize(int w, int h) {
		this.sprite.setSize(w, h);
		
	}

}
