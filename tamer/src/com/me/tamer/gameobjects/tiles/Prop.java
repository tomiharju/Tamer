package com.me.tamer.gameobjects.tiles;


import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.gameobjects.tiles.obstacles.Obstacle;
import com.me.tamer.physics.RigidBody;

/**
 * @author tomi
 * Props are all the non-interactive gameobjects in the level, props are trees, rocks, stumps and 
 * all the other objects which only affect with their rigidbodies and graphical aspect.
 */
public class Prop extends StaticObject implements Obstacle{
	
	private Vector2 x_axis = new Vector2(1,0);
	private Vector2 y_axis = new Vector2(0,1);
	private Vector2 separator = new Vector2();
	
	public void setup(Environment level){
		level.addNewObject(this);
		level.getObstacles().add(this);
		setZindex(0);
	}

	public void setPixelsX(String pixels){
		float x = Float.parseFloat(pixels);
		setSize(x,getSize().y);
	}
	public void setPixelsY(String pixels){
		float y = Float.parseFloat(pixels);
		setSize(getSize().x,y);
	}
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setSize(getSize().x / 40 , (getSize().y / 40));
		setRenderType(graphics);
	}

	@Override
	public void resolve(ArrayList<Creature> creatures) {
		int size = creatures.size();
		for( int i = 0 ; i < size ; i ++){
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
					creatures.get(i).setHeading(separator.tmp().rotate(-45));

				}
				
				
				
			}
			
		}
		
	}
	
}
