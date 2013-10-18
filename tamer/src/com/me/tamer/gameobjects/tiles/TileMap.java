package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Level;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RendererFactory;

/**
 * @author tomi
 * The terrrain object for the environment.
 * Hold the grid info, which is used to draw tiles and obstacles on it.
 * Is also the lowest lvl layer, under which objects are drawn if they fall underground.
 *
 */
public class TileMap extends StaticObject{
	
	private int numTiles	=0;
	private ArrayList<Vector2> terrain;
	private Vector2 bounds;
	
	public TileMap(){
		setZindex(1);
	}
	
	public void setup(Level level){
		level.addNewObject(this);
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
		setSize("1:1");
		
		this.renderType = graphics;

	}
	

	

	
	
	
	
	
	

}
