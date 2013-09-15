package com.me.tamer.gameobjects;

public interface Renderer {
	public enum RendererType{
		STATIC,ANIMATED
	}

	public void draw();
	public void loadGraphics(String objectName);
}
