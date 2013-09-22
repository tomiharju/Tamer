package com.me.tamer.gameobjects.tiles;

import com.me.tamer.gameobjects.StaticObject;

/**
 * @author tomi
 * The terrrain object for the environment.
 * Hold the grid info, which is used to draw tiles and obstacles on it.
 * Is also the lowest lvl layer, under which objects are drawn if they fall underground.
 *
 */
public class TileMap extends StaticObject{
	
	private int tile_height = 0;
	private int tile_width = 0;
	private int width = 0;
	private int height = 0;
	public TileMap(){
		
		
	}
	
	
	public void setMapSize(int width,int height){
		this.width = width;
		this.height = height;
		
	}
	@Override
	public void setGraphicSize(String size) {
		String[] values = size.split(":");
		int w = Integer.parseInt(values[0]);
		int h = Integer.parseInt(values[1]);
		
		renderer.setSize(w, h);
		
	}
	

	public int getTile_width() {
		return tile_width;
	}

	public void setTile_width(int tile_width) {
		this.tile_width = tile_width;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getTile_height() {
		return tile_height;
	}

	public void setTile_height(int tile_height) {
		this.tile_height = tile_height;
	}
	
	
	
	
	
	
	
	

}
