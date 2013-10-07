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
	}
	
	@Override 
	public void debugDraw(ShapeRenderer shapeRndr){
		
		Vector2 pos = tamer.getPosition();
		Vector2 head = IsoHelper.twoDToIso(tamer.getHeading());
		
		System.out.println(pos.x+","+pos.y);
		shapeRndr.begin(ShapeType.Line);
		
		shapeRndr.setColor(1, 1, 1, 1);
		shapeRndr.line(pos.x, pos.y, head.x * 10, head.y * 10);
		
		shapeRndr.end();
		
		/*
		shapeRndr.begin(ShapeType.Circle);
		Vector2 cPos = new Vector2();
		cPos.x = 0;
		cPos.y = 5;
		
		shapeRndr.circle(cPos.x, cPos.y, 1);
		shapeRndr.end();
		*/
		
		shapeRndr.begin(ShapeType.Circle);
		Vector2 cPos2 =  IsoHelper.twoDToIso(new Vector2(5,0));
		shapeRndr.circle(cPos2.x , cPos2.y, 0.1f);
		shapeRndr.end();
		
		shapeRndr.begin(ShapeType.Circle);
		Vector2 cPos3 =  IsoHelper.twoDToIso(new Vector2(0,0));
		shapeRndr.circle(cPos3.x , cPos3.y, 0.1f);
		shapeRndr.end();
		
		shapeRndr.begin(ShapeType.Circle);
		Vector2 cPos4 =  IsoHelper.twoDToIso(new Vector2(-5,0 ));
		shapeRndr.circle(cPos4.x, cPos4.y , 0.1f);
		shapeRndr.end();
	}
	
	
	@Override
	public void update(float dt) {
	}
	
	public float[] getCurrentScreamArea(){
		return screamArea;
	}
}
