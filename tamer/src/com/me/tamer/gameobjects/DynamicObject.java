package com.me.tamer.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.physics.RigidBody;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RendererFactory;

public class DynamicObject implements GameObject{
	
	private Vector2 position;
	private Vector2 velocity;
	private Vector2 force;
	private float mass;
	private float invMass;
	private Renderer renderer;
	private boolean isCarbage = false;
	protected RigidBody body = null;
	
	
	@Override
	public void update(float dt) {
		
	}
	@Override
	public void draw(SpriteBatch batch) {
		renderer.draw(batch);
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setRender(String rendertype) {
		Renderer renderer = RendererFactory.createRenderer(rendertype);
		this.renderer = renderer;
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
	public void setGraphicSize(String size) {
		String[] values = size.split(":");
		int w = Integer.parseInt(values[0]);
		int h = Integer.parseInt(values[1]);
		this.renderer.setSize(w, h);
		
	}
	@Override
	public void setGraphics(String graphics) {
		this.renderer.loadGraphics(graphics);
		
	}
	@Override
	public void setPosition(String pos) {
		String[] values = pos.split(":");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		Vector2 position = IsoHelper.twoDToIso(new Vector2(x,y));
		this.position = position;
		renderer.setPosition(position);
	}
	public void setVelocity(String vel){
		String[] values = vel.split(":");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		this.velocity = new Vector2(x,y);
		
	}
	public void setMass(String mass){
		this.mass = Integer.parseInt(mass);
		if(this.mass == 0 ) throw new ArithmeticException("Mass cannot be 0");
		
		this.invMass = 1 / this.mass;
	}
	@Override
	public Vector2 getPosition() {
		return position;
	}
	@Override
	public Vector2 getSize() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public RigidBody getRigidBody() {
		return body;
	}
	

}
