package com.me.tamer.gameobjects.creatures;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.Level.WormState;
import com.me.tamer.core.TamerStage;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.ui.ControlContainer;
import com.me.tamer.utils.DrawOrderComparator;

public class Worm extends DynamicObject implements Creature {

	private final int NUMBER_PARTS = 9;
	private final float FINAL_SPEED = 20.0f;
	private ArrayList<WormPart> parts;
	private float speed = FINAL_SPEED;
	private WormPart head = null;

	private ControlContainer controls;
	private DrawOrderComparator comparator;

	private boolean beingEaten 	= false;
	private boolean bound 			= false;
	private boolean insideFence 	= false;
	private boolean submerged 		= false;
	private boolean escaped		= false;
	private boolean drowning = false;
	private boolean dead = false;

	// for draw order
	ArrayList<GameObject> drawParts = new ArrayList<GameObject>();

	private Environment environment;
	private TamerStage stage;

	public Worm() {
		parts = new ArrayList<WormPart>();
		comparator = new DrawOrderComparator();
		controls = ControlContainer.instance();
		stage = TamerStage.instance();
		setZindex(-1);
	}

	public void wakeUp(Environment environment) {
		this.environment = environment;
		environment.getCreatures().add(this);
		addPart("head", 0, super.getPosition(), super.getVelocity());
		for (int i = 0; i < NUMBER_PARTS; i++)
			addPart("joint", i + 1, super.getPosition(), super.getVelocity());
		connectPieces();

		for (int i = parts.size() - 1; i >= 0; i--) {
			parts.get(i).setZindex(-1);
		}

		head = parts.get(0);

		// for drawing order
		// is there better way to do this?
		for (int i = 0; i < parts.size(); i++) {
			drawParts.add(((GameObject) parts.get(i)));
		}
	}

	public void addPart(String type, int ordinal, Vector2 pos, Vector2 vel) {
		WormPart part = null;
		if (type.equalsIgnoreCase("head")) {
			part = new WormPart();
			part.createHead(pos, vel, this);
		} else if (type.equalsIgnoreCase("joint")) {
			part = new WormPart();
			part.createBodyPart(ordinal, pos, vel, this);
		} 

		parts.add(part);
	}

	public void removePart(WormPart part) {
		parts.remove(part);
	}

	public void connectPieces() {
		for (int i = 0; i < parts.size(); i++) {
			if ((i + 1) < parts.size()) {
				parts.get(i + 1).attachToParent(parts.get(i));
			}
		}
	}

	public void update(float dt) {
		//reset escaped to true and check parts to prove otherwise
		escaped = true;
		
		for (int i = 0; i < parts.size(); i++){
			parts.get(i).solveJoints(dt);
			parts.get(i).update(dt);
			if( parts.get(i).getLevelOfDecay() > 0.1f ) escaped = false;
		}

		//Remove worm from gameobjects once it has fled
		if (escaped) markAsCarbage();
	
		// kill worm when head has decayed
		if (dead) markAsCarbage();

		head.getVelocity().set(head.getHeading().tmp().mul(speed) );
		solveEffects();
	}

	public void draw(SpriteBatch batch) {
		Collections.sort(drawParts, comparator);
		for (int i = 0; i < drawParts.size(); i++) {
			drawParts.get(i).draw(batch);
		}
	}

	public void doScreamEffect() {
		if (!head.isBlinking())
			for (int i = 0; i < parts.size(); i++) {
				parts.get(i).setBlinking(true);
			}
		else
			for (int i = 0; i < parts.size(); i++) {
				parts.get(i).setBlinking(false);
			}
	}

	public boolean isWithinRange(Vector2 point, float radius) {
		if (head.getPosition().dst(point) < radius)
			return true;
		return false;
	}

	public void decay() {
		if(drowning || beingEaten) stage.getLevel().setWormState(this, WormState.DEAD);
		else stage.getLevel().setWormState(this, WormState.FENCE);
		
		for (int i = 0; i < parts.size(); i++) {
			parts.get(i).decay();
		}
	}

	@Override
	public Creature affectedCreature(Vector2 point, float radius) {
		float mindist = radius;
		WormPart part = null;

		for (int i = 0; i < parts.size(); i++) {
			float dist = parts.get(i).getPosition().dst(point);
			if (dist < mindist) {
				mindist = dist;
				part = parts.get(i);
			}
		}
		return part;
	}

	@Override
	public void spearHit(Spear spear) {
		//this is double check. already checked on spear
		if (!submerged) {
			// nail worm to center of a tile
			getPosition().x = (float) Math.floor(getPosition().x) + 1;
			getPosition().y = (float) Math.floor(getPosition().y);
			bind();

			playSound(TamerSound.SPEAR_WORM);
		}
	}

	public void bind() {
		bound = true;
		disableCollision();
		parts.get(parts.size() - 1).setInvMass(0);
		//speed = 0;
	}

	@Override
	public void unBind() {
		bound = false;
		enableCollision();
		speed = FINAL_SPEED;
		parts.get(parts.size() - 1).setInvMass(
				1 / parts.get(parts.size() - 1).getMass());
	}

	public void solveEffects() {
		setOnSpearRange(false);
		for (int i = 0; i < parts.size(); i++) {
			if (parts
					.get(i)
					.getPosition()
					.dst(controls.getEnvironment().getTamer().getShadow()
							.getPosition()) < 1) {
				setOnSpearRange(true);
			}
		}
	}

	public void submerge() {
		stage.getLevel().setWormState(this, WormState.DEAD);
		
		for(int i = 0 ; i < parts.size() ; i ++)
			parts.get(i).decay();
		this.submerged = true;
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
	}

	@Override
	public void applyPull(Vector2 point, float magnitude) {
		// TODO Auto-generated method stub
		
	}

	public void dispose(){
		parts.clear();
		parts = null;
		head = null;
	}
	
	//---------------------------------------------
	//Setters
	//---------------------------------------------

	public void setOnSpearRange(boolean onRange) {
		if ( !submerged && !beingEaten && !bound){
			for (int i = 0; i < parts.size(); i++) {
				parts.get(i).setOnSpearRange(onRange);
			}
			// Tell tamer that worm is on range and give the tail part as parameter
			if (onRange)environment.getTamer().setCreatureOnSpearRange( parts.get(parts.size() - 1).getPosition());
		}
	}

	public void setHeading(Vector2 newHeading) {
		head.setHeading(newHeading);
	}

	public void setHead(WormPart part) {
		head = part;
	}

	public void setAttacked(boolean b) {
		for (int i = 0; i < parts.size(); i++)
			parts.get(i).setAttacked(b);
	}

	public void setBeingEaten(boolean b) {
		beingEaten = b;
	}

	public void setInsideFence(boolean b) {
		if (!insideFence && b) {
			insideFence = b;
			decay();
		} 
	}

	public void setDrowning(boolean drowning) {
		speed = 0 ;
		for(int i = 0 ; i < parts.size() ; i++)
			parts.get(i).setJointlength(0.01f);
		this.drowning = drowning;
	}

	//---------------------------------------------
	//Getters
	//---------------------------------------------
	
	public ArrayList<WormPart> getParts() {
		return parts;
	}
	
	@Override
	public int getType() {
		return Creature.TYPE_WORM;
	}

	public WormPart getTail() {
		return parts.get(parts.size() - 1);
	}

	public float getSpeed() {
		return speed;
	}

	public Vector2 getSize() {
		return head.getSize();
	}

	public WormPart getHead() {
		return head;
	}

	public Vector2 getPosition() {
		return head.getPosition();
	}

	public Vector2 getHeading() {
		return head.getHeading();
	}
	
	public TamerStage getStage(){
		return stage;
	}
	
	public boolean isBeingEaten() {
		return beingEaten;
	}

	public boolean isBound() {
		return bound;
	}

	public boolean isInsideFence() {
		return insideFence;
	}
	
	public boolean isDrowning() {
		return drowning;
	}

	public boolean isSubmerged() {
		return submerged;
	}

	@Override
	public boolean isDecaying() {
		// TODO Auto-generated method stub
		return false;
	}
}
