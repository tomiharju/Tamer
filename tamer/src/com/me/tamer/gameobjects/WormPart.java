package com.me.tamer.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.physics.RigidBodyCircle;

public class WormPart extends DynamicObject {
	
	private float restLength = 1.5f;
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
	//Physics optimization variables;
	Vector2 impulseA = new Vector2();
	Vector2 impulseB = new Vector2();
	Vector2 tempVector = new Vector2();
	
	
	public void createHead(Vector2 pos, Vector2 vel){
		setRenderer("static:wormhead");
		partName = "Head";
		radii = .5f;
		mass = 20;
		position = new Vector2(pos);
		velocity = new Vector2(vel);
		force = new Vector2(vel);
		size = new Vector2(radii,radii);
		body = new RigidBodyCircle(position,velocity,mass,radii);
	}
	public void createBodyPart(int ordinal,Vector2 pos, Vector2 vel){
		setRenderer("static:wormpart");
		partName = "Joint";
		radii = .5f;
		mass = 10;
		position = new Vector2(pos);
		position.add(vel.cpy().nor().mul(-ordinal*restLength));
		velocity = new Vector2(0,0);
		force = new Vector2(0,0);
		size = new Vector2(radii,radii);
		body = new RigidBodyCircle(position,velocity,mass,radii);
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
		//Overidde this to do nothing.
	}
	public void updateChild(float dt){
		if(child != null && child.partName.equalsIgnoreCase("Joint"))
			child.updateChild(dt);
		
		tempVector.set(velocity);
		position.add(tempVector.mul(dt));
		velocity.mul(0);
		if(partName.equalsIgnoreCase("Head"))
			velocity.add(force);
	}
	
	public void solveJoint(float dt){
		tempVector.set(child.position);
		Vector2 axis = tempVector.sub(position);
		float currentDistance = axis.len();
		Vector2 unitAxis = axis.nor();
		tempVector.set(child.getVelocity());
		Vector2 relativeVelocity = tempVector.sub(velocity);
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
		tempVector.set(impulse);
		Vector2 addA = tempVector.mul(child.getRigidBody().getInvMass());
		tempVector.set(impulse);
		Vector2 addB = tempVector.mul(body.getInvMass());
		child.getVelocity().sub(addA);
		velocity.add(addB);
	}
	
	
	
	

}
