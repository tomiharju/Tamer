package com.me.tamer.gameobjects.tamer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.AntOrc;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.tiles.Prop;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.utils.EventPool;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.RuntimeObjectFactory;
import com.me.tamer.utils.tEvent;

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
	
	private Vector2 help = new Vector2();

	public Spear() {
		setGraphics("spear");
		sound = SoundManager.instance();

		// hitbox
		hitbox.setGraphics("vRocks1.png");
		hitbox.setHitBox("1");
		hitbox.setZindex(-10);
	}

	public void setGraphics(String graphics) {
		Renderer render = RenderPool.addRendererToPool("animated", graphics);
		render.loadGraphics(graphics, 1, 8);
		setSize(new Vector2(2.4f, 1.5f));
		setRenderType(graphics);

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
	
	@Override
	public void draw(SpriteBatch batch) {
		//overrided to adjust spear draw position
		Renderer renderer = RenderPool.getRenderer(getRenderType());
		renderer.setSize(getSize());
		
		help.set(getPosition());
		help.x -= 0.5f;
		help.y += 0.5;
		help = Helper.worldToScreen(help);
		
		renderer.setPosition(help);
		renderer.setOrientation( solveOrientation() );
		renderer.draw(batch);	
	}

	public void wakeUp(Environment environment) {
		this.environment = environment;
		attached = false;
		setZindex(-1);
		
		throwSpear();
	}

	public void throwSpear() {
		
		markAsActive();
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
				targetFound = true;
				switch (creature.getType()){
				case (Creature.TYPE_ANT):
					targetCreature = creature;
					targetPoint = ((DynamicObject) targetCreature).getPosition();
					break;
				case (Creature.TYPE_WORMPART):
					if (targetCreature == null) {
						targetCreature = creature;
						targetPoint = ((DynamicObject) targetCreature).getPosition();
					} else if (((DynamicObject) creature).getPosition().dst(environment.getTamer().getShadow().getPosition()) < ((DynamicObject) targetCreature).getPosition().dst(environment.getTamer().getShadow().getPosition())) {
						targetCreature = creature;
						targetPoint = ((DynamicObject) targetCreature)
								.getPosition();
					}
					break;
				default:
					break;
				}	
			} 
		}
		//This had to be moved here, because if you throw a spear and if there are no creatures in the arraylist
		//The spear will be thrown onto (0,0)
		if(!targetFound){
			//if target is not found, aim to center of a tile
			targetPoint = new Vector2();
			targetPoint.set(environment.getTamer().getShadow().getPosition());
			// Subtract size of the shadow in world to get centerpoint
			targetPoint.x -= 0.5;
			targetPoint.y += 0.5;
			
			//set spear to hit center of the tile
			targetPoint.x = (float) Math.floor(targetPoint.x) + 1;//0.5f;
			targetPoint.y = (float) Math.floor(targetPoint.y);// + 0.5f;
		}

		setHeading(targetPoint.tmp().sub(getPosition()));
		direction.set(targetPoint.tmp().sub(getPosition()));
		direction.nor();
		
		//boolean to prevent tamer from picking up spear right after it has been dropped
		justDropped = true;

		//TamerStage.addDebugLine(new Vector2(0, 0), targetPoint);
	}

	/**
	 * Add this spear back to the pool of spears Remove from active gameobjects
	 */
	public void pickUp() {
		attached = false;
		setZindex(-1); // not sure if needed here

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

	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setup(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(Environment level) {
		// TODO Auto-generated method stub	
	}
}
