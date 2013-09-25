package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
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
	
	private Renderer tilerenderer = null;
	private ArrayList<GroundTile> terrain;
	private Vector2 bounds;
	
	
	
	@Override
	public void draw(SpriteBatch batch){
		for(GroundTile tile : terrain){
			tile.draw(batch);
		}
			
	}
	
	public void setMapSize(String size){
		
		String[] values = size.split(":");
		int r = Integer.parseInt(values[0]);
		int c = Integer.parseInt(values[1]);
		this.rows = r;
		this.columns = c;
		terrain = new ArrayList<GroundTile>();
		
		for(int i = 0 ; i < this.rows ; i ++ )
			for(int k = 0 ; k < this.columns ; k ++){
				GroundTile tile = new GroundTile();
				//C stands for columns, which is same as cartesian x, row for y and cartesian y
				tile.setPosition( (i - rows/2) +":"+ (k - columns/2));
				tile.setRenderer("static:"+renderType);
				tile.setSize(this.size.x+":"+this.size.y);
				terrain.add(tile);
			}
		
		bounds = new Vector2(r,c);
	}
	

	public void setTileSize(String size) {
		String[] values = size.split(":");
		float w = Float.parseFloat(values[0]);
		float h = Float.parseFloat(values[1]);
		this.size = new Vector2(w,h);
		
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
