package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.gameobjects.tiles.obstacles.Obstacle;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RendererFactory;
import com.me.tamer.utils.VectorHelper;

/**
 * @author tomi
 * The terrrain object for the environment.
 * Hold the grid info, which is used to draw tiles and obstacles on it.
 * Is also the lowest lvl layer, under which objects are drawn if they fall underground.
 *
 */
public class TileMap extends StaticObject implements Obstacle{
	
	private int numTiles	=0;
	private ArrayList<Vector2> terrain;
	private Vector2 mapBounds;
	private Vector2 collisionHeading 	= new Vector2();
	private Vector2 collisionAxis		= new Vector2();
	private Vector2 collisionPos		= new Vector2();
	
	public void setup(Environment env){
		env.addNewObject(this);
		env.getObstacles().add(this);
		mapBounds = env.getMapBounds();
		setZindex(1);
	}
	
	@Override
	public void draw(SpriteBatch batch){
		Renderer renderer = RenderPool.getRenderer(renderType);
		renderer.setSize(size.x,size.y);
		for(int i = 0 ; i < numTiles ; i++){
			renderer.setPosition(IsoHelper.twoDToTileIso(terrain.get(i)));
			renderer.draw(batch);
		}
			
	}
	
	
	public void setTileMap(String data){
		String[] points = data.split("\\.");
		terrain = new ArrayList<Vector2>();
		for(String s : points){
			Vector2 tile = new Vector2();
			String[] coords = s.split("\\:");
			float x = Float.parseFloat(coords[0]);
			float y = Float.parseFloat(coords[1]);
			tile.set(x,y);
			terrain.add(tile);
		}
			
		numTiles = terrain.size();	
	}
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setSize(new Vector2(1,0.5f));
		
		this.renderType = graphics;

	}

	@Override
	public void resolve(ArrayList<Creature> creatures) {
		int size = creatures.size();
		for( int i = 0 ; i < size ; i ++){
			collisionPos.set(IsoHelper.twoDToTileIso(((DynamicObject) creatures.get(i)).getPosition()));
			
			if(collisionPos.x > mapBounds.x / 2 || collisionPos.x < -mapBounds.x / 2){
				collisionAxis.set(0,1);
				collisionHeading.set(creatures.get(i).getHeading());
				collisionHeading.set(VectorHelper.projection(collisionHeading,collisionAxis));
				collisionHeading.rotate(45);
				System.out.println("Collision heading "+collisionHeading.toString());

				creatures.get(i).setHeading(collisionHeading);
			}
			if(collisionPos.y > mapBounds.y / 2 || collisionPos.y < -mapBounds.y / 2){
				collisionAxis.set(1,0);
				collisionHeading.set(creatures.get(i).getHeading());
				collisionHeading.set(VectorHelper.projection(collisionHeading,collisionAxis));
				collisionHeading.rotate(45);

				System.out.println("Collision heading "+collisionHeading.toString());
				
				creatures.get(i).setHeading(collisionHeading);
			}
			
		}
		
	}
	

	

	
	
	
	
	
	

}
