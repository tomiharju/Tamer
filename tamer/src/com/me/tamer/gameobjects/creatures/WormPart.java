package com.me.tamer.gameobjects.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.physics.RigidBodyBox;
import com.me.tamer.utils.Helper;

public class WormPart extends DynamicObject implements Creature {
	//Container worm
	private Worm worm = null;
	private final float MIN_LENGTH   = 0.15f;
	private final float STRETCH_AMOUNT = 0.20f;
	private final float HEAD_POS_FIX = 0.05f;
	private float joint_length = 0.4f;
	private float lengthAngle  = 0;
	private int ordinal;
	private float invMass;
	private float mass;
	private Vector2 help 		= new Vector2();
	
	//Effect variables
	private boolean onSpearRange = false;
	private boolean blinking = false;

	//Chain related stuff
	private WormPart parent 	= null;
	private WormPart child 		= null;
	private boolean isTail		= false;
	private String partName 	= null;
	
	//Physics optimization variables;
	private RigidBodyBox body	= null;
	Vector2 impulseA 			= new Vector2();
	Vector2 impulseB 			= new Vector2();
	Vector2 axis 				= new Vector2();
	Vector2 relativeVelocity 	= new Vector2();
	Vector2 orientationVector	= new Vector2();
	Vector2 temp 				= new Vector2();
	
	public void createHead(Vector2 pos, Vector2 vel,Worm worm){
		this.worm 			= worm;
		setGraphics("wormhead");
		partName 			= "Head";
		mass 				= 30;
		invMass				= 1 / mass;
		setPosition(pos);
		setVelocity(vel);
		setForce(new Vector2(vel).mul(worm.getSPEED()));
		setHeading(vel);
		body = new RigidBodyBox(getPosition(),getVelocity(),10,1,1);
		this.ordinal 		= 0;
	}
	
	public void createBodyPart(int ordinal,Vector2 pos, Vector2 vel,Worm worm){
		this.worm 			= worm;
		setGraphics("wormpart");
		partName 			= "Joint";
		mass 				= 10;
		invMass				= 1 / mass;
		setPosition(pos);
		getPosition().add(vel.tmp().nor().mul( -ordinal * joint_length));
		setVelocity(new Vector2(0,0));
		setForce(new Vector2(0,0));
		this.ordinal 		= ordinal;

	}
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("animated",graphics);
		render.loadGraphics(graphics,1,8);
		setSize(1,1f);
		setRenderType(graphics);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		Renderer renderer = RenderPool.getRenderer(getRenderType());
		
		if (onSpearRange)batch.setColor(0.1f, 1, 0.1f, 1.0f);
		else if (blinking)batch.setColor(0.1f,0.1f,1.0f,1.0f);
			
		renderer.setSize(getSize());
		
		//Fix position of the headpart
		if(partName.equalsIgnoreCase("head")){
			help.set(Helper.worldToScreen(getPosition()));
			help.y += HEAD_POS_FIX;
			renderer.setPosition(help);
		}
		else renderer.setPosition(Helper.worldToScreen(getPosition()));
		
		
		renderer.setOrientation( solveOrientation() );
		renderer.setAngle(getAngle());
		renderer.draw(batch);
		
		//reset to default color
		batch.setColor(Color.WHITE);
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
		if(partName.equalsIgnoreCase("head")){
			solveJoint(dt);
			joint_length = 0.3f; //Math.abs((float) Math.sin(lengthAngle)) * STRETCH_AMOUNT;
			child.solveJoints(dt);
		}
		else if(child != null){
			solveJoint(dt);
			lengthAngle += dt;
			joint_length = MIN_LENGTH + Math.abs((float) Math.sin(lengthAngle)) * STRETCH_AMOUNT;
			child.solveJoints(dt);
		}
	}
	
	public void update(float dt){
		//Update headings
		if (partName.equalsIgnoreCase("joint")){
			if(child != null)
				setHeading( child.getPosition().tmp().sub(getPosition()).nor() ); 
			else
				setHeading( getPosition().tmp().sub(parent.getPosition()).nor() );
			
			int spriteNumber = solveOrientation();
			
			setAngle(getHeading().angle() +45 + 180 - spriteNumber * 45);
		}else{
	
			//this assumes that head always has a child
			setAngle(child.getAngle()); 

		}
		
		
		
	}
	public void updateChild(float dt){
		
		if(child != null && child.partName.equalsIgnoreCase("Joint"))
			child.updateChild(dt);
		if(invMass  > 0)
			getPosition().add(getVelocity().tmp().mul(dt));
		getVelocity().mul(0);
	
	}
	
	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {

		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(Helper.worldToScreen(getPosition()));
		shapeRndr.begin(ShapeType.Rectangle);
		shapeRndr.rect(temp.x -0.1f,temp.y-0.1f, 0.2f ,0.2f);
		shapeRndr.end();
			
	}
	
	public boolean getDebug(){
		return false;
	}
	
	public void solveJoint(float dt){
		
		axis.set(child.getPosition().tmp().sub(getPosition()));
		float currentDistance 	= axis.len();
		Vector2 unitAxis 		= axis.nor();

		relativeVelocity.set(child.getVelocity().tmp().sub(getVelocity()));
		float relVelMagnitude 	= relativeVelocity.dot(unitAxis);
		float relativeDistance 	= (currentDistance - joint_length);
		
		if( relativeDistance > 0){
			float impulse 	= 0;
			float remove 	= relVelMagnitude + relativeDistance / dt;
			if(invMass == 0 && child.getInvMass() == 0)
				impulse 	= 0;
			else
				impulse 	= remove / (invMass + child.getInvMass());
			impulse = impulse * 0.9f;
			applyImpulse(unitAxis.mul(impulse));
		}
	}
	
	public void setOnSpearRange(boolean b){
		onSpearRange = b;
	}
	
	public void setBlinking(boolean b){
		blinking = b;
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
		invMass = 0;	
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
	public void moveToPoint(Vector2 point) {
		setHeading(point.tmp().sub(getPosition()));
		
		if(child  != null){
			child.setForce(getForce());
			child.setHeading(getForce());
			worm.setHead(child);
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
	public void applyPull(Vector2 point,float magnitude) {
		Vector2 pullVector = point.tmp().sub(getPosition());
		pullVector.nor().mul(magnitude);
		getVelocity().add(pullVector); 
		if(getPosition().dst(point) < 0.15f)
			moveToPoint(point);
	}
	
	public boolean isBlinking(){
		return blinking;
	}
	
	@Override
	public boolean isAffected(Vector2 point, float radius) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RigidBodyBox getCollider() {
		return body;
	}
}
