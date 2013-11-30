package com.me.tamer.gameobjects.tiles;

import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.utils.Helper;
/**
 * @author tomi Decor are all the non-interactive gameobjects in the level,
 *         props are trees, rocks, stumps and all the other objects which only
 *         affect with their rigidbodies and graphical aspect.
 */
public class Decor extends StaticObject{
	boolean cacheable = true;
	public void setup(Environment env) {
		if(cacheable)
			env.addStaticObject(this);
		else
			env.addNewObject(this);
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
