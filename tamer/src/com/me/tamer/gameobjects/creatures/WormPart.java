package com.me.tamer.gameobjects.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renderers.AnimatedRenderer;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.services.TextureManager.TamerAnimations;
import com.me.tamer.utils.Helper;

public class WormPart extends DynamicObject implements Creature{
	//Finals
	private  float DECAY_SPEED = 1f;
	private  float MIN_LENGTH = 0.09f;
	private  float STRETCH_AMOUNT = 0.08f;
	
	// Container worm
	private Worm worm = null;
	
	//Decay variables
	private boolean decaying = false;
	private float levelOfDecay = 1;
	private boolean attacked = false;
	
	// Effect variables
	private boolean onSpearRange = false;
	private boolean blinking = false;

	// Chain related
	private WormPart parent = null;
	private WormPart child = null;
	private int partType = 0;
	private final int TYPE_HEAD = 1;
	private final int TYPE_BODY = 2;
	private float joint_length = 0.3f;
	private float lengthAngle = 0;
	private float invMass;
	private float mass;

	// Physics optimization variables;
	Vector2 impulseA = new Vector2();
	Vector2 impulseB = new Vector2();
	Vector2 axis = new Vector2();
	Vector2 relativeVelocity = new Vector2();
	Vector2 orientationVector = new Vector2();
	Vector2 temp = new Vector2();
	
	@Override
	public void draw(SpriteBatch batch) {
		AnimatedRenderer renderer = (AnimatedRenderer)RenderPool.getRenderer(getRenderType());
		renderer.setSize(getSize());

		if (onSpearRange)
			batch.setColor(0.7f, 1, 0.7f, 1.0f);
		else if (blinking)
			batch.setColor(0.7f, 0.7f, 1.0f, 1.0f);
		
		if (decaying) {
			levelOfDecay -= DECAY_SPEED * Gdx.graphics.getDeltaTime();
			batch.setColor(1, 1, 1, levelOfDecay);
			if(levelOfDecay < 0.1f)
				levelOfDecay = 0.1f;
		}
		
		renderer.setPosition(Helper.worldToScreen(getPosition()));
		renderer.setOrientation(solveOrientation());
		renderer.setAngle(getAngle());
		renderer.draw(batch);

		// reset to default color
		batch.setColor(Color.WHITE);
	}
	
	public void update(float dt) {
		// Update headings
		// And iterate positions
		if (partType == TYPE_BODY) {
			if (child != null)
				setHeading(child.getPosition().tmp().sub(getPosition()).nor());
			else if(parent != null)
				setHeading(getPosition().tmp().sub(parent.getPosition()).nor());

			int spriteNumber = solveOrientation();

			// solve difference between sprite angle and heading angle
			setAngle(getHeading().angle() + 46f + 180 - spriteNumber * 45);
	
		} 
		getPosition().add(getVelocity().tmp().mul(dt));
		getVelocity().mul(0.5f);
	}
	
	//---------------------------------
	//initialization
	//---------------------------------
	
	public void createHead(Vector2 pos, Vector2 vel, Worm worm) {
		this.worm = worm;
		setGraphics(TamerAnimations.WORMHEAD);
		partType = TYPE_HEAD;
		mass = 10;
		invMass = 1 / mass;
		setPosition(pos);
		setVelocity(vel);
		setHeading(vel);
	}

	public void createBodyPart(int ordinal, Vector2 pos, Vector2 vel, Worm worm) {
		this.worm = worm;
		setGraphics(TamerAnimations.WORMPART);
		partType = TYPE_BODY;
		mass = 10;
		invMass = 1 / mass;
		setPosition(pos);
		getPosition().add(vel.tmp().nor().mul(-ordinal * joint_length));
		setVelocity(new Vector2(0, 0));
		setHeading(vel);
		setForce(new Vector2(0, 0));
	}

	public void setGraphics(TamerAnimations graphics) {
		Renderer render = RenderPool.addRendererToPool("animated", graphics.getFileName());
		render.loadGraphics(graphics, 1, 8);
		setSize(1, 1f);
		setRenderType(graphics.getFileName());
	}
	
	//---------------------------------
	//WormPart
	//---------------------------------

	public void attachToParent(WormPart parent) {
		this.parent = parent;
		parent.attachToChild(this);
	}

	public void attachToChild(WormPart child) {
		this.child = child;
	}

	public void solveJoints(float dt) {
		if (partType == TYPE_HEAD) {
			solveJoint(dt);
			joint_length = 0.3f;
		} else if (partType == TYPE_BODY) {
			solveJoint(dt);
			lengthAngle += dt;
			joint_length = MIN_LENGTH;

			joint_length = MIN_LENGTH + Math.abs((float) Math.sin(lengthAngle))
					* STRETCH_AMOUNT;
		}
	}

	public void solveJoint(float dt) {
		if(child == null)return;
		
		axis.set(child.getPosition().tmp().sub(getPosition()));
		float currentDistance = axis.len();
		Vector2 unitAxis = axis.nor();

		relativeVelocity.set(child.getVelocity().tmp().sub(getVelocity()));
		float relVelMagnitude = relativeVelocity.dot(unitAxis);
		float relativeDistance = (currentDistance - joint_length);

			float impulse = 0;
			float remove = relVelMagnitude + relativeDistance / dt;
			impulse = remove / (invMass + child.getInvMass());
			applyImpulse(unitAxis.mul(impulse));
	}

	public void applyImpulse(Vector2 impulse) {
		Vector2 addA = impulse.tmp().mul(child.getInvMass());
		child.getVelocity().sub(addA);
		Vector2 addB = impulse.tmp().mul(invMass);
		getVelocity().add(addB);
	}
	
	public void dispose(Environment environment) {
		environment.getCreatures().remove(this);
		worm.getParts().remove(this);
	}

	public void decay() {
		decaying = true;
	}

	//-----------------------------------------
	//Setters and getters
	//-----------------------------------------
	
	public void setJointlength(float len){
		MIN_LENGTH = len;
		STRETCH_AMOUNT = len;
	}
	
	public void setOnSpearRange(boolean b) {
		onSpearRange = b;
	}

	public void setBlinking(boolean b) {
		blinking = b;
	}
	
	public void setAttacked(boolean b){
		attacked = b;
	}
	
	public void setInvMass(float invMass) {
		this.invMass = invMass;
	}
	
	@Override
	public int getType() {
		return Creature.TYPE_WORM;
	}
	
	public float getLevelOfDecay(){
		return levelOfDecay;
	}
	
	public float getInvMass() {
		return invMass;
	}

	public float getMass() {
		return mass;
	}

	@Override
	public float getSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public boolean isBlinking() {
		return blinking;
	}

	public boolean isDecaying() {
		return decaying;
	}
	
	public boolean isAttacked(){
		return attacked;
	}
	//-----------------------------------------
	//Creature implementation
	//-----------------------------------------
	
	@Override
	public void kill() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyPull(Vector2 point, float magnitude) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Creature affectedCreature(Vector2 poitn, float radius) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void spearHit(Spear spear) {
		worm.spearHit(spear);		
	}
	
	public void unBind() {
		invMass = 1 / mass;
	}

	public void bind() {
		invMass = 0;
	}
	
	//-----------------------------------------
	//Debug
	//-----------------------------------------
	
	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(Helper.worldToScreen(getPosition()));
		shapeRndr.begin(ShapeType.Rectangle);
		shapeRndr.rect(temp.x - getSize().x / 2, temp.y, getSize().x,getSize().y);
		shapeRndr.end();
	}
	
}
