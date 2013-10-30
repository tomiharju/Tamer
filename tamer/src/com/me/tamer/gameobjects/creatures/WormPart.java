package com.me.tamer.gameobjects.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.physics.RigidBodyCircle;

public class WormPart extends DynamicObject implements Creature {
	
	//Container worm
	private Worm worm = null;
	
	private float restLength = 0.7f;
	private float k  = 0.8f; //Stretch factor ( 0.8 is pretty high )
	private int ordinal = 0;
	private float speed = 10;
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
	Vector2 heading = new Vector2();
	
	public void createHead(Vector2 pos, Vector2 vel,Worm worm){
		this.worm = worm;
		setGraphics("wormhead");
		partName = "Head";
		radii = .25f;
		mass = 20;
		position = new Vector2(pos);
		velocity = new Vector2(vel);
		force = new Vector2(vel).mul(speed);
		body = new RigidBodyCircle(position,velocity,mass,radii);
		this.ordinal = 0;
	}
	
	public void createBodyPart(int ordinal,Vector2 pos, Vector2 vel,Worm worm){
		this.worm = worm;
		setGraphics("wormpart");
		partName = "Joint";
		radii = .25f;
		mass = 10;
		position = new Vector2(pos);
		position.add(vel.tmp().nor().mul(-ordinal*restLength));
		velocity = new Vector2(0,0);
		force = new Vector2(vel).mul(speed);
		//Set worm graphic size, add a little extra to avoid excess collision due to parts being in contact all the time
		body = new RigidBodyCircle(position,velocity,mass,radii);
		this.ordinal = ordinal;
	}
	
	public void setGraphics(String graphics){

		Renderer render = RenderPool.addRendererToPool("animated",graphics);
		render.loadGraphics(graphics,1,8);
		setSize(new Vector2(1,1));
		renderType = graphics;
	}
	
	
	public void unBind(){
		body.setInvMass( 1 / body.getInvMass());
	}

	public void attachToParent(WormPart parent){
		this.parent = parent;
		parent.attachToChild(this);
	}
	public void attachToChild(WormPart child){
		this.child = child;
	}
	public void solveJoints(float dt){
		if(child != null){
			solveJoint(dt);
			child.solveJoints(dt);
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
	
	public void setHeading(Vector2 newHeading){
		force.set(newHeading).mul(speed);
	}
	
	public void setAsTail(){
		isTail = true;
	}
	public boolean isTail(){
		return isTail;
	}
	
	
	@Override
	public void spearHit(Spear spear) {
	if(partName.equalsIgnoreCase("head")){
		killPart();
		markAsCarbage();
	}
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


	/* (non-Javadoc)
	 * @see com.me.tamer.gameobjects.superclasses.DynamicObject#dispose(com.me.tamer.gameobjects.Level)
	 */
	public void dispose(Environment environment){
		//TODO: play some death animations before actually disposing?
		environment.getCreatures().remove(this);
		worm.getParts().remove(this);
	}
	
	/**
	 * When a spear hits the worm in the head, use this recursive function to remove all the body parts.
	 */
	public void killPart(){
		if(child != null)
			child.killPart();
		markAsCarbage();
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
	
	public String getPartName(){
		return partName;
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public Vector2 getHeading(){
		heading.set(getVelocity().tmp().nor());
		return heading;
	}
	
	@Override
	public void setPosition(Vector2 pos) {
		this.position.set(pos);
		
	}

	@Override
	public Creature affectedCreature(Vector2 point, float radius) {
		if(this.position.dst(point) < radius)
			return this;
		else
			return null;
		
	}

	@Override
	public void applyPull(Vector2 point) {
		Vector2 pullVector = point.tmp().sub(position);
		pullVector.nor().mul(5.5f);
		velocity.add(pullVector.mul(Gdx.graphics.getDeltaTime())); 
	}

	@Override
	public boolean isAffected(Vector2 point, float radius) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
