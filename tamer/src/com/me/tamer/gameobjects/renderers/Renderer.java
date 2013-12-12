package com.me.tamer.gameobjects.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.services.TextureManager.TamerAnimations;

public interface Renderer {
	/**
	 * Different possible render types.
	 * STATIC is for non-animated sprites.
	 * ANIMATED is for animated sprites.
	 */
	public enum RenderType{
		STATIC,ANIMATED, UI, EFFECT
	}

	public void draw(SpriteBatch batch);
	/**
	 * @param objectName
	 * Loads the proper graphics file from assets using the objectName as parameter
	 * Creates texture and sprite objects for this object.
	 */
	public void loadGraphics(TamerAnimations animName, int FRAME_COLS, int FRAME_ROWS);
	public void loadGraphics(String objectName);
	public void loadGraphics(String animName,int FRAME_COLS,int FRAME_ROWS);
	
	public void setSize(float w, float h);
	public void setSize(Vector2 size);
	public void setColor(float r, float g, float b, float a);
	public void setPosition(Vector2 pos);
	
	public Color getColor();
	
}
