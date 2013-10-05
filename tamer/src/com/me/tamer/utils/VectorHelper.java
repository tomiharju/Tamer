package com.me.tamer.utils;

import com.badlogic.gdx.math.Vector2;

public class VectorHelper {
	static Vector2 projection = new Vector2();
	static Vector2 unit = new Vector2();
	public static Vector2 projection(Vector2 a, Vector2 b){
		unit.set(b);
		unit.nor();
		float dot = a.dot(unit);
		projection.set(unit.mul(dot));
		return projection;
		
		
		
	}
}
