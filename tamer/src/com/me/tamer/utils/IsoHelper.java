package com.me.tamer.utils;


import com.badlogic.gdx.math.Vector2;

public class IsoHelper{
		
	
	/**
	 * @param point
	 * @return
	 * Convert isometric point to cartesian coordinate.
	 */
	public static Vector2 isoTo2D(Vector2 point){
			Vector2  tempPt = new Vector2(0,0);
			tempPt.x = (2 * point.y + point.x ) / 2;
			tempPt.y = (2 * point.y - point.x ) / 2;
			return tempPt;
	}
	
	/**
	 * @param point
	 * @return
	 * Convert cartesian coordinate to isometric cordinate.
	 */
	public static Vector2 twoDToIso(Vector2 point){
			Vector2 tempPt = new Vector2(0,0);
			tempPt.x = point.x - point.y;
			tempPt.y = (point.x + point.y ) / 2;
			return tempPt;
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
			tempPt.y = (float) Math.floor(point.y / tileHeight);
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
