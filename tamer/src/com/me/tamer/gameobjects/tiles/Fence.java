package com.me.tamer.gameobjects.tiles;


import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.creatures.Worm;

public class Fence extends Prop{
	private float x,y,w,h;
	private Vector2 wormHeadPos = new Vector2();
	
	public Fence(float x,float y,float w,float h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public boolean checkIfInside(Worm worm){
		wormHeadPos.set(worm.getHead().getPosition());
		return wormHeadPos.x > x && wormHeadPos.x < x+w && wormHeadPos.y > y && wormHeadPos.y < y+h;
	}
}
