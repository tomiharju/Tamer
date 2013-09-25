package com.me.tamer.gameobjects.superclasses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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
	private float invMass;			// Precalculated invmass, used in physics calculations
	protected Vector2 size;
	private Renderer renderer;
	private boolean isCarbage = false;
	protected RigidBody body = null;
	
	
	@Override
	public void update(float dt) {
		position.add(velocity.mul(dt));
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
		this.renderer.setTarget(this);
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
		this.size = new Vector2(w,h);
		
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
		this.position = new Vector2(x,y);
	}
	public void setVelocity(String vel){
		String[] values = vel.split(":");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
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
	@Override
	public RigidBody getRigidBody() {
		return body;
	}
	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
	}
	public void setHeading(Vector2 heading){
		this.heading.set(heading);
	}
	

}
