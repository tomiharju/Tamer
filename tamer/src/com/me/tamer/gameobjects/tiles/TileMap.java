package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.gameobjects.tiles.obstacles.Obstacle;
import com.me.tamer.services.TextureManager.TamerTexture;
import com.me.tamer.utils.Helper;

/**
 * @author tomi
 * The terrrain object for the environment.
 * Hold the grid info, which is used to draw tiles and obstacles on it.
 * Is also the lowest lvl layer, under which objects are drawn if they fall underground.
 *
 */
public class TileMap extends StaticObject{
	
	private int numTiles	= 0;
	private ArrayList<Vector2> terrain;
	private Vector2 origo = new Vector2(0,0);
	private Vector2 tamerpos = new Vector2();

	private Environment env = null;

	
	public void setup(Environment env){
		env.addNewObject(this);
		this.env = env;
		setZindex(2);
	}
	
	@Override
	public void draw(SpriteBatch batch){
		Renderer renderer = RenderPool.getRenderer(getRenderType());
		
		if(env.getTamer() != null)
			tamerpos.set(Helper.worldToScreen(env.getTamer().getShadow().getPosition()));
		else
			tamerpos.set(origo);
	
		for(int i = 0 ; i < numTiles ; i++){
			if(env.isVisible(terrain.get(i)))
				renderer.setPosition(Helper.worldToScreen(terrain.get(i)));
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
			tile.set(x , y );
			terrain.add(tile);
		}
		
		numTiles = terrain.size();	
	}
	
	public void setTerrain(String graphics){
		
//		Renderer render = RenderPool.addRendererToPool("static",graphics);
//		render.loadGraphics(graphics);
//		setSize(Helper.TILESIZE.x,Helper.TILESIZE.y);
//		render.setSize(getSize());
//		setRenderType(graphics);
		
		
		//just to test
		Renderer render = RenderPool.addRendererToPool("animated", "vTile");
		render.loadGraphics("dirt2", 1, 1);
		setSize(Helper.TILESIZE.x,Helper.TILESIZE.y);
		render.setSize(getSize());
		setRenderType("vTile");

	}


	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGraphics(String graphics) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getDebug() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void wakeUp(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGraphics(TamerTexture tex) {
		// TODO Auto-generated method stub
		
	}	
}
