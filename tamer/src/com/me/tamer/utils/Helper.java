package com.me.tamer.utils;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

public class Helper {
	
	public static Vector2 TILESIZE = new Vector2((float)Math.sqrt(2)+0.1f,((float)Math.sqrt(2) / 2)+0.05f);
	public static float TILE_WIDTH_PIXEL =  480 / (float) (12 * Math.sqrt(2));
	public static float TILE_HEIGHT_PIXEL = TILE_WIDTH_PIXEL / 2;
	public static float VIRTUAL_SIZE_X = (float) (12 * Math.sqrt(2));
	public static float VIRTUAL_SIZE_Y = (float) (40 * Math.sqrt(2) / 2);
	
	static Vector2 projection = new Vector2();
	static Vector2 unit = new Vector2();
	
	private static Matrix3 worldMatrix = new Matrix3().scale(1f,0.5f).rotate(-45);
	private static Matrix3 screenMatrix = new Matrix3().scale(1f,0.5f).rotate(-45).inv();
	
	private static Vector2 temp = new Vector2(0,0), temp1 = new Vector2(0,0);
	
	//Variables for debug draw
	private static Vector2 start = new Vector2();
	private static Vector2 end = new Vector2();
	private static ArrayList<Vector2> debugLines = new ArrayList<Vector2>();
	
	public static Vector2 worldToScreen(Vector2 point){
		temp.set(point.x + Helper.TILESIZE.x / 2, point.y );
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
	
	public static void debugDraw(ShapeRenderer debugRender, Camera camera){
		
		debugRender.setProjectionMatrix(camera.combined);
		debugRender.setColor(1, 1, 1, 1);
		for (int i = 0; i<debugLines.size(); i+=2){
			start.set ( Helper.worldToScreen(debugLines.get(i).tmp() ));
			end.set( Helper.worldToScreen(debugLines.get(i+1).tmp() ));
			
			debugRender.begin(ShapeType.Line);
			debugRender.line(start.x , start.y, end.x, end.y );
			debugRender.end();
		}
	}
	
	public static void addDebugLine(Vector2 s, Vector2 e){	
		debugLines.add( new Vector2().set(s));
		debugLines.add( new Vector2().set(e));
	}
	
}
