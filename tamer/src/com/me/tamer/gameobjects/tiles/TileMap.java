package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
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
		cache.setProjectionMatrix(stage.getCamera().combined);

		AssetManager assetManager = stage.getGame().getAssetManager();
		TextureRegion terrainTex = assetManager.get("data/graphics/sheetData",
				TextureAtlas.class).findRegion(getRenderType());
	
		Vector2 help = new Vector2();
		if (terrainTex != null)
			for (int i = 0; i < terrain.size(); i++) {
				help.set(Helper.worldToScreen(terrain.get(i)));
				cache.add(terrainTex, help.x - Helper.TILESIZE.x / 2,
						help.y, Helper.TILESIZE.x , Helper.TILESIZE.y  );
				
			}
	
	}
	public void setTerrain(String graphics) {
		graphics = graphics.split("\\.")[0];
		setSize(Helper.TILESIZE);
		setRenderType(graphics);
	}

	public int getNumTiles(){
		return numTiles;
	}
}
