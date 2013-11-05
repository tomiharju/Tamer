package com.me.tamer.gameobjects.tiles;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.gameobjects.tiles.obstacles.Obstacle;
import com.me.tamer.physics.RigidBody;
import com.me.tamer.utils.Helper;

/**
 * @author tomi
 * Props are all the non-interactive gameobjects in the level, props are trees, rocks, stumps and 
 * all the other objects which only affect with their rigidbodies and graphical aspect.
 */
public class Prop extends StaticObject implements Obstacle{
	
	
	
	private float scale 	 = 0;
	private float bounds	 = 0;
	private Vector2 temp	 = new Vector2();
	
	public void setup(Environment level){
		level.addNewObject(this);
		level.getObstacles().add(this);
		setZindex(0);
	}

	public void setPixelsX(String pixels){
		float x = Float.parseFloat(pixels);
		setSize(x / Helper.TILE_WIDTH,getSize().y );
	}
	public void setPixelsY(String pixels){
		float y = Float.parseFloat(pixels);
		setSize(getSize().x ,y / Helper.TILE_WIDTH);
	}
	public void setScale(String scale){
		float s = Float.parseFloat(scale);
		this.scale = s;
		bounds =  this.scale * 4;
	
	}
	
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setSize(getSize().x , getSize().y);
		setRenderType(graphics);
	}

	@Override
	public void resolve(ArrayList<Creature> creatures) {
		int size = creatures.size();
		for( int i = 0 ; i < size ; i ++){
			
			temp.set(((DynamicObject) creatures.get(i)).getPosition());
		//	temp.add(((DynamicObject) creatures.get(i)).getVelocity().tmp().mul(Gdx.graphics.getDeltaTime()));
			Vector2 center = getPosition();
		
			if(temp.x > center.x - bounds / 2 && temp.x < center.x + bounds / 2
 				& temp.y > center.y  && temp.y < center.y + bounds ){
				System.out.println("Worm at " +temp.toString() +" center at "+center.toString()+ " bounds " +bounds );
				temp.set(-creatures.get(i).getHeading().y*(float)Math.random()*1,creatures.get(i).getHeading().x*(float)Math.random()*1);
				creatures.get(i).setHeading(temp);
				}
			
			}
			
			
			
			
			
			
			
			
			
			
			/*
			//Check if creature within check radius ( 0.5f to be adjusted )
			float check_radius = getSize().x / 3 + ((DynamicObject) creatures.get(i)).getSize().x + 0.5f;
			float distance = ((DynamicObject) creatures.get(i)).getPosition().dst(getPosition());
			//System.out.println("Distance " + distance + " Check radius "+check_radius);
			if(distance < check_radius){
				//System.out.println("Worm close to collision");
				separator = ((DynamicObject) creatures.get(i)).getPosition().tmp().sub(getPosition());
				float x_dot = separator.dot(x_axis);
				float y_dot = separator.dot(y_axis);
				if(x_dot > y_dot){
					//System.out.println("Turning towards x-axis");
					creatures.get(i).setHeading(separator.tmp().rotate(45));
				}
				else if(y_dot > x_dot){
					creatures.get(i).setHeading(separator);

				}
				
				
				
			}
		*/	
		
		
	}
	
	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		
		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(Helper.worldToScreen(getPosition()));
		shapeRndr.begin(ShapeType.Rectangle);
		shapeRndr.rect(temp.x - bounds / 2,temp.y, bounds , bounds / 2);
		shapeRndr.end();
		
		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(Helper.worldToScreen(getPosition()));
		shapeRndr.begin(ShapeType.Rectangle);
		shapeRndr.rect(temp.x -0.1f,temp.y-0.1f, 0.2f ,0.2f);
		shapeRndr.end();
			
	}
	
	public boolean getDebug(){
		return false;
	}
	
	public Vector2 getCenterPosition(){
		return getPosition().tmp().set(getPosition().x - bounds, getPosition().y + bounds );
		
	}
	

	
	
}
