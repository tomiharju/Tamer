package com.me.tamer.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.Interactable;
import com.me.tamer.physics.RigidBodyCircle;

public class WormPart extends DynamicObject implements Interactable {
	
	private float restLength = 1.3f;
	private float k  = 0.8f; //Stretch factor ( 0.8 is pretty high )
	private int ordinal = 0;
	private float speed = 0;
	private float radii = 0;
	private float mass = 0;
	//Chain related stuff
	private WormPart parent 	= null;
	private WormPart child 		= null;

	private String partName = null;
	//Physics optimization variables;
	Vector2 impulseA = new Vector2();
	Vector2 impulseB = new Vector2();
	Vector2 tempVector = new Vector2();
	
	
	public void createHead(Vector2 pos, Vector2 vel){
		setRenderer("static:wormhead");
		partName = "Head";
		radii = .25f;
		mass = 20;
		position = new Vector2(pos);
		velocity = new Vector2(vel);
		force = new Vector2(vel).mul(5);
		size = new Vector2(radii*2,radii*2);
		body = new RigidBodyCircle(position,velocity,mass,radii);
		this.ordinal = 0;
	}
	public void createBodyPart(int ordinal,Vector2 pos, Vector2 vel){
		setRenderer("static:wormpart");
		partName = "Joint";
		radii = .25f;
		mass = 10;
		position = new Vector2(pos);
		position.add(vel.tmp().nor().mul(-ordinal*restLength));
		velocity = new Vector2(0,0);
		force = new Vector2(0,0);
		size = new Vector2(radii*2,radii*2);
		body = new RigidBodyCircle(position,velocity,mass,radii);
		this.ordinal = ordinal;
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
	//		System.out.println("Part {"+getOrdinal() + " : " + partName+ "} calling child {"+child.getOrdinal()+" : "+child.getPartName() + "}");
			solveJoint(dt);
			child.solveForces(dt);
		}//else
		//	System.out.println("Hi, im the tail");
	}
	public void update(float dt){
		//Overidde this to do nothing.
	}
	public void updateChild(float dt){
		
		if(child != null && child.partName.equalsIgnoreCase("Joint"))
			child.updateChild(dt);
		
		position.add(velocity.tmp().mul(dt));
		velocity.mul(.9f);
	
		if(partName.equalsIgnoreCase("Head"))
			velocity.add(force.tmp().mul(dt));
	}
	
	public void solveJoint(float dt){
		
		Vector2 axis = new Vector2(child.position.tmp().sub(position));
		float currentDistance = axis.len();
		Vector2 unitAxis = axis.nor();

		Vector2 relativeVelocity = child.velocity.tmp().sub(velocity);
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
		Vector2 addA = impulse.cpy().mul(child.body.getInvMass());
		Vector2 addB = impulse.cpy().mul(body.getInvMass());
		child.velocity.sub(addA);
		velocity.add(addB);
	}
	@Override
	public void spearHit(Spear spear) {
		body.setInvMass(0);
		
	}
	@Override
	public void lassoHit(String lasso) {
		// TODO Auto-generated method stub
		
	}
	
	public int getOrdinal(){
		return ordinal;
	}
	public String getPartName(){
		return partName;
	}
	@Override
	public void unBind() {
		body.setInvMass( 1 / body.getMass());
	}
	
	
	
	

}
