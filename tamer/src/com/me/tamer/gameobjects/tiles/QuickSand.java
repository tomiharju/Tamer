package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.tamer.gameobjects.superclasses.StaticObject;

public class QuickSand extends StaticObject{
	ArrayList<SandPart> parts;

	public QuickSand(){
		parts = new ArrayList<SandPart>();
	}
	public void addSandPart(SandPart p){
		parts.add(p);
	}
	
	public void resolvePullingPart(){
		//todo
	}
	public void draw(SpriteBatch batch){
		//Override to avoid default action
	}
	public void update(float dt){
		//Override to avoid default action
	}
}