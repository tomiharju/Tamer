package com.me.tamer.gameobjects.superclasses;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.services.TextureManager.TamerTexture;
import com.me.tamer.utils.Helper;

public abstract class DynamicObject implements GameObject {

	// Someone has to fix these to private
	private Vector2 position = new Vector2(); // "World position"
	private Vector2 centerPosition = new Vector2();
	private Vector2 velocity = new Vector2(); // "World velocity"
	private Vector2 heading = new Vector2(); // Unit vector of current velocity
	private Vector2 spriteHeading = new Vector2();
	private Vector2 force = new Vector2(); // Magnitude and direction of per
											// loop velocity increment
	private Vector2 size = new Vector2(); // Graphics sprite size
	private float angle = 0;
	private String renderType = null; // Graphics name, used for fetching
										// correct renderer for object
	private boolean isCarbage = false; // Setting to true, causes carbage
										// collection loop to remove this object
										// from game
	private boolean debug = false;
	private int zIndex = 0; // Forced drawing order
	private Vector2 isoHeading = new Vector2(); // Used for determining the
												// sprite
	protected float borderOffset = 0;
	private Vector2 zeroHeading = new Vector2(-0.5f, 1);// -1 +
														// (float)Math.sin(Math.PI/8),1
														// +
														// (float)Math.cos(Math.PI/8));//-0.5f,
														// 2.0f);//;
	private float headingAngle = 0;
	private float spriteNumber = 0;
	private boolean collisionDisabled = false;
	protected boolean fading = false;

	protected TweenManager tweenManager;
	
	//Sound
	SoundManager sound;

	@Override
	public void draw(SpriteBatch batch) {
		Renderer renderer = RenderPool.getRenderer(getRenderType());
		renderer.setSize(getSize());
		renderer.setPosition(Helper.worldToScreen(position));
		renderer.setOrientation( solveOrientation() );
		renderer.draw(batch);	
	}

	public void update(float dt) {
	}

	public int solveOrientation() {

		zeroHeading.nor();
		spriteHeading.set(heading);
		//quick fix to the issue
		if (spriteHeading.y == 0) spriteHeading.y = 0.001f;
		else if(spriteHeading.x == 0) spriteHeading.x = 0.001f;
		
		headingAngle = ((float) Math.acos(spriteHeading.dot(zeroHeading)
				/ (spriteHeading.len() * zeroHeading.len())));
		spriteNumber = ((float) (headingAngle / Math.PI * 180 / 45));

		// cannot be below zero
		if (spriteNumber <= 0)
			spriteNumber = 0.001f;
//		if(spriteNumber == 1)
//			spriteNumber = -0.001f;
		
		if (spriteHeading.x > zeroHeading.x && spriteHeading.y > 0)
			spriteNumber = (8 - spriteNumber);
		else if (spriteHeading.x > -zeroHeading.x && spriteHeading.y < 0)
			spriteNumber = (8 - spriteNumber);

		spriteNumber = ((float) Math.floor(spriteNumber));
		return (int)(spriteNumber);
	}
	
	public void playSound(TamerSound s){
		if (sound == null) sound = SoundManager.instance();
		
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Playing sound: " +s.name());
		sound.play(s);
	}

	@Override
	public void setSize(Vector2 size) {
		this.size.set(size);
	}

	@Override
	public void setPosition(String pos) {
		String[] values = pos.split(":");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		this.position.set(x, y);
	}

	@Override
	public void setPosition(Vector2 pos) {
		this.position.set(pos);
	}

	public void setVelocity(String vel) {
		String[] values = vel.split(":");
		float x = Float.parseFloat(values[0]);
		float y = Float.parseFloat(values[1]);
		this.velocity.set(x, y);

	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public Vector2 getSize() {
		return size;
	}

	public Vector2 getHeading() {
		return heading;
	}

	public void setHeading(Vector2 heading) {
		heading.nor();
		this.heading.set(heading);
	}

	public void setHeading(float x, float y) {
		heading.set(x, y);
		heading.nor();
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity.set(velocity);
	}

	@Override
	public void markAsActive() {
		isCarbage = false;

	}

	@Override
	public void markAsCarbage() {
		isCarbage = true;

	}

	@Override
	public boolean isCarbage() {
		return isCarbage;
	}

	@Override
	public boolean getDebug() {
		return debug;
	}

	@Override
	public int getZIndex() {
		return zIndex;
	}

	public void setzIndex(String index) {

	}

	@Override
	public void setZindex(int z) {
		zIndex = z;

	}

	public Vector2 getForce() {
		return force;
	}

	public void setForce(Vector2 force) {
		this.force.set(force);
	}

	public void setForce(float x, float y) {
		this.force.set(x, y);
	}

	public String getRenderType() {
		return renderType;
	}

	public void setRenderType(String renderType) {
		this.renderType = renderType;
	}
	
	public void setRenderType(TamerTexture tex) {
		
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	@Override
	public void setSize(float x, float y) {
		this.size.set(x, y);

	}

	public boolean isWithinRange(Vector2 point, float radius) {
		if(position.dst(point) < radius)
			return true;
			
		else
			return false;
		
	}

	@Override
	public Vector2 getCenterPosition() {
		this.centerPosition.set(position.x - getSize().x / 2, position.y
				+ getSize().y / 2);
		return centerPosition;
	}

	public boolean isCollisionDisabled() {
		return collisionDisabled;
	}

	public void disableCollision() {
		collisionDisabled = true;
	}

	public void enableCollision() {
		collisionDisabled = false;
	}
	
	@Override
	public void setup(Environment level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose(Environment level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setGraphics(String graphics) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setGraphics(TamerTexture tex) {
		// TODO Auto-generated method stub

	}
	
	

}
