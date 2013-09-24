package ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public interface UIElement {
	
	public void draw(SpriteBatch batch);
	public void update(float dt);
	public void relocate();
	public boolean handleInput(Vector3 input);
	public void touchUp();

}
