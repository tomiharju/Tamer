package com.me.tamer.gameobjects;

import com.badlogic.gdx.math.Vector2;

public class StaticObject implements GameObject{
	private Vector2 position;
	private Renderer renderer;
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw() {
		renderer.draw();
		// TODO Auto-generated method stub
		
	}

}
