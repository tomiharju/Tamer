package com.me.tamer.gameobjects.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.tamer.Spear;

public class WormPart extends DynamicObject implements Creature {
	
	//Container worm
	private Worm worm = null;
	
	private float JOINT_LENGTH = 0.7f;
	private float MIN_LENGTH   = 0.7f;
	private float lengthAngle  = 0;
	private int ordinal;
	private final float SPEED = 5;
	private float invMass;
	private float mass;
	//Chain related stuff
	private WormPart parent 	= null;
	private WormPart child 		= null;
	private boolean isTail		= false;
	private String partName 	= null;
	//Physics optimization variables;
	Vector2 impulseA 			= new Vector2();
	Vector2 impulseB 			= new Vector2();
	Vector2 axis 				= new Vector2();
	Vector2 relativeVelocity 	= new Vector2();
	Vector2 orientationVector	= new Vector2();
	
	public void createHead(Vector2 pos, Vector2 vel,Worm worm){
		this.worm 			= worm;
		setGraphics("wormhead");
		partName 			= "Head";
		mass 				= 30;
		invMass				= 1 / mass;
		setPosition(pos);
		setVelocity(vel);
		setForce(new Vector2(vel).mul(SPEED));
		setHeading(vel);
		this.ordinal 		= 0;
	}
	
	public void createBodyPart(int ordinal,Vector2 pos, Vector2 vel,Worm worm){
		this.worm 			= worm;
		setGraphics("wormpart");
		partName 			= "Joint";
		mass 				= 10;
		invMass				= 1 / mass;
		setPosition(pos);
		getPosition().add(vel.tmp().nor().mul( -ordinal * JOINT_LENGTH));
		setVelocity(new Vector2(0,0));
		setForce(new Vector2(0,0));
		this.ordinal 		= ordinal;
	}
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("animated",graphics);
		render.loadGraphics(graphics,1,8);
		setSize(1,1);
		setRenderType(graphics);
	}
	
	
	public void unBind(){
		invMass = 1 / mass;
	}

	public void attachToParent(WormPart parent){
		this.parent = parent;
		parent.attachToChild(this);
	}
	public void attachToChild(WormPart child){
		this.child = child;
	}
	public void solveJoints(float dt){
		lengthAngle += dt;
		JOINT_LENGTH = MIN_LENGTH + Math.abs((float) Math.sin(lengthAngle)) * 0.3f;
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
		
		getPosition().add(getVelocity().tmp().mul(dt));
		getVelocity().mul(.9f);
	
	}
	
	public void solveJoint(float dt){
		
		axis.set(child.getPosition().tmp().sub(getPosition()));
		float currentDistance 	= axis.len();
		Vector2 unitAxis 		= axis.nor();

		relativeVelocity.set(child.getVelocity().tmp().sub(getVelocity()));
		float relVelMagnitude 	= relativeVelocity.dot(unitAxis);
		float relativeDistance 	= (currentDistance - JOINT_LENGTH);
		
		if( relativeDistance > 0){
			float impulse 	= 0;
			float remove 	= relVelMagnitude + relativeDistance / dt;
			if(invMass == 0 && child.getInvMass() == 0)
				impulse 	= 0;
			else
				impulse 	= remove / (invMass + child.getInvMass());
			
			applyImpulse(unitAxis.mul(impulse));
		}
	}
	
	public int solveOrientation(){
		
			orientationVector.set(getOrientationDirection());
			getZeroHeading().nor();
			float angle = (float) Math.acos(orientationVector.dot(getZeroHeading().tmp().set(1,0)) / (orientationVector.len() * getZeroHeading().len()));
			angle = (float) Math.toDegrees(angle);
		//	setAngle(angle);
			setHeadingAngle((float) Math.acos(orientationVector.dot(getZeroHeading()) / (orientationVector.len() * getZeroHeading().len())));
			
			setHeadingAngle((float) (getHeadingAngle() / Math.PI * 180 / 45));
			
			if (getHeadingAngle() == 0) setHeadingAngle(0.001f);
			if (orientationVector.x > getZeroHeading().x && orientationVector.y > 0) setHeadingAngle(8 - getHeadingAngle());
			else if (orientationVector.x > -getZeroHeading().x && orientationVector.y < 0) setHeadingAngle(8 - getHeadingAngle());
			
			setHeadingAngle((float) Math.floor(getHeadingAngle()));
		
		return (int)getHeadingAngle();
		
	}
	
	public Vector2 getOrientationDirection(){
		if(this.partName.equalsIgnoreCase("head"))
			return getForce();
		else{
			if(child != null)
				return child.getPosition().tmp().sub(getPosition()); 
			else
				return getPosition().tmp().sub(parent.getPosition());
		}
		
		
	}
	
	private float getInvMass() {
		return invMass;
	}

	public void applyImpulse(Vector2 impulse){
		Vector2 addA = impulse.tmp().mul(child.getInvMass());
		child.getVelocity().sub(addA);
		Vector2 addB = impulse.tmp().mul(invMass);
		getVelocity().add(addB);
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
		else{
			invMass = 0;
		}
		
	}
	@Override
	public void lassoHit(String lasso) {
		// TODO Auto-generated method stub
		
	}
	
	public int getOrdinal(){
		return ordinal;
	}
	public float getSpeed(){
		return SPEED;
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
	public void moveToPoint(Vector2 point) {
		setForce(point.tmp().sub(getPosition()));
		setHeading(getForce());
		if(child  != null){
			child.setForce(getForce());
			child.setHeading(getForce());
			worm.setHead(child);
			child.partName = "head";
		}else
			worm.markAsCarbage();
		
		markAsCarbage();
		
	}
	
	public String getPartName(){
		return partName;
	}
	

	@Override
	public Creature affectedCreature(Vector2 point, float radius) {
		if(getPosition().dst(point) < radius)
			return this;
		else
			return null;
		
	}

	@Override
	public void applyPull(Vector2 point) {
		Vector2 pullVector = point.tmp().sub(getPosition());
		pullVector.nor().mul(5.5f);
		getVelocity().add(pullVector.mul(Gdx.graphics.getDeltaTime())); 
		if(getPosition().dst(point) < 0.05f)
			moveToPoint(point);
	}

	@Override
	public boolean isAffected(Vector2 point, float radius) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
