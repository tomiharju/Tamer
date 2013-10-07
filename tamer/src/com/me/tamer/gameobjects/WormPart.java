package com.me.tamer.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.Interactable;
import com.me.tamer.physics.RigidBodyCircle;

public class WormPart extends DynamicObject implements Interactable {
	
	//Container worm
	private Worm worm = null;
	
	private float restLength = 1.3f;
	private float k  = 0.8f; //Stretch factor ( 0.8 is pretty high )
	private int ordinal = 0;
	private float speed = 5;
	private float radii = 0;
	private float mass = 0;
	//Chain related stuff
	private WormPart parent 	= null;
	private WormPart child 		= null;
	private boolean isTail		= false;
	private String partName = null;
	//Physics optimization variables;
	Vector2 impulseA = new Vector2();
	Vector2 impulseB = new Vector2();
	Vector2 axis = new Vector2();
	Vector2 relativeVelocity = new Vector2();
	
	
	public void createHead(Vector2 pos, Vector2 vel,Worm worm){
		setRenderer("animated:wormhead");
		this.worm = worm;
		partName = "Head";
		radii = .25f;
		mass = 20;
		position = new Vector2(pos);
		velocity = new Vector2(vel);
		force = new Vector2(vel).mul(speed);
		size = new Vector2(radii*2,radii*2);
		body = new RigidBodyCircle(position,velocity,mass,radii);
		this.ordinal = 0;
	}
	public void createBodyPart(int ordinal,Vector2 pos, Vector2 vel,Worm worm){
		this.worm = worm;
		setRenderer("animated:wormpart");
		partName = "Joint";
		radii = .25f;
		mass = 10;
		position = new Vector2(pos);
		position.add(vel.tmp().nor().mul(-ordinal*restLength));
		velocity = new Vector2(0,0);
		force = new Vector2(vel).mul(speed);
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
		
		position.add(velocity.tmp().mul(dt));
		velocity.mul(.9f);
	
		if(partName.equalsIgnoreCase("Head"))
			velocity.add(force.tmp().mul(dt));
	}
	
	public void solveJoint(float dt){
		
		axis.set(child.position.tmp().sub(position));
		float currentDistance = axis.len();
		Vector2 unitAxis = axis.nor();

		relativeVelocity.set(child.velocity.tmp().sub(velocity));
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
		Vector2 addA = impulse.tmp().mul(child.body.getInvMass());
		child.velocity.sub(addA);
		Vector2 addB = impulse.tmp().mul(body.getInvMass());
		velocity.add(addB);
	}
	
	public void setAsTail(){
		isTail = true;
	}
	public boolean isTail(){
		return isTail;
	}
	
	
	@Override
	public void spearHit(Spear spear) {
	if(partName.equalsIgnoreCase("head"))
		markAsCarbage();
	else
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
	
	/* (non-Javadoc)
	 * @see com.me.tamer.gameobjects.superclasses.DynamicObject#dispose(com.me.tamer.gameobjects.Level)
	 */
	public void dispose(Level level){
		//TODO: play some death animations before actually disposing?
		level.getCreatures().remove(this);
		level.getRigidBodies().remove(this.body);
		worm.getParts().remove(this);
	}
	/* 
	 * This method is called by quicksandAction after timer fires. 
	 * Its used to kill the current tail part, and set the next part in chain as tail.
	 * 
	 */
	@Override
	public void kill() {
		if(parent != null){
			parent.child = null;
			parent.setAsTail();
		}
		markAsCarbage();
	}
	@Override
	public void moveToFinish() {
		if(child  != null){
			child.force.set(force);
			worm.setHead(child);
			child.partName = "head";
		}else
			worm.markAsCarbage();
		
		markAsCarbage();
		
	}
	
	
	
	
	

}
