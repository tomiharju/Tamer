package com.me.tamer.gameobjects.tiles.obstacles;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.utils.IsoHelper;

public class QuickSand extends StaticObject{
	ArrayList<SandPart> parts;
	private Vector2 bogHoleCenter;
	private Vector2 temp;
	
	private boolean isActivated = false;

	
	public QuickSand(){
		parts = new ArrayList<SandPart>();
		bogHoleCenter = new Vector2();
		temp = new Vector2();
	}
	public void setup(Environment environment){
		environment.addNewObject(this);
		environment.addObstacle(this);
		setBogHole();
	}
	public void addSandPart(SandPart p){
		parts.add(p);
	}
	
	public void setBogHole(){
		for(SandPart s : parts)
			bogHoleCenter.add(s.getPosition());
		
		bogHoleCenter.div(parts.size());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(IsoHelper.twoDToTileIso(bogHoleCenter));
		shapeRndr.begin(ShapeType.Rectangle);
		shapeRndr.rect(temp.x-0.2f,temp.y, 0.4f,0.4f);
		shapeRndr.end();
		
	}
	
	public boolean getDebug(){
		return true;
	}
	
	
	
	
	
	
	
	
	public void draw(SpriteBatch batch){
		//Override to avoid default action
	}
	public void update(float dt){
		//Override to avoid default action
	}
}