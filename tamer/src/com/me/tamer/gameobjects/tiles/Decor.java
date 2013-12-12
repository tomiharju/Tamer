package com.me.tamer.gameobjects.tiles;

import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.utils.Helper;
/**
 * @author Tamer
 *  Decor are all the non-interactive gameobjects in the level.
 *  Decor can be drawn with spritecache or as independet sprite ( Selectable in TamerWorldEditor )
 *  We can draw a lot of decor with spritecache, but if we need to make objects "hide" behind a decor
 *  it needs to be drawn as a normal sprite.
 *  
 */
public class Decor extends StaticObject{
	boolean cacheable = true;
	public void setup(Environment env) {
		if(cacheable)
			env.addStaticObject(this);
		else
			env.addNewObject(this);
		
		setSize(getSize().x + 0.1f, getSize().y + 0.05f);
	}
	public void wakeup(Environment env){
		env.addNewObject(this);
	}
	public void setCacheable(String value){
		cacheable = Boolean.parseBoolean(value);
		
	}

	public void setPixelsX(String pixels){
		float x = Float.parseFloat(pixels);
		setSize(x / Helper.TILE_WIDTH_PIXEL,getSize().y );
	}
	public void setPixelsY(String pixels){
		float y = Float.parseFloat(pixels);
		setSize(getSize().x ,y / Helper.TILE_WIDTH_PIXEL);
	}
}
