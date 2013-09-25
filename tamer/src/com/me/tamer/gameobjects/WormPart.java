package com.me.tamer.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.physics.RigidBodyCircle;

public class WormPart extends DynamicObject {
	
	private int restLength = 15;
	private float k  = 0.8f; //Stretch factor ( 0.8 is pretty high )
	private int ordinal = 0;
	private float speed = 0;
	private float radii = 0;
	private float mass = 0;
	//Chain related stuff
	private WormPart parent 	= null;
	private WormPart child 		= null;
	private boolean isAttached = false;
	private String partName = null;
	
	
	public void createHead(){
		partName = "Head";
		radii = 0.5f;
		mass = 20;
		body = new RigidBodyCircle(position,getVelocity(),mass,radii);
	}
	public void createBodyPart(){
		partName = "Joint";
		radii = 0.5f;
		mass = 10;
		body = new RigidBodyCircle(position,getVelocity(),mass,radii);
	}
	
	public void bind(){
		body.setInvMass(0);
		isAttached = true;
	}
	public void unBind(){
		body.setInvMass( 1 / body.getInvMass());
	}
	public void setHeading(Vector2 heading){
		this.heading.set(heading);
	}
	public void attachToParent(WormPart parent){
		this.parent = parent;
		parent.attachToChild(this);
	}
	public void attachToChild(WormPart child){
		this.child = child;
	}
	public void solveForces(float dt){
		if(child != null){
			solveJoint(dt);
			child.solveForces(dt);
		}
	}
	public void update(float dt){
	/*	if(child != null && child.partName.equalsIgnoreCase("Join"))
			child.update(dt);
		
		position.add(velocity.cpy().mul(dt));
		velocity.mul(0.9f);
		if(partName.equalsIgnoreCase("Head"))
			velocity.add(force);
	*/}
	
	public void solveJoint(float dt){
		Vector2 axis = child.position.cpy().sub(position);
		float currentDistance = axis.len();
		Vector2 unitAxis = axis.nor();
		Vector2 relativeVelocity = child.getVelocity().cpy().sub(getVelocity());
		float relVelMagnitude = relativeVelocity.dot(unitAxis);
		float relativeDistance = (currentDistance - restLength);
		if( relativeDistance > 0){
			float impulse = 0;
			float remove = relVelMagnitude + relativeDistance / dt;
			if(body.getInvMass() == 0 && child.getRigidBody().getInvMass() == 0)
				impulse = 0;
			else
				impulse = remove / (body.getInvMass() + child.getRigidBody().getInvMass());
			impulse = impulse * k;
			Vector2 impulseVector = unitAxis.mul(impulse);
			applyImpulse(impulseVector);
				
			
			
		}
	}
	
	public void applyImpulse(Vector2 impulse){
		Vector2 addA = impulse.cpy().mul(child.getRigidBody().getInvMass());
		Vector2 addB = impulse.cpy().mul(body.getInvMass());
		child.getVelocity().sub(addA);
		getVelocity().add(addB);
	}
	
	
	
	

}
