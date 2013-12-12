package com.me.tamer.gameobjects.superclasses;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.utils.Helper;

/**
 * @author Tamer
 * Superclass for all the static objects in game.
 * Static objects can be drawn either by using a spritecache or by batching. ( selectable in TamerWorldEditor )
 *
 */
public abstract class StaticObject implements GameObject{
	
	private Vector2 position		= new Vector2();
	private Vector2 size 			= new Vector2();
	private Vector2 centerPosition 	= new Vector2();
	

	private String renderType		= null;
	private boolean isCarbage 		= false;
	private int zIndex 				= 0;
	private float bounds = 0;
	
	TweenManager tweenManager;

	@Override
	public void draw(SpriteBatch batch) {
		//Don't draw if this is dummy object
		if(renderType != null){
			Renderer renderer = RenderPool.getRenderer(getRenderType());
			renderer.setSize(getSize());
			renderer.setPosition(Helper.worldToScreen(position));
			renderer.draw(batch);
		}	
	}
	public void update(float dt){
		//Do nothing by default
	}
	public boolean isWithinRange(Vector2 poitn, float radius){
		return false;
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
	public void setSize(Vector2 size) {
		this.size.set(size);
		
	}
	public void setSize(float x, float y){
		this.size.set(x,y);
	}
	
	//-------------------------------------------------------------------------
	//Object creation methods
	//-------------------------------------------------------------------------
	public void setGraphics(String graphics){
		graphics = graphics.split("\\.")[0];
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setRenderType(graphics);
	}
	
	@Override
	public void setPosition(String pos) {
		String[] values = pos.split(":");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		this.position.set(x,y);
	}
	
	//-------------------------------------------------------------------------
	//Setters and getters
	//-------------------------------------------------------------------------
	public void setPosition(Vector2 pos){
		this.position.set(pos);
	}
	
	@Override
	public Vector2 getPosition() {
		return position;
	}
	public Vector2 getCenterPosition(){
		this.centerPosition.set(position.x - Helper.TILESIZE.x / 2,position.y + Helper.TILESIZE.y / 2);
		return centerPosition;
	}
	
	public Vector2 getScreenTileCenter(){
		this.centerPosition.set(Helper.worldToScreen(position).x + Helper.TILESIZE.x / 2, Helper.worldToScreen(position).y + Helper.TILESIZE.y / 2);
		return centerPosition;
	}

	@Override
	public Vector2 getSize() {
		return size;
	}

	public void markAsActive() {
		isCarbage = false;
	}

	@Override
	public int getZIndex() {
		return zIndex;
	}

	@Override
	public void setZindex(int z) {
		zIndex = z;
		
	}
	
	public String getRenderType() {
		return renderType;
	}

	public void setRenderType(String renderType) {
		this.renderType = renderType;
	}
	public void setBounds(float bounds){
		this.bounds = bounds;
	}
	@Override
	public void setzIndex(String index) {
		int zIndex = Integer.parseInt(index);
		setZindex(-zIndex);
		
	}
	
	@Override
	public void debugDraw(ShapeRenderer sr){
		
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
	
	public float getBounds(){
		return bounds;
	}
}
