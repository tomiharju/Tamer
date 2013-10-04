package com.me.tamer.gameobjects.renders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.utils.IsoHelper;

/**
 * @author Kesyttäjät
 * This class is the superclass for statically drawn objects
 * This class only has a static Sprite object with attached texture on it.
 *
 */
public class StaticRenderer implements Renderer {
	
	private Sprite sprite;
	private String type;
	public StaticRenderer(){
	
	}
	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	@Override
	public void loadGraphics(String graphicsName) {
		sprite 	= new Sprite(new Texture(Gdx.files.internal("data/graphics/"+graphicsName+".png")));
		if(sprite == null)
			throw new IllegalArgumentException("Could not load sprite!");
	}
	@Override
	public void setSize(float w, float h) {
		sprite.setSize(w, h);
		
	}
	@Override
	public void setPosition(Vector2 pos) {
		sprite.setPosition(pos.x - sprite.getWidth(), pos.y - 0.5f );
		
	}
	
	@Override
	public void setOrientation(float angle) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setRenderType(String type) {
		this.type = type;
		
	}
	

}
