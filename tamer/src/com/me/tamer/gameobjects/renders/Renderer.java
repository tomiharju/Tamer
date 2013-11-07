package com.me.tamer.gameobjects.renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public interface Renderer {
	/**
	 * Different possible render types.
	 * STATIC is for non-animated sprites.
	 * ANIMATED is for animated sprites.
	 */
	public enum RenderType{
		STATIC,ANIMATED
	}

	public void draw(SpriteBatch batch);
	/**
	 * @param objectName
	 * Loads the proper graphics file from assets using the objectName as parameter
	 * Creates texture and sprite objects for this object.
	 */
	public void loadGraphics(String objectName);
	public void loadGraphics(String animName,int FRAME_COLS,int FRAME_ROWS);
	/**
	 * @param w width
	 * @param h	 height
	 * Sets the width and height of this sprite
	 */
	public void setSize(float w, float h);
	public void setSize(Vector2 size);
	public void setPosition(Vector2 pos);
	public void setOrientation(int orientation);
	public void setAngle(float angle);
	public void setBounds(float x, float y, float width, float height);
}
