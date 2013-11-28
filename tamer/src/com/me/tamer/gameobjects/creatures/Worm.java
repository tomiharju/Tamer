package com.me.tamer.gameobjects.creatures;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.me.tamer.core.Level.WormState;
import com.me.tamer.core.TamerStage;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.services.TextureManager.TamerTexture;
import com.me.tamer.ui.ControlContainer;
import com.me.tamer.utils.DrawOrderComparator;

public class Worm extends DynamicObject implements Creature {

	private final int NUMBER_PARTS = 8;
	private ArrayList<WormPart> parts;
	private float SPEED = 7.0f;
	private WormPart head = null;
	private WormPart tail = null;

	// for effects
	private ControlContainer controls;

	// for draw order
	ArrayList<GameObject> gameobjects = new ArrayList<GameObject>();

	private boolean beingEaten = false;
	private boolean bound = false;
	private boolean insideFence = false;
	private boolean drowning = false;
	private boolean dead = false;

	// for effects
	private DrawOrderComparator comparator;

	// for draw order
	ArrayList<GameObject> drawParts = new ArrayList<GameObject>();

	private Environment environment;
	private TamerStage stage;

	public Worm() {
		parts = new ArrayList<WormPart>();
		comparator = new DrawOrderComparator();
		controls = ControlContainer.instance();
		stage = TamerStage.instance();
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
		tail = parts.get(parts.size() - 1);

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
		} else
			throw new IllegalArgumentException("Wrong partname");

		parts.add(part);
	}

	public void removePart(WormPart part) {
		parts.remove(part);
		if (parts.size() > 0)
			tail = parts.get(parts.size() - 1);
	}

	public void connectPieces() {
		for (int i = 0; i < parts.size(); i++) {
			if ((i + 1) < parts.size()) {
				parts.get(i + 1).attachToParent(parts.get(i));
			} else if ((i + 1) == parts.size())
				parts.get(i).setAsTail();
		}
	}

	public void update(float dt) {
		//reset dead to true and check parts to prove otherwise
		dead = true;
		
		for (int i = 0; i < parts.size(); i++){
			parts.get(i).solveJoints(dt);
			parts.get(i).update(dt);
			if( parts.get(i).getLevelOfDecay() > 0.1f ) dead = false;
		}

		// kill worm when head has decayed
		if (dead) markAsCarbage();
			
		head.getVelocity().set(head.getHeading().tmp().mul(SPEED));

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

	public void dispose() {
		parts = null;
		head = null;
	}

	public boolean isWithinRange(Vector2 point, float radius) {
		// for(int i = 0 ; i < parts.size() ; i ++){
		// if(parts.get(i).getPosition().dst(point) < radius)
		// return true;
		// }

		if (head.getPosition().dst(point) < radius)
			return true;
		return false;
	}

	@Override
	public void applyPull(Vector2 point, float magnitude) {
		Vector2 direction = point.tmp().sub(head.getPosition());

		if (point.dst(head.getPosition()) > 0.5f) {
			head.getVelocity().add(direction.mul(magnitude));
			head.setHeading(direction);
		}
	}

	@Override
	public void lassoHit(String lasso) {
		// TODO Auto-generated method stub

	}

	@Override
	public void kill() {
		
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
		if (!drowning) {
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

		SPEED = 0;
	}

	@Override
	public void unBind() {
		bound = false;
		enableCollision();
		SPEED = 5;
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

	public void setOnSpearRange(boolean onRange) {
		if ( !drowning && !beingEaten && !bound){
			for (int i = 0; i < parts.size(); i++) {
				parts.get(i).setOnSpearRange(onRange);
			}
			// Tell tamer that worm is on range and give the tail part as parameter
			if (onRange)environment.getTamer().setCreatureOnSpearRange( parts.get(parts.size() - 1).getPosition());
		}
	}

	public void setTail(WormPart part) {
		tail = part;
	}

	public void setHeading(Vector2 newHeading) {
		head.setHeading(newHeading);
	}

	public void setHead(WormPart part) {
		head = part;
	}

	@Override
	public void markAsCarbage() {
		System.out.println("drownin: " +drowning +"beingEta: " +beingEaten);
		if(drowning)stage.getLevel().setWormState(this, WormState.DEAD);
		else stage.getLevel().setWormState(this, WormState.FENCE);
		
		super.markAsCarbage();
	}

	public ArrayList<WormPart> getParts() {
		return parts;
	}

	public float getSPEED() {
		return SPEED;
	}

	@Override
	public int getType() {
		return Creature.TYPE_WORM;
	}

	public WormPart getTail() {
		return parts.get(parts.size() - 1);
	}

	public float getSpeed() {
		return SPEED;
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

	@Override
	public void decay() {
	}

	@Override
	public boolean isDecaying() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAttacked(boolean b) {
		for (int i = 0; i < parts.size(); i++)
			parts.get(i).setAttacked(b);
	}

	public void setBeingEaten(boolean b) {
		beingEaten = b;
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

	public void setInsideFence(boolean b) {
		if (!insideFence && b) {
			insideFence = b;
			
			for (int i = 0; i < parts.size(); i++) {
				parts.get(i).decay();
			}
			
		} else if (insideFence && !b) {
//			insideFence = b;
			//stage.getLevel().setWormState(this, WormState.DEFAULT);
		}

	}

	public boolean isDrowning() {
		return drowning;
	}

	public void setDrowning(boolean drowning) {
		this.drowning = drowning;
	}

	
}
