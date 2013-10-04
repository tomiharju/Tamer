package com.me.tamer.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public interface UiElement {
	
	public void draw(SpriteBatch batch);
	public void update(float dt);
	public void relocate();
	public boolean handleInput(Vector2 input);
	public void touchUp();
	public void touchDown();
	public boolean isTouched(Vector2 input);

}
