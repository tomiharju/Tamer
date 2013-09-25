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
	private ArrayList<Vector2> vertices = null;
	private float radii = 0;
	
	
	public RigidBodyCircle(Vector2 p, Vector2 v, float mass, float radii){
		this.position = p;
		this.velocity = v;
		this.mass = mass ;
		if(this.mass == 0)
			this.invMass = 0;
		else
			this.invMass = 1 / this.mass;
		this.radii = radii;
		vertices = new ArrayList<Vector2>(1);
		vertices.add(this.position);
	}
	@Override
	public boolean isDynamic() {
		return this.invMass > 0 ;
	}

	@Override
	public Contact generateContact(RigidBody body) {
		if(body instanceof RigidBodyCircle){
			float overlap = -100000;
			Vector2 normal = null;
			Vector2 axis = body.getPosition().cpy().sub(position);
			axis.nor();
			Vector2 projection1 = this.project(axis);
			Vector2 projection2 = body.project(axis);
			
			float o = this.getOverlap(projection1,projection2);
			if( o > overlap ){
				overlap = o;
				normal = axis;
			}
			
			return new Contact(normal,overlap,this,body);
		}
		
		if(body instanceof RigidBodyBox){
			float overlap = -10000;
			Vector2 normal = null;
			ArrayList<Vector2> axes2 = body.getAxes();
			
			Vector2 closest = body.getClosestVertice(this.position);
			Vector2 axis1 = closest.cpy().sub(this.position);
			axis1.nor();
			
			Vector2 projection1 = this.project(axis1);
			Vector2 projection2 = body.project(axis1);
			
			float o = this.getOverlap(projection1, projection2);
			if( o > overlap){
				overlap = o;
				normal = axis1;
			}
			for( int i = 0 ; i < axes2.size() ; i ++){
				Vector2 axis = axes2.get(i);
				projection1 = this.project(axis);
				projection2 = body.project(axis);
				
				o = this.getOverlap(projection1, projection2);
				if( o > overlap){
					overlap = o ;
					normal = axis;
				
				}
				
				
			}
			
			
			return new Contact(normal,overlap,this,body);
			
		}
		
		if(body instanceof RigidBodyLine){
			
			float overlap = -10000;
			Vector2 normal = null;
			
			Vector2 closest = body.getClosestVertice(position);
			Vector2 circleToClosest = closest.cpy().sub(position);
			circleToClosest.nor();
			
			ArrayList<Vector2> lineaxes = body.getAxes();

			Vector2 projection1 = this.project(circleToClosest);
			Vector2 projection2 = body.project(circleToClosest);
			
			float o = this.getOverlap(projection1, projection2);
			
			if(o > overlap){
				overlap = o;
				normal = circleToClosest;
			}
			
			
			for(int i = 0 ; i < lineaxes.size() ; i ++){
				Vector2 axis = lineaxes.get(i);
				projection1 = this.project(axis);
				projection2 = body.project(axis);
				
				o = this.getOverlap(projection1, projection2);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2 project(Vector2 axis) {
		float center = axis.dot(position);
	    float min = center - radii;
	    float max = center + radii;
	    return new Vector2(min, max);
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
}
