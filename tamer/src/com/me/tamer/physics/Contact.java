package com.me.tamer.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.me.tamer.gameobjects.superclasses.GameObject;

public class Contact implements Poolable{
	
	private Vector2 N;
	private float dist;
	private RigidBody objA;
	private RigidBody objB;
	private float impulse;
	
	
	public Vector2 getN() {
		return N;
	}

	public void setN(Vector2 n) {
		N = n;
	}

	public float getDist() {
		return dist;
	}

	public void setDist(float dist) {
		this.dist = dist;
	}

	public RigidBody getObjA() {
		return objA;
	}

	public void setObjA(RigidBody objA) {
		this.objA = objA;
	}

	public RigidBody getObjB() {
		return objB;
	}

	public void setObjB(RigidBody objB) {
		this.objB = objB;
	}

	public float getImpulse() {
		return impulse;
	}

	public void setImpulse(float impulse) {
		this.impulse = impulse;
	}


	
	public Contact(Vector2 normal, float dist, RigidBody a, RigidBody b){
		this.N = new Vector2(normal.x,normal.y);
		this.dist = dist;
		this.objA = a;
		this.objB = b;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
