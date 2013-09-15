package com.me.tamer.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Renderer {
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
}
