package com.me.tamer.gameobjects.tamer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.RuntimeObjectFactory;

public class Tamer extends DynamicObject {

	private float SPEED = 0f;
	private final float FLYING_HEIGHT = 7.0f;
	private final float AIM_SPEED = 0.001f; // heading interpolating coefficient
	private final float BORDER_OFFSET = -5.0f;
	private TamerShadow shadow = null;
	private GryphonScream scream = null;
	private Environment environment;
	private Vector2 help = new Vector2();
	private Vector2 movementAxis = new Vector2();

	// spear related variables
	private int numSpears = 30;
	private ArrayList<Spear> spears = null;

	// Variables for entering the field
	private Vector2 spawnPosition = new Vector2();
	private Vector2 spawnDirection = new Vector2();
	private Vector2 isoPosition = new Vector2();
	private Vector2 mapBounds = new Vector2();
	private boolean enteredField = false;

	private final float DISTANCE_BOUNDS = 5.0f;
	private final float MIN_SPAWN_DISTANCE = 5.0f;
	private final float SPAWN_SPEED = 5.0f;

	private SoundManager sound;

	public Tamer() {
		// Spears
		spears = new ArrayList<Spear>();
		for (int i = 0; i < numSpears; i++) {
			RuntimeObjectFactory.addToObjectPool("spear", new Spear());
		}

		// Scream
		scream = new GryphonScream(this);

		// Shadow
		shadow = new TamerShadow(this);
		// Z-index for drawing order
		setZindex(-10);
		setGraphics("tamer");

		// sound
		sound = SoundManager.instance();

	}

	public void setup() {
		// NO-ACTION
	}

	public void wakeUp(Environment environment) {
		environment.setState(Environment.TAMER_ENTER);
		// Spears
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName()
				+ " :: Tamer has woken up! " + this.toString());
		this.environment = environment;
		this.environment.setTamer(this);

		// enter the field
		mapBounds.set(environment.getMapBounds());
		mapBounds.x -= DISTANCE_BOUNDS;
		mapBounds.y -= DISTANCE_BOUNDS;

		spawnPosition.set(shadow.getPosition());
	}

	public void setGraphics(String graphics) {
		Renderer render = RenderPool.addRendererToPool("animated", graphics);
		render.loadGraphics(graphics, 1, 8);
		setSize(5, 3.1f);
		setRenderType(graphics);
	}

	@Override
	public void update(float dt) {
		if (environment.getState() == Environment.TAMER_ENTER) {
			if (shadow.getPosition().dst(spawnPosition) > MIN_SPAWN_DISTANCE) {
				enteredField = true;
			}

			shadow.getPosition()
					.add(spawnDirection.tmp().mul(SPAWN_SPEED * dt));
			getPosition().set(shadow.getPosition().x - FLYING_HEIGHT,
					shadow.getPosition().y + FLYING_HEIGHT);
		} else {
			shadow.getPosition().add(getHeading().tmp().mul(SPEED * dt));
			getPosition().set(shadow.getPosition().x - FLYING_HEIGHT,
					shadow.getPosition().y + FLYING_HEIGHT);
			SPEED = 0;

			for (int i = 0; i < spears.size(); i++) {
				if (!spears.get(i).isJustDropped()
						&& shadow.getPosition()
								.dst(spears.get(i).getPosition()) < 1) {
					if (spears.get(i).isAttached()) {
						spears.get(i).pickUp();
						spears.remove(i);
					}
				}
			}
		}

		scream.update(dt);
	}

	@Override
	public void draw(SpriteBatch batch) {
		shadow.draw(batch);
		scream.draw(batch);
		super.draw(batch);
		
	}

	/**
	 * @param direction
	 *            Joystick uses this method to move tamer around
	 */
	public void manouver(Vector2 direction) {
		direction.set(checkBounds(direction));

		direction.rotate(45);
		float power = direction.len();
		SPEED = 0.2f * power;

		if (power > 0.5) {
			setHeading(direction);
		}
	}

	public Vector2 checkBounds(Vector2 movement) {
		Vector2 mapBounds = environment.getMapBounds();
		help.set(shadow.getPosition());

		help.set(Helper.worldToScreen(help));
		help.add(movement.tmp().mul(Gdx.graphics.getDeltaTime()));

		if (help.x > mapBounds.x  
				|| help.x < -mapBounds.x ) {
			movementAxis.set(1, 0);
			movement.sub(Helper.projection(movement, movementAxis));
		}
		if (help.y > mapBounds.y 
				|| help.y < -mapBounds.y  ) {
			movementAxis.set(0, 1);
			movement.sub(Helper.projection(movement, movementAxis));
		}

		return movement;
	}

	/**
	 * @param direction
	 *            Used only to turn tamer around his position when throwing a
	 *            spear
	 */
	public void turn(Vector2 direction) {
		direction.rotate(45);
		getHeading().lerp(direction, AIM_SPEED);
		getHeading().nor();
	}

	public void throwSpear() {
		Spear spear = (Spear) RuntimeObjectFactory.getObjectFromPool("spear");
		if (spear != null) {
			spear.setPosition(getPosition());
			spears.add(spear);
		} else
			System.err.println("No spears remaining");

		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
				+ " :: playing throwing sound");
		sound.setVolume(0.8f);
		sound.play(TamerSound.THROW);

	}

	public void setSpawnDirection(Vector2 spawnDirection) {
		this.spawnDirection.set(spawnDirection);
	}

	public void useScream() {
		scream.activate();
	}

	public boolean hasEnteredField() {
		return enteredField;
	}

	public TamerShadow getShadow() {
		return shadow;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public Spear getActiveSpear() {
		for (int i = 0; i < spears.size(); i++) {
			if (!spears.get(i).isAttached())
				return spears.get(i);
		}
		return null;
	}
	
	
	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		shadow.debugDraw(shapeRndr);
		
		/*Vector2 temp = new Vector2();
		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(Helper.worldToScreen(getCenterPosition()));
		shapeRndr.begin(ShapeType.Rectangle);
		shapeRndr.rect(temp.x-0.25f, temp.y-0.25f, 0.5f, 0.5f);
		shapeRndr.end();
		
		 * shapeRndr.setColor(1, 1, 1, 1);
		 * temp.set(Helper.worldToScreen(getPosition()));
		 * shapeRndr.begin(ShapeType.Rectangle); shapeRndr.rect(temp.x
		 * -0.1f,temp.y-0.1f, 0.2f ,0.2f); shapeRndr.end();
		 */

	}

	public boolean getDebug() {
		return true;
	}
}
