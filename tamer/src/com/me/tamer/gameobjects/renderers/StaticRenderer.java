package com.me.tamer.gameobjects.renderers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerStage;
import com.me.tamer.services.TextureManager.TamerAnimations;

/**
 * @author Kesyttäjät This class is the superclass for statically drawn
 *         objects This class only has a static Sprite object with attached
 *         texture on it.
 * 
 */
public class StaticRenderer implements Renderer {

	private Sprite sprite;
	private AssetManager assetManager;

	public StaticRenderer() {
		TamerStage stage = TamerStage.instance();
		assetManager = stage.getGame().getAssetManager();
	}

	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

	@Override
	public void loadGraphics(String gfx) {
		sprite = new Sprite( assetManager.get("data/graphics/sheetData", TextureAtlas.class).findRegion(gfx) );
		sprite.setColor(Color.WHITE);
	}

	@Override
	public void setSize(float w, float h) {
		sprite.setSize(w, h);
	}

	@Override
	public void setSize(Vector2 size) {
		sprite.setSize(size.x, size.y);
	}

	@Override
	public void setPosition(Vector2 pos) {
		sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y);
//		sprite.setPosition(pos.x, pos.y);
	}

	public void setBounds(float x, float y, float width, float height) {
		sprite.setBounds(x, y, width, height);

	}

	@Override
	public void setOrientation(int orientation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadGraphics(String animName, int FRAME_COLS, int FRAME_ROWS) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAngle(float angle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadEffect(String animName, int FRAME_COLS, int FRAME_ROWS,
			boolean looping, float speed) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColor(float r, float g, float b, float a) {
		if (sprite != null)
			sprite.setColor(r, g, b, a);
	}

	@Override
	public Color getColor() {
		if (sprite != null)
			return sprite.getColor();
		else
			return Color.WHITE;
	}

	@Override
	public void loadGraphics(TamerAnimations animName, int FRAME_COLS,
			int FRAME_ROWS) {
		// TODO Auto-generated method stub
		
	}


}
