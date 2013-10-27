package com.me.tamer.gameobjects.superclasses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.physics.RigidBody;
import com.me.tamer.physics.RigidBodyBox;
import com.me.tamer.physics.RigidBodyCircle;
import com.me.tamer.utils.IsoHelper;

public class StaticObject implements GameObject{
	protected RigidBody body = null;
	protected Vector2 position;
	protected Vector2 size = new Vector2();
	protected Vector2 centerPosition = new Vector2();
	protected String renderType = null;
	private boolean isCarbage = false;
	private boolean debug = false;
	private int zIndex = 0;
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		Renderer renderer = RenderPool.getRenderer(renderType);
		renderer.setSize(size.x,size.y);
		renderer.setPosition(IsoHelper.twoDToTileIso(position));
		renderer.setOrientation(0);
		renderer.draw(batch);
		
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
	@Override
	public void setPosition(String pos) {
		String[] values = pos.split(":");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		this.position = new Vector2(x,y);
		}

	@Override
	public Vector2 getPosition() {
		return position;
	}
	public Vector2 getCenterPosition(){
		this.centerPosition.set(position.x-this.size.x / 2,position.y + this.size.y );
		return centerPosition;
	}

	@Override
	public Vector2 getSize() {
		return size;
	}
	public void setRigidBody(String bodytype){
		if(bodytype.equalsIgnoreCase("box"))
			body = new RigidBodyBox(position,new Vector2(0,0),0,size.x,size.y); //Position, speed, mass, width,height ( speed and mass are 0 cause its static object )
		else if(bodytype.equalsIgnoreCase("circle"))
			body = new RigidBodyCircle(position,new Vector2(0,0),0,size.x/2);//Position, velocity, mass, radii
		else if(bodytype.equalsIgnoreCase("no-body"))
			body = null;
		
	}

	@Override
	public RigidBody getRigidBody() {
		return body;
	}

	@Override
	public void setup(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resolveForces(float dt) {
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

	@Override
	public void setPosition(Vector2 pos) {
		// TODO Auto-generated method stub
		
	}

	

}
