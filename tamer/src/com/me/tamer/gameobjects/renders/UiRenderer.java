package com.me.tamer.gameobjects.renders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.GameObject;

public class UiRenderer implements Renderer {

	private String type ;
	
	private Sprite sprite;
	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
		
	}
	
	@Override
	public void loadGraphics(String graphicsName) {
		sprite 	= new Sprite(new Texture(Gdx.files.internal("data/graphics/"+graphicsName+".png")));
	}
	@Override
	public void setSize(float w, float h) {
		sprite.setSize(w, h);
		
	}
	@Override
	public void setPosition(Vector2 pos) {
		sprite.setPosition(pos.x - sprite.getWidth()/2, pos.y - sprite.getHeight() / 2 );
		
	}
	public void setPosition2(Vector2 pos){
		sprite.setPosition(pos.x - sprite.getWidth()/2, pos.y - 0.5f );

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
