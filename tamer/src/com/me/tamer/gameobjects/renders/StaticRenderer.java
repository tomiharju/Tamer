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
	//Whose renderer this is
	private GameObject target = null;
	
	private Sprite sprite;
	
	public StaticRenderer(){
	
	}
	@Override
	public void draw(SpriteBatch batch) {
		//Translate "mathematical position" to on-screen position ( isometric )
		Vector2 position = IsoHelper.twoDToIso(target.getPosition());
		setPosition(position);
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
		sprite.setPosition(pos.x - sprite.getWidth()/2, pos.y - 0.5f );
		
	}
	@Override
	public void setTarget(GameObject obj) {
		this.target = obj;
		
	}
	

}
