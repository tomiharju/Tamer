package com.me.tamer.gameobjects.superclasses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.tamer.Tamer;
import com.me.tamer.physics.RigidBody;
import com.me.tamer.physics.RigidBodyBox;
import com.me.tamer.physics.RigidBodyCircle;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RendererFactory;

public class DynamicObject implements GameObject{
	
	//Someone has to fix these to private
	protected Vector2 position = new Vector2();		//"World position"
	protected Vector2 velocity = new Vector2();		//"World velocity"
	protected Vector2 heading = new Vector2();
	protected Vector2 force = new Vector2();		// Magnitude and direction of per loop position iteration
	protected Vector2 size = new Vector2();
	protected float angle = 0;
	protected float borderOffset = 0;
	



	private float mass;			
	
	protected String renderType;
	private boolean isCarbage = false;
	protected RigidBody body = null;
	private boolean debug = false;
	private int zIndex = 0;
	private Vector2 zeroHeading = new Vector2(-0.5f,1);// -1 + (float)Math.sin(Math.PI/8),1 + (float)Math.cos(Math.PI/8));//-0.5f, 2.0f);//;
	private double headingAngle;
	
	public DynamicObject(){
	}
	
	@Override
	public void update(float dt) {
		position.add(velocity.mul(dt));
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		Renderer renderer = RenderPool.getRenderer(renderType);
		renderer.setSize(size.x,size.y);
		renderer.setPosition(IsoHelper.twoDToTileIso(position));
		renderer.setOrientation( solveOrientation() );
		renderer.setAngle(angle);
		renderer.draw(batch);	
	}
	
	public int solveOrientation(){
		if(getHeading() != null){
			zeroHeading.nor();

			headingAngle = Math.acos(getHeading().dot(zeroHeading) / (getHeading().len() * zeroHeading.len()));
			
			headingAngle = headingAngle / Math.PI * 180 / 45;
			
			if (headingAngle == 0) headingAngle = 0.001f;
			if (heading.x > zeroHeading.x && heading.y > 0) headingAngle = 8 - headingAngle;
			else if (heading.x > -zeroHeading.x && heading.y < 0) headingAngle = 8 - headingAngle;
			
			headingAngle = Math.floor(headingAngle);
		}
		
		return (int)headingAngle;
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
		this.position.set(pos.cpy());
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
		if(bodytype.equalsIgnoreCase("box"))
			body = new RigidBodyBox(position,new Vector2(0,0),0,size.x,size.y); //Position, speed, mass, width,height ( speed and mass are 0 cause its static object )
		else if(bodytype.equalsIgnoreCase("circle"))
			body = new RigidBodyCircle(position,velocity,mass,size.x/2);//Position, velocity, mass, radii
		else if(bodytype.equalsIgnoreCase("no-body"))
			body = null;
		if(body != null)
			body.setOwner(this);
	}
	
	public void setMass(String mass){
		this.mass = Integer.parseInt(mass);
		if(this.mass == 0 ) throw new ArithmeticException("Mass cannot be 0");
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
	public RigidBody getRigidBody() {
		return body;
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
		heading.nor();
		this.heading.set(heading);
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

}
