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
	
	public StaticRenderer(String objectName){
		loadGraphics(objectName.toLowerCase());
	}
	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
		
	}
	
	@Override
	public void loadGraphics(String objectName) {
		sprite 	= new Sprite(new Texture(Gdx.files.internal("data/"+objectName+".png")));
		sprite.setSize(50, 50);
		sprite.setPosition(5,5);
		
	}

}
