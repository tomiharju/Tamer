package com.me.tamer.gameobjects.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.services.TextureManager.TamerTexture;
import com.me.tamer.utils.Helper;

public class WormPart extends DynamicObject implements Creature{
	
	private final float DECAY_SPEED = 0.5f;
	private final float MIN_LENGTH = 0.15f;
	private final float STRETCH_AMOUNT = 0.07f;
	private final float HEAD_POS_FIX = 0.005f;
	
	// Container worm
	private Worm worm = null;
	private boolean decaying = false;
	private float levelOfDecay = 1;
	private boolean attacked = false;
	
	private float joint_length = 0.4f;
	private float lengthAngle = 0;
	private int ordinal;
	private float invMass;
	private float mass;
	private Vector2 help = new Vector2();

	// Effect variables
	private boolean onSpearRange = false;
	private boolean blinking = false;

	// Chain related stuff
	private WormPart parent = null;
	private WormPart child = null;
	private boolean isTail = false;
	private int partType = 0;
	private final int TYPE_HEAD = 1;
	private final int TYPE_BODY = 2;

	// Physics optimization variables;
	Vector2 impulseA = new Vector2();
	Vector2 impulseB = new Vector2();
	Vector2 axis = new Vector2();
	Vector2 relativeVelocity = new Vector2();
	Vector2 orientationVector = new Vector2();
	Vector2 temp = new Vector2();

	public void createHead(Vector2 pos, Vector2 vel, Worm worm) {
		this.worm = worm;
		setGraphics(TamerTexture.WORMHEAD);
//		setGraphics("wormhead");
		partType = TYPE_HEAD;
		mass = 30;
		invMass = 1 / mass;
		setPosition(pos);
		setVelocity(vel);
		setForce(new Vector2(vel).mul(worm.getSPEED()));
		setHeading(vel);
		this.ordinal = 0;
	}

	public void createBodyPart(int ordinal, Vector2 pos, Vector2 vel, Worm worm) {
		this.worm = worm;
		setGraphics(TamerTexture.WORMPART);
//		setGraphics("vwormpart2");
		partType = TYPE_BODY;
		mass = 10;
		invMass = 1 / mass;
		setPosition(pos);
		getPosition().add(vel.tmp().nor().mul(-ordinal * joint_length));
		setVelocity(new Vector2(0, 0));
		setForce(new Vector2(0, 0));
		this.ordinal = ordinal;
	}

	public void setGraphics(TamerTexture graphics) {
		Renderer render = RenderPool.addRendererToPool("animated", graphics.name());
		render.loadGraphics(graphics, 1, 8);
		setSize(1, 1f);
		setRenderType(graphics.name());
	}

	@Override
	public void draw(SpriteBatch batch) {
		Renderer renderer = RenderPool.getRenderer(getRenderType());

		if (onSpearRange)
			batch.setColor(0.1f, 1, 0.1f, 1.0f);
		else if (blinking)
			batch.setColor(0.8f, 0.8f, 1.0f, 1.0f);
		if (decaying) {
			levelOfDecay -= DECAY_SPEED * Gdx.graphics.getDeltaTime();
			batch.setColor(1, 1, 1, levelOfDecay);
			if(levelOfDecay < 0)
				worm.removePart(this);
		}
		
		
		renderer.setSize(getSize());

		// Fix position of the headpart
		if (partType == TYPE_HEAD ) {
			help.set(Helper.worldToScreen(getPosition()));
			help.y += HEAD_POS_FIX;
			renderer.setPosition(help);
		} else
			renderer.setPosition(Helper.worldToScreen(getPosition()));

		renderer.setOrientation(solveOrientation());
		renderer.setAngle(getAngle());
		renderer.draw(batch);

		// reset to default color
		batch.setColor(Color.WHITE);
	}

	public void unBind() {
		invMass = 1 / mass;
	}

	public void bind() {
		invMass = 0;
	}

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
			joint_length = MIN_LENGTH + Math.abs((float) Math.sin(lengthAngle))
					* STRETCH_AMOUNT;
		}
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
		getVelocity().mul(0);

	}

	public void solveJoint(float dt) {
		if(child == null)return;
		
		axis.set(child.getPosition().tmp().sub(getPosition()));
		float currentDistance = axis.len();
		Vector2 unitAxis = axis.nor();

		relativeVelocity.set(child.getVelocity().tmp().sub(getVelocity()));
		float relVelMagnitude = relativeVelocity.dot(unitAxis);
		float relativeDistance = (currentDistance - joint_length);

		if (relativeDistance > 0) {
			float impulse = 0;
			float remove = relVelMagnitude + relativeDistance / dt;
			if (invMass == 0 && child.getInvMass() == 0)
				impulse = 0;
			else
				impulse = remove / (invMass + child.getInvMass());
			impulse = impulse * 0.9f;
			applyImpulse(unitAxis.mul(impulse));
		}
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
	
	public boolean isAttacked(){
		return attacked;
	}

	public void applyImpulse(Vector2 impulse) {
		Vector2 addA = impulse.tmp().mul(child.getInvMass());
		child.getVelocity().sub(addA);
		Vector2 addB = impulse.tmp().mul(invMass);
		getVelocity().add(addB);
	}

	public void setAsTail() {
		worm.setTail(this);
	}

	public boolean isTail() {
		return isTail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.me.tamer.gameobjects.superclasses.DynamicObject#dispose(com.me.tamer
	 * .gameobjects.Level)
	 */
	public void dispose(Environment environment) {
		// TODO: play some death animations before actually disposing?
		environment.getCreatures().remove(this);
		worm.getParts().remove(this);
	}

	/**
	 * When a spear hits the worm in the head, use this recursive function to
	 * remove all the body parts.
	 */
	

	public void decay() {
		decaying = true;
	}

	public boolean isBlinking() {
		return blinking;
	}


	@Override
	public void setup(Environment level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void wakeUp(Environment level) {
		// TODO Auto-generated method stub

	}

	public float getInvMass() {
		return invMass;
	}

	public int getOrdinal() {
		return ordinal;
	}


	
	public boolean isDecaying() {
		return decaying;
	}
	
	public float getLevelOfDecay(){
		return levelOfDecay;
	}
	
	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(Helper.worldToScreen(getPosition()));
		shapeRndr.begin(ShapeType.Rectangle);
		shapeRndr.rect(temp.x - getSize().x / 2, temp.y, getSize().x,getSize().y);
		shapeRndr.end();
	}

	public boolean getDebug() {
		return false;
	}

	public void spearHit(Spear spear) {
		worm.spearHit(spear);		
	}

	@Override
	public void lassoHit(String lasso) {
		// TODO Auto-generated method stub
		
	}

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

	@Override
	public int getType() {
		return Creature.TYPE_WORM;
	}

	@Override
	public void setGraphics(String graphics) {
		// TODO Auto-generated method stub
		
	}
}
