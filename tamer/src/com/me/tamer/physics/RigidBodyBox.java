package com.me.tamer.physics;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.GameObject;

public class RigidBodyBox implements RigidBody{
	
	private DynamicObject owner = null;
	
	private Vector2 position = null;
	private Vector2 velocity = null;
	private float mass = 0;
	private float invMass = 0;
	private ArrayList<Vector2> vertices = null;
	private float width = 0;
	private float height = 0;

	public RigidBodyBox(Vector2 p, Vector2 v,float mass,float width, float height){
		this.position = p;
		this.velocity = v;
		this.mass = mass;
		if(this.mass == 0)
			this.invMass = 0;
		else
			this.invMass = 1 / this.mass;
		this.width = width;
		this.height = height;
		vertices = new ArrayList<Vector2>(4);
			Vector2 v1 = new Vector2((position.x - width / 2), (position.y - height / 2));
	        Vector2 v2 = new Vector2((position.x - width / 2), (position.y + height / 2));
	        Vector2 v3 = new Vector2((position.x + width / 2), (position.y + height / 2));
	        Vector2 v4 = new Vector2((position.x + width / 2), (position.y - height / 2));
	        vertices.add(v1);
	        vertices.add(v2);
	        vertices.add(v3);
	        vertices.add(v4);
	}

	@Override
	public boolean isDynamic() {
		return this.invMass > 0 ;
	}

	@Override
	public Contact generateContact(RigidBody body) {
		if(body instanceof RigidBodyBox){
			float overlap = -10000;
			Vector2 normal = null;
			ArrayList<Vector2> axes1 = this.getAxes();
			ArrayList<Vector2> axes2 = body.getAxes();
			
			for(int i = 0 ; i < axes1.size() ; i++){
				Vector2 axis = axes1.get(i);
				Vector2 projection1 = this.project(axis);
				Vector2 projection2 = body.project(axis);
				
				float o = this.getOverlap(projection1,projection2);
				if( o > overlap ){
					overlap = o;
					normal = axis;
				}
			}
			
			for(int i = 0 ; i < axes2.size() ; i++){
				Vector2 axis = axes2.get(i);
				Vector2 projection1 = this.project(axis);
				Vector2 projection2 = body.project(axis);
				
				float o = this.getOverlap(projection1,projection2);
				if( o > overlap ){
					overlap = o;
					normal = axis;
				}
			}
			
			return new Contact(normal,overlap,this,body);
		}
		if(body instanceof RigidBodyLine){
			
			float overlap = -10000;
			Vector2 normal = null;
			ArrayList<Vector2> axes1 = this.getAxes();
			ArrayList<Vector2> axes2 = body.getAxes();
			
			for(int i = 0 ; i < axes1.size() ; i++){
				Vector2 axis = axes1.get(i);
				Vector2 projection1 = this.project(axis);
				Vector2 projection2 = body.project(axis);
				
				float o = this.getOverlap(projection1,projection2);
				if( o > overlap ){
					overlap = o;
					normal = axis;
				}
			}
			
			for(int i = 0 ; i < axes2.size() ; i++){
				Vector2 axis = axes2.get(i);
				Vector2 projection1 = this.project(axis);
				Vector2 projection2 = body.project(axis);
				
				float o = this.getOverlap(projection1,projection2);
				if( o > overlap ){
					overlap = o;
					normal = axis;
				}
			}
			
			return new Contact(normal,overlap,this,body);
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Vector2> getAxes() {
		ArrayList<Vector2> axes = new ArrayList<Vector2>();
		for(int i = 0 ; i < this.vertices.size() ; i++){
			Vector2 p1 = this.vertices.get(i);
			Vector2 p2 = this.vertices.get(i + 1 == this.vertices.size() ? 0 : i + 1);
			Vector2 edge = p1.cpy().sub(p2);
			Vector2 normal = edge.rotate(-90);
			normal.nor();
			axes.add(normal);
		}
		return axes;
	}

	@Override
	public Vector2 project(Vector2 axis) {
		float min = axis.dot(this.vertices.get(0));
		float max = min;
		
		for(int i = 0 ; i < this.vertices.size() ; i++){
			float p = axis.dot(this.vertices.get(i));
			if( p < min)
				min = p;
			else if( p > max )
				max = p;
		}
		return new Vector2(min,max);
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
		float mindist = 10000;
		Vector2 vertice = point;
		
		for(int i = 0 ; i < this.vertices.size(); i ++){
			float dist = this.vertices.get(i).dst(point);
			if ( dist < mindist){
				mindist = dist;
				vertice = this.vertices.get(i);
			}
		}
		return vertice;
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
}
