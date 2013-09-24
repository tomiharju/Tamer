package com.me.tamer.physics;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.GameObject;

public interface RigidBody {
	public boolean isDynamic();
	public void setMass();
	public Contact generateContact(RigidBody body);
	public ArrayList<Vector2> getAxes();
	public Vector2 project(Vector2 axis);
	public float getOverlap(Vector2 a, Vector2 b);
	public Vector2 getPosition();
	public Vector2 getVelocity();
	public float getInvMass();
	public Vector2 getClosestVertice(Vector2 point);
	public void setOwner(DynamicObject obj);
	public DynamicObject getOwner();
	


}
