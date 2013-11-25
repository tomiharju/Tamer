package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerStage;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.gameobjects.tiles.obstacles.Obstacle;
import com.me.tamer.services.TextureManager.TamerTexture;
import com.me.tamer.utils.Helper;

/**
 * @author tomi The terrrain object for the environment. Hold the grid info,
 *         which is used to draw tiles and obstacles on it. Is also the lowest
 *         lvl layer, under which objects are drawn if they fall underground.
 * 
 */
public class TileMap extends StaticObject {

	private ArrayList<Vector2> terrain;
	private Environment env = null;
	private int numTiles ;

	public void setup(Environment env) {
		this.env = env;
		env.setTileMapObject(this);
		setZindex(-20);
	}

	
	@Override
	public void draw(SpriteBatch batch) {
		
		/*
		 * Renderer renderer = RenderPool.getRenderer(getRenderType()); if
		 * (env.getTamer() != null) {
		 * tamerpos.set(Helper.worldToScreen(env.getTamer().getShadow()
		 * .getPosition()));
		 * 
		 * } else tamerpos.set(origo);
		 * 
		 * if (prevPos.dst(tamerpos) > 1) { visibleTiles.clear();
		 * prevPos.set(tamerpos); for (int i = 0; i < numTiles; i++) { if
		 * (env.isVisible(terrain.get(i))) { visibleTiles.add(terrain.get(i));
		 * renderer.setPosition(Helper.worldToScreen(terrain.get(i)));
		 * renderer.draw(batch); } } } else { for (int i = 0; i <
		 * visibleTiles.size(); i++) {
		 * renderer.setPosition(Helper.worldToScreen(visibleTiles.get(i)));
		 * renderer.draw(batch); } }
		 */
	}

	public void setTileMap(String data) {
		String[] points = data.split("\\.");
		terrain = new ArrayList<Vector2>();

		for (String s : points) {
			Vector2 tile = new Vector2();
			String[] coords = s.split("\\:");
			float x = Float.parseFloat(coords[0]);
			float y = Float.parseFloat(coords[1]);
			tile.set(x, y);
			terrain.add(tile);
		}
		numTiles = terrain.size();

	}

	public void generate(SpriteCache cache){
		TamerStage stage = TamerStage.instance();
		AssetManager assetManager = stage.getGame().getAssetManager();
		TextureRegion terrainTex = assetManager.get("data/graphics/sheetData",
				TextureAtlas.class).findRegion(getRenderType());
		Vector2 help = new Vector2();
		if (terrainTex != null)
			for (int i = 0; i < terrain.size(); i++) {
				help.set(Helper.worldToScreen(terrain.get(i)));
				cache.add(terrainTex, help.x,
						help.y, Helper.TILESIZE.x, Helper.TILESIZE.y);
				
			}
	
	}
	public void setTerrain(String graphics) {
		graphics = graphics.split("\\.")[0];
		setSize(Helper.TILESIZE);
		setRenderType(graphics);
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
	public int getNumTiles(){
		return numTiles;
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
