package com.me.tamer.utils;


import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

public class IsoHelper{
		
	private static Matrix3 isomatrix = new Matrix3().scale(1,0.5f).rotate(-45);
	private static Vector2 temp = new Vector2(0,0);
	
	/**
	 * @param point
	 * @return
	 * Convert isometric point to cartesian coordinate.
	 */
	public static Vector2 isoTo2D(Vector2 point){
			temp.x = (2 * point.y + point.x ) / 2;
			temp.y = (2 * point.y - point.x ) / 2;
			return temp;
	}
	
	/**
	 * @param point
	 * @return
	 * Convert cartesian coordinate to isometric cordinate.
	 */
	public static Vector2 twoDToIso(Vector2 point){
			temp.set(point);
			temp.mul(isomatrix);
			return temp;
	}
	
	public static Vector2 getScreenCoordinatesFromGrid(Vector2 square)
	{
	    //Takes one square from the grid as parameter for now
	   

	    //Transform into screen coordinates
	    Vector2 screenCoordinates = new Vector2();
	      
	    screenCoordinates.x = (float) (((square.x - square.y)) * Math.sqrt(2.0f));
	    screenCoordinates.y = (float) ((square.x + square.y) * (Math.sqrt(2) /  2));
	    
	    
	    return screenCoordinates;

	}

	public static Vector2 twoDToTileIso(Vector2 point){
		temp.set(point);
		temp.mul(isomatrix);
		return temp;
	}

	/**
	 * @param point
	 * @param tileHeight
	 * @return Returns specific tile number from cartesian coordinates
	 * For example, tile at row 4 columm 6
	 */
	public static Vector2 getTileCoordinates(Vector2 point, int tileHeight){
			Vector2 tempPt = new Vector2(0,0);
			tempPt.x = (float) Math.floor( point.x / tileHeight);
			tempPt.y = (float) Math.floor( point.y / tileHeight);
			return tempPt;
	}
	
	/**
	 * @param point
	 * @param tileHeight
	 * @return returns cartesian coordinate based on specific tile position.
	 */
	public static Vector2 get2dFromTileCoordinates(Vector2 point, int tileHeight){
			Vector2 tempPt =new Vector2(0,0);
			tempPt.x = point.x * tileHeight;
			tempPt.y = point.y * tileHeight;
			return tempPt;
	}
}
