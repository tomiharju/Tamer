package com.me.tamer.utils;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.superclasses.GameObject;

public class DrawOrderComparator implements Comparator<GameObject> {
	private Vector2 o1Pos = new Vector2();
	private Vector2 o2Pos = new Vector2();
	@Override
	public int compare(GameObject o1, GameObject o2) {
		//First try to sort with z-index
		if (o1.getZIndex() < o2.getZIndex())return 1;
		else if (o1.getZIndex() > o2.getZIndex())return -1; 
		else {
			//If z-index is the same continue sorting with screenPos.y
			//If either one of the objects has null-position -> draw it first
			if (o1.getPosition() != null) o1Pos.set(o1.getPosition());
			else{
				Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName()
						+ " :: o1 null");
				return -1;
			}
			if (o2.getPosition() != null) o2Pos.set(o2.getPosition());
			else{
				Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName()
						+ " :: o2 null");
				return 1;
				
			}
			o1Pos.set(Helper.worldToScreen(o1Pos));
			o2Pos.set(Helper.worldToScreen(o2Pos));
			//Draw the object that is further away first
			if (o1Pos.y > o2Pos.y){
				return -1;
			}else if (o1Pos.y < o2Pos.y){
				return 1;	
			}else return 0;
		}	
	}
}
