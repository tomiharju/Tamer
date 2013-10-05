package com.me.tamer.physics;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.GameObject;

public class RigidBodyCircle implements RigidBody {

	private DynamicObject owner = null;
	
	private Vector2 position = null;
	private Vector2 velocity = null;
	private float mass = 0;
	private float invMass = 0;
	private Vector2 vertice = null;
	private float radii = 0;
	

	//Preloaded variables for calculations
	ArrayList<Vector2> axes1 ;
	ArrayList<Vector2> axes2 ;
	Vector2 normal;
	Vector2 axis;
	Vector2 closest ;
	Vector2 circleToClosest ;
	Vector2 projection1 ;
	Vector2 projection2 ;
	Vector2 bodyPosition;
	//Helper function variables
	ArrayList<Vector2> axes ;
	Vector2 projectResult ;
	
	
	public RigidBodyCircle(Vector2 p, Vector2 v, float mass, float radii){
		this.position = p;
		this.velocity = v;
		this.mass = mass ;
		if(this.mass == 0)
			this.invMass = 0;
		else
			this.invMass = 1 / this.mass;
		this.radii = radii;
		vertice = new Vector2(p);
		
		//Precalculate values for optimization
		preCalculateValues();

	}
	@Override
	public boolean isDynamic() {
		return this.invMass > 0 ;
	}

	@Override
	public Contact generateContact(RigidBody body) {
		if(body instanceof RigidBodyCircle){
			if(position.dst(body.getPosition() ) > radii + body.getRadii() + 2f)
				return null;
			
			float overlap = -100000f;
		
			axis.set(body.getPosition().tmp().sub(position));
			axis.nor();
			projection1.set(project(axis));
			projection2.set(body.project(axis));
		
			float o = getOverlap(projection1,projection2);
			
			if( o > overlap ){
				overlap = o;
				normal.set(axis);
			}
			
			return new Contact(normal,overlap,this,body);
			
		}
		
		if(body instanceof RigidBodyBox){
			float overlap = -10000f;
			axes2 = body.getAxes();
			
			closest.set(body.getClosestVertice(position));
			axis.set(closest.cpy().sub(position));
			axis.nor();
			
			projection1.set(this.project(axis));
			projection2.set(body.project(axis));
			
			float o = getOverlap(projection1, projection2);
			if( o > overlap){
				overlap = o;
				normal.set(axis);
			}
			for( int i = 0 ; i < axes2.size() ; i ++){
				axis.set(axes2.get(i));
				projection1.set(project(axis));
				projection2.set(body.project(axis));
				
				o = getOverlap(projection1, projection2);
				if( o > overlap){
					overlap = o ;
					normal.set(axis);
				
				}
				
				
			}
			
			
			return new Contact(normal,overlap,this,body);
			
		}
		
		if(body instanceof RigidBodyLine){
			
			float overlap = -10000;		
			closest = body.getClosestVertice(position);
			circleToClosest = closest.cpy().sub(position);
			circleToClosest.nor();
			
			ArrayList<Vector2> lineaxes = body.getAxes();

			projection1 = this.project(circleToClosest);
			projection2 = body.project(circleToClosest);
			
			float o = getOverlap(projection1, projection2);
			
			if(o > overlap){
				overlap = o;
				normal = circleToClosest;
			}
			
			
			for(int i = 0 ; i < lineaxes.size() ; i ++){
				axis = lineaxes.get(i);
				projection1 = this.project(axis);
				projection2 = body.project(axis);
				
				o = getOverlap(projection1, projection2);
				if( o > overlap){
					overlap = o;
					normal = axis;
				}
				
				
			}
			return new Contact(normal, overlap, this, body);
		}
		
		
		return null;
		
	}

	@Override
	public ArrayList<Vector2> getAxes() {
		
		return null;
	}

	@Override
	public Vector2 project(Vector2 axis) {
		float center = axis.dot(position);
	    float min = center - radii;
	    float max = center + radii;
	    projectResult.set(min,max);
	    return projectResult;
	  
	}

	@Override
	public float getOverlap(Vector2 a, Vector2 b) {
		float max0 = a.y;
		float min1 = b.x;
		return min1-max0;
	}

	@Override
	public void setMass() {
		// TODO Auto-generated method stub
		
	}
	public Vector2 getPosition(){
		return position;
	}

	@Override
	public Vector2 getClosestVertice(Vector2 point) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Vector2 getVelocity() {
		return velocity;
	}
	@Override
	public float getInvMass(){
		return invMass;
	}
	public float getRadii(){
		return radii;
	}
	@Override
	public void setOwner(DynamicObject obj) {
		this.owner = obj;
		
	}
	@Override
	public DynamicObject getOwner() {
		return owner;
	}

	@Override
	public void setInvMass(float ivm) {
		invMass=ivm;
		
	}
	@Override
	public void preCalculateValues() {
		axes1 = new ArrayList<Vector2>();
		axes2 = new ArrayList<Vector2>();
		normal = new Vector2();
		axis = new Vector2();
		closest = new Vector2();
		circleToClosest = new Vector2();
		projection1 = new Vector2();
		projection2 = new Vector2();
		//Helper function variables
		axes = new ArrayList<Vector2>();
		projectResult = new Vector2();
		
	}
	@Override
	public float getMass() {
		return mass;
	}
}
