package com.me.tamer.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * @author Kesyttäjät
 * This class is the superclass for statically drawn objects
 * This class only has a static Sprite object with attached texture on it.
 *
 */
public class StaticRenderer implements Renderer {

	private Texture texture;
	private Sprite sprite;
	
	public StaticRenderer(String objectName){
		loadGraphics(objectName);
	}
	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void loadGraphics(String objectName) {
		texture = new Texture(Gdx.files.internal("data/tamer.png"));
		sprite 	= new Sprite(texture);
		sprite.setSize(256, 256);
		sprite.setPosition(0,0);
		
	}

}
