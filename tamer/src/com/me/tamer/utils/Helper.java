package com.me.tamer.utils;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

public class Helper {
	//Global scaleing variables
	//ELI IHAN SUOMEKSI TÄMÄ TILE_WIDTH ON NYT SE ARVO, JOKA KERROTTUNA TILEN LEVEYDELLÄ
	//ELI SQRT(2) JOTTA SAADAAN TILEN LEVEYS PIXELEISSÄ, SIITÄKU LASKEE NIIN TULEE 40 PIXELIÄ.
	public static float TILE_WIDTH =  480 / (float) (12 * Math.sqrt(2));
	public static float TILE_HEIGHT = TILE_WIDTH / 2;
	public static float VIRTUAL_SIZE_X = (float) (12 * Math.sqrt(2)) ;
	public static float VIRTUAL_SIZE_Y = (float) (40 * Math.sqrt(2) / 2);
	public static Vector2 TILESIZE = new Vector2((float)Math.sqrt(2),(float)Math.sqrt(2) / 2);
	static Vector2 projection = new Vector2();
	static Vector2 unit = new Vector2();
	
	private static Matrix3 worldMatrix = new Matrix3().scale(1,0.5f).rotate(-45);
	private static Matrix3 screenMatrix = new Matrix3().scale(1,0.5f).rotate(-45).inv();
	private static Vector2 temp = new Vector2(0,0), temp1 = new Vector2(0,0);
	
	public static Vector2 worldToScreen(Vector2 point){
	
		temp.set(point);
		temp.mul(worldMatrix);
		return temp;
	}
	
	public static Vector2 screenToWorld(Vector2 point){
		temp1.set(point);
		temp1.mul(screenMatrix);
		return temp1;
	}
	
	public static Vector2 projection(Vector2 a, Vector2 b){
		unit.set(b);
		unit.nor();
		float dot = a.dot(unit);
		projection.set(unit.mul(dot));
		return projection;	
	}
}
