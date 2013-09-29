package com.me.tamer.utils;


import com.badlogic.gdx.math.Vector2;

public class IsoHelper{
		
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
			temp.x = (point.x - point.y);
			temp.y = (point.x + point.y ) * 0.5f;
			return temp;
	}
	public static Vector2 twoDToTileIso(Vector2 point){
		temp.x = (point.x - point.y) * 0.5f;
		temp.y = (point.x + point.y ) * 0.5f;
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
