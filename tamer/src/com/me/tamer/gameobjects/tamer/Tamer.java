package com.me.tamer.gameobjects.tamer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.Environment.RunningState;
import com.me.tamer.gameobjects.creatures.WormPart;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.services.TextureManager.TamerAnimations;
import com.me.tamer.services.TextureManager.TamerStatic;
import com.me.tamer.ui.ControlContainer;
import com.me.tamer.utils.EventPool;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.RuntimeObjectFactory;
import com.me.tamer.utils.tEvent;

public class Tamer extends DynamicObject {
	
	//Higher level entities
	private Environment environment;
	private ControlContainer controls;
	
	//Finals
	public final static float FLYING_HEIGHT = 7.0f;
	private final float DISTANCE_BOUNDS = 5.0f;
	private final float SPAWN_SPEED = 5.0f;
	private final float SPEAR_COOL_DOWN = 0.5f;
	
	//Children
	private TamerShadow shadow = null;
	private GryphonScream scream = null;
	
	//Movement
	private float speed = 0f;
	private Vector2 mapBounds = new Vector2();

	//Spear
	private int spearAmount;
	private ArrayList<Spear> spears = null;
	private Spear spearToBePicked = null;
	private Vector2 targetIndicator = new Vector2();
	private boolean creatureOnSpearRange = false;
	private boolean throwSpearPossible = false;
	private boolean onSpearCoolDown = false;
	private boolean spearOnRange = false;

	//Enter the field
	private Vector2 spawnPosition = new Vector2();
	private Vector2 spawnDirection = new Vector2();
	
	//Help
	private Vector2 help = new Vector2();
	private Vector2 movementAxis = new Vector2();
	
	public Tamer() {
		scream = new GryphonScream(this);
		shadow = new TamerShadow(this);
		
		// Z-index for drawing order
		setZindex(-10);
		setGraphics(TamerAnimations.TAMER);
		
		controls = ControlContainer.instance();
	}
	
	public void wakeUp(Environment environment) {	
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName()
				+ " :: Tamer has woken up! " + this.toString());
		
		this.environment = environment;
		//this.environment.setTamer(this);

		// enter the field
		mapBounds.set(environment.getMapBounds());
		mapBounds.x -= DISTANCE_BOUNDS;
		mapBounds.y -= DISTANCE_BOUNDS;

		spawnPosition.set(shadow.getPosition());
		
		//Get amount of spears from the level
		spearAmount = environment.getStage().getLevel().getSpears();
				
		// Spears
		spears = new ArrayList<Spear>();
		for (int i = 0; i < spearAmount; i++) {
			RuntimeObjectFactory.addToObjectPool("spear", new Spear());
		}
		
		controls.addSpearsAvailable(spearAmount);
	}

	public void setGraphics(TamerAnimations graphics) {
		Renderer render = RenderPool.addRendererToPool("animated", graphics.getFileName());
		render.loadGraphics(graphics, 1, 8);
		setSize(5, 3.1f);
		setRenderType(graphics.getFileName());
		
		//create target indicator renderer
		Renderer render2 = RenderPool.addRendererToPool("static", TamerStatic.TARGET_INDICATOR.getFileName() );
		render2.loadGraphics(TamerStatic.TARGET_INDICATOR.getFileName());
		render2.setSize(Helper.TILESIZE);
	}

	@Override
	public void update(float dt) {
		if (environment.getState() == RunningState.TAMER_ENTER) enterField(dt);
		getPosition().add(getHeading().tmp().mul(speed * dt));
		speed = 0;
		resolvePossibleActions();
		
		scream.update(dt);
		shadow.update(dt);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		//Quick fix to issue that tamer is drawn before he is supposed to
		if (environment.getState() == RunningState.TAMER_ENTER || environment.getState() == RunningState.NORMAL){
			super.draw(batch);
			
			if (!spearOnRange && throwSpearPossible){
				Renderer renderer2 = RenderPool.getRenderer( TamerStatic.TARGET_INDICATOR.getFileName() );
				renderer2.setSize(Helper.TILESIZE);
				renderer2.setPosition(Helper.worldToScreen( targetIndicator ));
				batch.setColor(1, 1, 1, 0.5f);
				renderer2.draw(batch);	
				batch.setColor(Color.WHITE);
			}
			shadow.draw(batch);
			scream.draw(batch);
		}
	}

	//-------------------------------------------------------------------------
	//Actions
	//-------------------------------------------------------------------------
	
	public void manouver(Vector2 direction) {
		direction.set(checkBounds(direction));
		direction.rotate(45);
		float power = direction.len();
		speed = 0.2f * power;
		if (power > 0.5) setHeading(direction);
	}
	
	public void enterField(float dt){
		speed = SPAWN_SPEED;
	}
	
	public void throwSpear() {
		if(onSpearCoolDown) return;
		if (throwSpearPossible){
			Spear spear = (Spear) RuntimeObjectFactory.getObjectFromPool("spear");
			if (spear != null) {
				spear.setPosition(getPosition());
				spears.add(spear);
				
				onSpearCoolDown = true;
				controls.setSpearCooldown(true);
				EventPool.addEvent(new tEvent(this,"stopSpearCooldown",SPEAR_COOL_DOWN,1));
				
				controls.addSpearsAvailable(-1);
				playSound(TamerSound.TAMER_SPEAR);
			} else
				Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName()
						+ " :: no spears remaining");
		}
	}
	
	public void pickUpSpear(){
		controls.addSpearsAvailable(1);
		spearToBePicked.pickUp();
		spears.remove( spearToBePicked );
	}
	
	public void stopSpearCooldown(){
		onSpearCoolDown = false;
		controls.setSpearCooldown(false);
	}

	public void useScream() {
		scream.activate();
	}
	
	//-------------------------------------------------------------------------
	//Helpers
	//-------------------------------------------------------------------------
	
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
	
	public void resolvePossibleActions(){
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
		
		if (!creatureOnSpearRange) throwSpearPossible = false;
		else throwSpearPossible = true;
		
		//reset this after every loop
		creatureOnSpearRange = false;	
	}
	
	//-------------------------------------------------------------------------
	//Setters and getters
	//-------------------------------------------------------------------------
	
	public void setSpawnDirection(Vector2 spawnDirection) {
		this.spawnDirection.set(spawnDirection);
	}
	
	public void setCreatureOnSpearRange(Vector2 creaturePosition){
		if(creaturePosition!=null){
			creatureOnSpearRange=true;
			targetIndicator.set(creaturePosition);
		}	
	}
	
	public TamerShadow getShadow() {
		return shadow;
	}
	
	public Vector2 getShadowPosition(){
		return shadow.getPosition();
	}
	
	public Environment getEnvironment() {
		return environment;
	}
	
	//-------------------------------------------------------------------------
	//Debug
	//-------------------------------------------------------------------------

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
}
