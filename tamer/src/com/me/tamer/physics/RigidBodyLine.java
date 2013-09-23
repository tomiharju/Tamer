package com.me.tamer.physics;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class RigidBodyLine implements RigidBody {

	private Vector2 position = null;
	private Vector2 velocity = null;
	private float mass;
	private float invMass;
	@Override
	public boolean isDynamic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setMass() {
		// TODO Auto-generated method stub

	}

	@Override
	public Contact generateContact(RigidBody body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Vector2> getAxes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2 project(Vector2 axis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getOverlap(Vector2 a, Vector2 b) {
		// TODO Auto-generated method stub
		return 0;
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

}
