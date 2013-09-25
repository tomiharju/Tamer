package com.me.tamer.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class StaticObject implements GameObject{
	private Vector2 position;
	private Renderer renderer;
	private boolean isCarbage = false;
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		renderer.draw(batch);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
		
	}

	@Override
	public void markAsCarbage() {
		isCarbage = true;
		
	}

	@Override
	public boolean isCarbage() {
		return isCarbage;
	}

}
