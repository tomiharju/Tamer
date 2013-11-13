package com.me.tamer.gameobjects.tamer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.tiles.Prop;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.utils.RuntimeObjectFactory;

public class Spear extends DynamicObject {

	private Environment environment;
	private SoundManager sound;

	private Creature targetCreature = null;
	private Creature creature = null;
	private boolean attached = false;
	private Vector2 targetPoint = new Vector2();

	private boolean justDropped = false;

	private Vector2 direction = new Vector2();
	private final float SPEED = 25.0f;
	private Prop hitbox = new Prop();

	public Spear() {
		setGraphics();
		sound = SoundManager.instance();

		// hitbox
		hitbox.setGraphics("vRocks1.png");
		hitbox.setHitBox("1");
		hitbox.setZindex(-10);

	}

	public void setGraphics() {
		Renderer render = RenderPool.addRendererToPool("animated", "spear");
		render.loadGraphics("spear", 1, 8);
		setSize(new Vector2(2.4f, 1.5f));
		setRenderType("spear");

	}

	public void update(float dt) {

		// check that tamer has moved enough from spear when it has been dropped
		// and spear has reached ground
		if (justDropped
				&& attached
				&& environment.getTamer().getShadow().getPosition()
						.dst(getPosition()) > 1) {
			justDropped = false;
		}

		if (!attached) {
			// adjust direction when worm moves
			direction.set(targetPoint.tmp().sub(getPosition()));
			direction.nor();
			getPosition().add(direction.tmp().mul(SPEED * dt));
			//getPosition().add(direction.x * (SPEED * dt),
				//	direction.y * (SPEED * dt));

			if (getPosition().dst(targetPoint) < 0.5f) {
				if (targetCreature != null) {

					targetCreature.spearHit(this);
					sound.setVolume(0.3f);

					Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
							+ " :: playing sound HIT");
					sound.play(TamerSound.HIT);

				}
				setPosition(targetPoint);
				attached = true;

				// create hitbox
				hitbox.setup(environment);
				hitbox.setZindex(-1);
				hitbox.setPosition(getPosition());
				hitbox.markAsActive();
				environment.addNewObject(hitbox);
			}

		}
	}

	public void wakeUp(Environment environment) {
		this.environment = environment;
		attached = false;
		setzIndex(-1);
		markAsActive();
		throwSpear();
	}

	public void throwSpear() {
		setPosition(environment.getTamer().getPosition());
		boolean targetFound = false;

		ArrayList<Creature> creatures = environment.getCreatures();
		int size = creatures.size();
		for (int i = 0; i < size; i++) {
			// Check if there is creature inside the shadow
			creature = creatures.get(i).affectedCreature(
					environment.getTamer().getShadow().getPosition(),
					environment.getTamer().getShadow().getSize().x / 2);
			if (creature != null) {
				if (targetCreature == null) {
					targetFound = true;
					targetCreature = creature;
					targetPoint = ((DynamicObject) targetCreature)
							.getPosition();
				} else if (((DynamicObject) creature).getPosition().dst(
						environment.getTamer().getShadow().getPosition()) < ((DynamicObject) targetCreature)
						.getPosition().dst(
								environment.getTamer().getShadow()
										.getPosition())) {
					targetCreature = creature;
					targetPoint = ((DynamicObject) targetCreature)
							.getPosition();
				}
			}
		}

		if (!targetFound) {
			targetPoint = new Vector2();
			targetPoint.set(environment.getTamer().getShadow().getPosition());
			// Subtract size of the shadow in world to get centerpoint
			targetPoint.x -= 0.5;
			targetPoint.y += 0.5;
			targetPoint.x = (float) Math.floor(targetPoint.x) + 0.5f;
			targetPoint.y = (float) Math.floor(targetPoint.y) + 0.5f;
		}

		setHeading(targetPoint.tmp().sub(getPosition()));
		direction.set(targetPoint.tmp().sub(getPosition()));
		direction.nor();

		justDropped = true;

		//TamerStage.addDebugLine(new Vector2(0, 0), targetPoint);
	}

	/**
	 * Add this spear back to the pool of spears Remove from active gameobjects
	 */
	public void pickUp() {
		attached = false;
		setzIndex(-1); // not sure if needed here

		if (targetCreature != null) {
			targetCreature.unBind();
			targetCreature = null;
		}

		// remove hitbox
		hitbox.markAsCarbage();

		RuntimeObjectFactory.addToObjectPool("spear", this);
		markAsCarbage();
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName()
				+ " :: Spear picked up ");
	}

	public boolean isAttached() {
		return attached;
	}

	public boolean isJustDropped() {
		return justDropped;
	}
}
