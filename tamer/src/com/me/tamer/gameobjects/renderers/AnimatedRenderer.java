package com.me.tamer.gameobjects.renderers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerStage;
import com.me.tamer.services.TextureManager.TamerTexture;

/**
 * @author Kesyttäjät
 * This class is the superclass for all objects that needs to be animated
 * This class has all the attributes needed for animation
 *
 */

/**
 * @author ville
 * 
 */
public class AnimatedRenderer implements Renderer {

	private Sprite sprite;
	private ArrayList<Animation> animations;
	private int currentAnimation = 0;

	private Texture spriteSheet;
	private TextureRegion[][] frames;
	private TextureRegion[] effectFrames;
	private TextureRegion currentFrame;
	private float stateTime;
	private float animationDuration = 3;
	private Vector2 size = new Vector2();
	private Vector2 pos = new Vector2();
	private float angle = 0;

	private Color batchColor = new Color(Color.WHITE);

	// Shader test
	private ShaderProgram shader;
	private ShaderProgram defaultShader;
	private float vtime = 0;
	private Texture tex0, tex1;
	int u_worldView, a_position;

	private TamerStage stage;

	private AssetManager assetManager;

	public AnimatedRenderer() {
		animations = new ArrayList<Animation>();
		stage = TamerStage.instance();
		assetManager = stage.getGame().getAssetManager();
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (!animations.isEmpty()) {
			currentFrame = animations.get(currentAnimation).getKeyFrame(
					stateTime, true);

			if (angle != 0) {
				batch.draw(currentFrame, pos.x - size.x / 2, pos.y, size.x / 2,
						size.y / 2, size.x, size.y, 1, 1, angle);
			} else {
				batch.draw(currentFrame, pos.x - size.x / 2, pos.y, size.x,
						size.y);
			}

		}
	}
	
	/*
	 * Old stuff
	 */
	@Override
	public void loadGraphics(String graphicsName) {
		sprite = new Sprite(new Texture(Gdx.files.internal("data/graphics/"
				+ graphicsName + ".png")));
		if (sprite == null)
			throw new IllegalArgumentException("Could not load sprite!");

		animations.add(new Animation(animationDuration, sprite));
	}

	@Override
	public void loadGraphics(TamerTexture animName, int FRAME_COLS,
			int FRAME_ROWS) {
		
		spriteSheet = assetManager.get(animName.getFileName(), Texture.class);
		frames = TextureRegion.split(spriteSheet, spriteSheet.getWidth()
				/ FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);

		for (int i = 0; i < FRAME_ROWS; i++) {
			animations.add(new Animation(animationDuration, frames[i]));
		}

		stateTime = 0f;
	}

	/*
	 * Old stuff
	 */
	@Override
	public void loadGraphics(String animName, int FRAME_COLS, int FRAME_ROWS) {
		spriteSheet = new Texture(
				Gdx.files.internal("data/graphics/animations/" + animName
						+ ".png"));

		frames = TextureRegion.split(spriteSheet, spriteSheet.getWidth()
				/ FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);

		for (int i = 0; i < FRAME_ROWS; i++) {
			animations.add(new Animation(animationDuration, frames[i]));
		}

		stateTime = 0f;

	}

	public void setAnimSpeed(float speed) {
		animationDuration = speed;
	}

	@Override
	public void setSize(float w, float h) {
		this.size.set(w, h);
	}

	@Override
	public void setSize(Vector2 size) {
		this.size.set(size);

	}

	@Override
	public void setPosition(Vector2 pos) {
		this.pos.set(pos.x, pos.y);
	}

	@Override
	public void setOrientation(int orientation) {
		this.currentAnimation = orientation;
	}

	public void setAngle(float a) {
		this.angle = a;
	}

	@Override
	public void setBounds(float x, float y, float width, float height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadEffect(String animName, int FRAME_COLS, int FRAME_ROWS,
			boolean looping, float speed) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColor(float r, float g, float b, float a) {
		batchColor.set(r, g, b, a);
	}

	@Override
	public Color getColor() {
		return batchColor;
	}

	@Override
	public void loadGraphics(TamerTexture animName) {
		// TODO Auto-generated method stub
		
	}

}
