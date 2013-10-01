package com.me.tamer.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me.tamer.gameobjects.superclasses.DynamicObject;

public class GryphonScream extends DynamicObject {
	private final float SCREAM_AREA_WIDTH = 4.0f;
	private final float SCREAM_AREA_DIST = 4.0f;
	private float[] screamArea = {SCREAM_AREA_WIDTH, SCREAM_AREA_DIST};
	
	//Debug
	private ShapeRenderer shapeRndr;
	
	public GryphonScream(){
		shapeRndr = new ShapeRenderer();
	}
	
	@Override
	public void draw(SpriteBatch batch){
		float x = 0;
		float y = 0;
		float width = 10.0f;
		float height = 10.0f;
		float radius = 5.0f;
		
	}
	
	@Override 
	public void debugDraw(){
		/*
		shapeRndr.begin(ShapeType.Line);
		shapeRndr.setColor(1, 1, 1, 1);
		shapeRndr.line(200, 200, 300, 300);

		shapeRndr.end();
		*/
	}
	
	
	@Override
	public void update(float dt) {
	}
	
	public float[] getCurrentScreamArea(){
		return screamArea;
	}
}
