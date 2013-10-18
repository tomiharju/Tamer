package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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
	
	private int tile_height 		= 0;
	private int tile_width 		= 0;
	private int rows 				= 0;
	private int columns			= 0;
	private int numTiles	=0;
	private ArrayList<Vector2> terrain;
	private Vector2 bounds;
	
	
	
	@Override
	public void draw(SpriteBatch batch){
		Renderer renderer = RenderPool.getRenderer(renderType);
		renderer.setSize(size.x,size.y);
		for(int i = 0 ; i < numTiles ; i++){
			renderer.setPosition((terrain.get(i)));
			renderer.draw(batch);
		}
			
	}
	
	public void setMapSize(String size){
		
		String[] values = size.split(":");
		int r = Integer.parseInt(values[0]);
		int c = Integer.parseInt(values[1]);
		this.rows = r;
		this.columns = c;
		numTiles = r * c;
		terrain = new ArrayList<Vector2>();
		setRenderer("static:"+renderType);
		setSize("1:1");
		for(int y = 0 ; y < this.rows ; y ++ )
			for(int x = 0 ; x < this.columns ; x ++){
				Vector2 tile = new Vector2();
				float tile_x = ( x - y ) * 0.5f;
				float tile_y = ((x + y ) * 0.5f ) - columns / 2; 
				tile.set(tile_x,tile_y);
				terrain.add(tile);
			}
		
		bounds = new Vector2(r,c);
	}
	

	public int getTile_width() {
		return tile_width;
	}

	public void setTile_width(int tile_width) {
		this.tile_width = tile_width;
	}

	public int getTile_height() {
		return tile_height;
	}

	public void setTile_height(int tile_height) {
		this.tile_height = tile_height;
	}
	
	
	
	
	
	
	
	

}
