package com.me.tamer.gameobjects.superclasses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.utils.Helper;

public class StaticObject implements GameObject{
	private Vector2 position		= new Vector2();
	private Vector2 size 			= new Vector2();
	private Vector2 centerPosition 	= new Vector2();
	

	private String renderType		= null;
	private boolean isCarbage 		= false;
	private boolean debug 			= false;
	private int zIndex 				= 0;
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		//Don't draw if this is dummy object
		if(renderType != null){
			Renderer renderer = RenderPool.getRenderer(getRenderType());
			renderer.setSize(getSize());
			renderer.setPosition(Helper.worldToScreen(position));
			renderer.setOrientation(0);
			renderer.draw(batch);
		}
		
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
	
	@Override
	public void setPosition(String pos) {
		String[] values = pos.split(":");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		this.position.set(x,y);
		}
	
	public void setPosition(Vector2 pos){
		this.position.set(pos);
	}
	@Override
	public Vector2 getPosition() {
		return position;
	}
	public Vector2 getCenterPosition(){
		this.centerPosition.set(position.x,position.y + Helper.TILESIZE.y / 4);
		return centerPosition;
	}
	
	public Vector2 getScreenTileCenter(){
		this.centerPosition.set(Helper.worldToScreen(position).x + Helper.TILESIZE.x / 2, Helper.worldToScreen(position).y + Helper.TILESIZE.y / 2);
		return centerPosition;
	}
	
	/*
	public void setCenterPosition(Vector2 centerPosition) {
		this.centerPosition.set(centerPosition);
	}
	public void setCenterPosition(float x,float y) {
		this.centerPosition.set(x,y);
	}
	*/
	@Override
	public Vector2 getSize() {
		return size;
	}
	

	

	@Override
	public void setup(Environment level) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void wakeUp(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDebug(boolean b) {
		debug = b;	
	}
	
	@Override
	public boolean getDebug(){
		return debug;
	}

	public void markAsActive() {
		isCarbage = false;
	}

	@Override
	public void dispose(Environment level) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getZIndex() {
		return zIndex;
	}

	@Override
	public void setZindex(int z) {
		zIndex = z;
		
	}

	@Override
	public void setGraphics(String graphics) {
		// TODO Auto-generated method stub
		
	}

	

	public String getRenderType() {
		return renderType;
	}

	public void setRenderType(String renderType) {
		this.renderType = renderType;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setzIndex(String index) {
		int zIndex = Integer.parseInt(index);
		setZindex(-zIndex);
		
	}

	

}
