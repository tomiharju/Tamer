package com.me.tamer.gameobjects.superclasses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.utils.IsoHelper;

public class DynamicObject implements GameObject{
	
	//Someone has to fix these to private
	private Vector2 position 	= new Vector2();		// "World position"
	private Vector2 velocity 	= new Vector2();		// "World velocity"
	private Vector2 heading 	= new Vector2();		// Unit vector of current velocity
	private Vector2 force 		= new Vector2();		// Magnitude and direction of per loop velocity increment
	private Vector2 size		= new Vector2();		// Graphics sprite size
	private float angle 		= 0;	
	private String renderType	= null;					// Graphics name, used for fetching correct renderer for object
	private boolean isCarbage 	= false;				// Setting to true, causes carbage collection loop to remove this object from game
	private boolean debug	 	= false;
	private int zIndex 			= 0;					// Forced drawing order 
	private Vector2 isoHeading 	= new Vector2();		//Used for determining the sprite
	protected float borderOffset = 0;
	private Vector2 zeroHeading = new Vector2(-0.5f,1);// -1 + (float)Math.sin(Math.PI/8),1 + (float)Math.cos(Math.PI/8));//-0.5f, 2.0f);//;
	private float headingAngle = 0;
	
	
	@Override
	public void update(float dt) {
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		Renderer renderer = RenderPool.getRenderer(getRenderType());
		renderer.setSize(getSize());
		renderer.setPosition(IsoHelper.twoDToTileIso(position));
		renderer.setOrientation( solveOrientation() );
		renderer.setAngle(getAngle());
		renderer.draw(batch);	
	}
	
	public int solveOrientation(){
		if(getHeading() != null){
			getZeroHeading().nor();

			setHeadingAngle((float) Math.acos(getHeading().dot(getZeroHeading()) / (getHeading().len() * getZeroHeading().len())));
			
			setHeadingAngle((float) (getHeadingAngle() / Math.PI * 180 / 45));
			
			if (getHeadingAngle() == 0) setHeadingAngle(0.001f);
			if (heading.x > getZeroHeading().x && heading.y > 0) setHeadingAngle(8 - getHeadingAngle());
			else if (heading.x > -getZeroHeading().x && heading.y < 0) setHeadingAngle(8 - getHeadingAngle());
			
			setHeadingAngle((float) Math.floor(getHeadingAngle()));
		}
		
		return (int)getHeadingAngle();
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
		this.position.set(x,y);
	}
	
	@Override
	public void setPosition(Vector2 pos){
		this.position.set(pos);
	}
	
	public void setVelocity(String vel){
		String[] values = vel.split(":");
		float x = Float.parseFloat(values[0]);
		float y = Float.parseFloat(values[1]);
		this.velocity.set(x,y);
		
	}
	
	public void setForce(String force){
		String[] values = force.split(":");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		this.force.set(x,y);
	}
	
	public void setRigidBody(String bodytype){
		/*
		if(bodytype.equalsIgnoreCase("box"))
			body = new RigidBodyBox(position,new Vector2(0,0),0,size.x,size.y); //Position, speed, mass, width,height ( speed and mass are 0 cause its static object )
		else if(bodytype.equalsIgnoreCase("circle"))
			body = new RigidBodyCircle(position,velocity,mass,size.x/2);//Position, velocity, mass, radii
		else if(bodytype.equalsIgnoreCase("no-body"))
			body = null;
		if(body != null)
			body.setOwner(this);
			*/
	}
	
	
	
	@Override
	public Vector2 getPosition() {
		return position;
	}
	
	@Override
	public Vector2 getSize() {
		return size;
	}
	
	public Vector2 getHeading(){
		return heading;
	}
	
	
	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setup(Environment level) {
		// TODO Auto-generated method stub
		
	}
	
	public void setHeading(Vector2 heading){
		this.heading.set(heading);
		this.heading.nor();
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector2 velocity) {
		this.velocity.set(velocity);
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
	public void markAsActive() {
		isCarbage = false;
		
	}
	@Override
	public void dispose(Environment level) {
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
	
	public Vector2 getForce(){
		return force;
	}
	
	public float getBorderOffset() {
		return borderOffset;
	}

	public void setBorderOffset(float borderOffset) {
		this.borderOffset = borderOffset;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}

	public void setForce(Vector2 force) {
		this.force.set(force);
	}

	public String getRenderType() {
		return renderType;
	}

	public void setRenderType(String renderType) {
		this.renderType = renderType;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	@Override
	public void setSize(float x, float y) {
		this.size.set(x,y);
		
	}

	public Vector2 getZeroHeading() {
		return zeroHeading;
	}

	public void setZeroHeading(Vector2 zeroHeading) {
		this.zeroHeading = zeroHeading;
	}

	public float getHeadingAngle() {
		return headingAngle;
	}

	public void setHeadingAngle(float headingAngle) {
		this.headingAngle = headingAngle;
	}

}
