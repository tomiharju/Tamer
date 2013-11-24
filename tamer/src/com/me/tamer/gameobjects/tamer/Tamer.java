package com.me.tamer.gameobjects.tamer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.Environment.RunningState;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.TextureManager.TamerTexture;
import com.me.tamer.ui.ControlContainer;
import com.me.tamer.utils.EventPool;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.RuntimeObjectFactory;
import com.me.tamer.utils.tEvent;

public class Tamer extends DynamicObject {

	private float SPEED = 0f;
	private final float FLYING_HEIGHT = 7.0f;
	private final float AIM_SPEED = 0.001f; // heading interpolating coefficient

	private final float DISTANCE_BOUNDS = 5.0f;
	private final float SPAWN_DISTANCE = 8.0f;
	private final float SPAWN_SPEED = 5.0f;
	private final float SPEAR_COOL_DOWN = 0.5f;
	
	private boolean onSpearCoolDown = false;
	private boolean spearOnRange = false;

	private TamerShadow shadow = null;
	private GryphonScream scream = null;
	private Environment environment;
	private Vector2 help = new Vector2();
	private Vector2 movementAxis = new Vector2();

	// spear related variables
	private int numSpears = 30;
	private ArrayList<Spear> spears = null;
	private Spear spearToBePicked = null;

	// Variables for entering the field
	private Vector2 spawnPosition = new Vector2();
	private Vector2 spawnDirection = new Vector2();
	private Vector2 isoPosition = new Vector2();
	private Vector2 mapBounds = new Vector2();
	private boolean enteredField = false;

	private SoundManager sound;
	private ControlContainer controls;

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
		setGraphics(TamerTexture.TAMER);

		// sound
		sound = SoundManager.instance();
		controls = ControlContainer.instance();
	}
	

	public void wakeUp(Environment environment) {		
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

	public void setGraphics(TamerTexture graphics) {
		Renderer render = RenderPool.addRendererToPool("animated", graphics.name());
		render.loadGraphics(graphics, 1, 8);
		setSize(5, 3.1f);
		setRenderType(graphics.name());
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		if (environment.getState() == RunningState.TAMER_ENTER) {
			if (shadow.getPosition().dst(spawnPosition) > SPAWN_DISTANCE) {
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

			spearToBePicked = null;
			spearOnRange = false;
			for (int i = 0; i < spears.size(); i++) {
				if (shadow.getPosition().dst(spears.get(i).getPosition()) < 1) {
					if (spears.get(i).isAttached()) {
						spearToBePicked = spears.get(i);
						spearOnRange = true;
					}
				}
			}
			
			controls.setSpearOnRange( spearOnRange );
			scream.update(dt);
		}	
			
//			for (int i = 0; i < spears.size(); i++) {
//				if (!spears.get(i).isJustDropped()
//						&& shadow.getPosition()
//								.dst(spears.get(i).getPosition()) < 1) {
//					if (spears.get(i).isAttached()) {
//						spears.get(i).pickUp();
//						spears.remove(i);
//					}
//				}
//			}
//		}
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		shadow.draw(batch);
		scream.draw(batch);
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
		help.set(shadow.getCenterPosition());
		
		help.set(Helper.worldToScreen(help));

		if (help.x > mapBounds.x) {
			float distance = help.x - mapBounds.x;
			movementAxis.set(-1, 0);
			movementAxis.rotate(45);
			shadow.getPosition().add(movementAxis.mul(distance));

		}
		if (help.x < -mapBounds.x) {
			float distance = help.x - -mapBounds.x;
			movementAxis.set(-1, 0);
			movementAxis.rotate(45);
			shadow.getPosition().add(movementAxis.mul(distance));
		}

		if (help.y > mapBounds.y) {
			float distance = help.y - mapBounds.y;
			movementAxis.set(0, -1);
			movementAxis.rotate(45);
			shadow.getPosition().add(movementAxis.mul(distance));
		}
		if (help.y < -mapBounds.y) {
			float distance = help.y - -mapBounds.y;
			movementAxis.set(0, -1);
			movementAxis.rotate(45);
			shadow.getPosition().add(movementAxis.mul(distance));
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
		if(onSpearCoolDown) return;
		
		Spear spear = (Spear) RuntimeObjectFactory.getObjectFromPool("spear");
		if (spear != null) {
			spear.setPosition(getPosition());
			spears.add(spear);
			
			//Cool down
			onSpearCoolDown = true;
			controls.setSpearCooldown(true);
			EventPool.addEvent(new tEvent(this,"enable",SPEAR_COOL_DOWN,1));
		} else
			System.err.println("No spears remaining");
		
		/*
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
				+ " :: playing throwing sound");
		sound.setVolume(0.8f);
		sound.play(TamerSound.THROW);*/

	}
	
	public void pickUpSpear(){
		spearToBePicked.pickUp();
		spears.remove( spearToBePicked );
	}
	
	public void enable(){
		onSpearCoolDown = false;
		controls.setSpearCooldown(false);
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

		Vector2 temp = new Vector2();
		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(environment.getMapBounds());
		shapeRndr.begin(ShapeType.Rectangle);
		shapeRndr.rect(-temp.x, -temp.y, temp.x * 2, temp.y * 2);
		shapeRndr.end();

		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(environment.getMapBounds());
		shapeRndr.begin(ShapeType.Rectangle);
		shapeRndr.rect(-0.25f, -0.25f, .5f, .5f);
		shapeRndr.end();

	}

	public boolean getDebug() {
		return false;
	}

	@Override
	public void setup(Environment level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose(Environment level) {
		// TODO Auto-generated method stub

	}


	@Override
	public void setGraphics(String graphics) {
		// TODO Auto-generated method stub
		
	}
}
