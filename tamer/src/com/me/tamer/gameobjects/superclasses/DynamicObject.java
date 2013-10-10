package com.me.tamer.gameobjects.superclasses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Level;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.physics.RigidBody;
import com.me.tamer.physics.RigidBodyBox;
import com.me.tamer.physics.RigidBodyCircle;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RendererFactory;

public class DynamicObject implements GameObject{
	
	protected Vector2 position;		//"Mathematical position"
	protected Vector2 velocity;		//"Mathmematical velocity"
	protected Vector2 heading;
	protected Vector2 force;			// Magnitude and direction of per loop position iteration
	private float mass;			
	private float invMass;				// Precalculated invmass, used in physics calculations
	protected Vector2 size;
	private String renderType;
	private boolean isCarbage = false;
	protected RigidBody body = null;
	private boolean debug = false;
	
	
	@Override
	public void update(float dt) {
		position.add(velocity.mul(dt));
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		Renderer renderer = RenderPool.getRenderer(renderType);
		renderer.setSize(size.x,size.y);
		renderer.setPosition(IsoHelper.twoDToIso(position));
		renderer.setOrientation(0);
		renderer.draw(batch);
		
	}
	
	
	@Override
	public void setRenderer(String renderinfo) {
		String[] info = renderinfo.split(":");
		RenderPool.addRendererToPool(info[0],info[1]);
		this.renderType = info[1];
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
	public void setSize(String size) {
		String[] values = size.split(":");
		float w = Float.parseFloat(values[0]);
		float h = Float.parseFloat(values[1]);
		this.size = new Vector2(w,h);
		
	}
	
	@Override
	public void setPosition(String pos) {
		String[] values = pos.split(":");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		this.position = new Vector2(x,y);
	}
	
	public void setVelocity(String vel){
		String[] values = vel.split(":");
		float x = Float.parseFloat(values[0]);
		float y = Float.parseFloat(values[1]);
		this.velocity = new Vector2(x,y);
		
	}
	
	public void setForce(String force){
		String[] values = force.split(":");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		this.force = new Vector2(x,y);
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
		// TODO Auto-generated method stub
		return null;
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
	public void setup() {
		// TODO Auto-generated method stub
		
	}
	
	public void setHeading(Vector2 heading){
		this.heading.set(heading);
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
	
	@Override
	public void resolveForces(float dt) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void wakeUp(Level level) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void markAsActive() {
		isCarbage = false;
		
	}
	@Override
	public void dispose(Level level) {
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
	

	

	

}
