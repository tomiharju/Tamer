package com.me.tamer.gameobjects;

import javax.print.attribute.standard.MediaSize.ISO;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.utils.IsoHelper;

public class GryphonScream extends DynamicObject {
	private final float SCREAM_AREA_WIDTH = 4.0f;
	private final float SCREAM_AREA_DIST = 4.0f;
	private float[] screamArea = {SCREAM_AREA_WIDTH, SCREAM_AREA_DIST};
	private Tamer tamer;
	
	//Debug
	private ShapeRenderer shapeRndr;
	
	public GryphonScream(Tamer t){
		tamer = t;
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
	public void debugDraw(ShapeRenderer shapeRndr){
		
		Vector2 pos = IsoHelper.twoDToIso(tamer.getPosition());
		System.out.println("tamerpos:"+pos.x+","+pos.y);
		shapeRndr.begin(ShapeType.Line);
		
		shapeRndr.setColor(1, 1, 1, 1);
		shapeRndr.line(pos.x, pos.y, 300, 300);
		
		shapeRndr.end();
		
	}
	
	
	@Override
	public void update(float dt) {
	}
	
	public float[] getCurrentScreamArea(){
		return screamArea;
	}
}
