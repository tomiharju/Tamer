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
import com.me.tamer.gameobjects.creatures.Worm;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.tiles.Prop;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.services.TextureManager.TamerTexture;
import com.me.tamer.utils.EventPool;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.RuntimeObjectFactory;
import com.me.tamer.utils.tEvent;

public class Spear extends DynamicObject {

	private final float ADJUST_TIME = 0.5f;

	private Environment environment;
	private Tamer tamer;

	private Creature targetCreature = null;
	private Creature creature = null;
	private Worm targetWorm = null;
	private boolean attached = false;

	private Vector2 targetPoint = new Vector2();

	private Vector2 direction = new Vector2();
	private final float SPEED = 25.0f;
	private Prop hitbox = new Prop();
	
	private Vector2 help = new Vector2();

	public Spear() {
		setGraphics(TamerTexture.SPEAR);

		// hitbox
		//hitbox.setGraphics("vRocks1.png");
		hitbox.setHitBox("1");
		hitbox.setZindex(-10);
	}

	public void setGraphics(TamerTexture graphics) {
		Renderer render = RenderPool.addRendererToPool("animated", graphics.name());
		render.loadGraphics(graphics, 1, 8);
		setSize(new Vector2(2.4f, 1.5f));
		setRenderType(graphics.name());
	}

	public void update(float dt) {
		
		if (!attached) {
			// adjust direction when worm moves
			direction.set(targetPoint.tmp().sub(getPosition()));
			direction.nor();
			getPosition().add(direction.tmp().mul(SPEED * dt));

			if (getPosition().dst(targetPoint) < 0.5f) {
				//not sure if really needed, but null pointer happened
				if (targetCreature != null) {
					targetCreature.spearHit(this);
					
				} else{
					playSound(TamerSound.SPEAR_GROUND);
				}
				setPosition(targetPoint);
				attached = true;

				// create hitbox
				environment.addNewObject(hitbox);
				hitbox.setZindex(-1);
				hitbox.setPosition(getPosition());
				hitbox.markAsActive();
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
		tamer = environment.getTamer();
		attached = false;
		setZindex(-1);
		
		throwSpear();
	}

	public void throwSpear() {
		
		markAsActive();
		setPosition(tamer.getPosition());
		boolean targetFound = false;

		ArrayList<Creature> creatures = environment.getCreatures();
		int size = creatures.size();
		for (int i = 0; i < size; i++) {
			// Check if there is creature inside the shadow	area
			creature = creatures.get(i).affectedCreature(
					tamer.getShadow().getPosition(),
					tamer.getShadow().getSize().x / 2);
			
			//remove target if it is worm that is being eaten or already bound or drowning
			if(creatures.get(i).getType() == Creature.TYPE_WORM){
				if(((Worm)creatures.get(i)).isBeingEaten())creature = null;
				else if (((Worm)creatures.get(i)).isBound())creature = null;
				else if (((Worm)creatures.get(i)).isDrowning())creature = null;
			}
			
			if (creature != null) {
				targetFound = true;
				switch (creature.getType()){
				case (Creature.TYPE_ANT):
					targetCreature = creature;
					if (((AntOrc)targetCreature).getTargetWorm() != null) targetWorm = ((AntOrc)targetCreature).getTargetWorm();
					targetPoint = ((DynamicObject) targetCreature).getPosition();
					break;
				case (Creature.TYPE_WORM):
					if (targetCreature == null) {
						//If ant is not on the aim try finding worm
						//Hit always the tail of the worm
						targetWorm = ((Worm)creatures.get(i));
						targetCreature = targetWorm.getParts().get( targetWorm.getParts().size() - 1);
						targetPoint = ((DynamicObject) targetCreature).getPosition();
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
			targetPoint.x = (float) Math.floor(targetPoint.x) + 1;
			targetPoint.y = (float) Math.floor(targetPoint.y);
		}

		setHeading(targetPoint.tmp().sub(getPosition()));
		direction.set(targetPoint.tmp().sub(getPosition()));
		direction.nor();
	}

	/**
	 * Add this spear back to the pool of spears Remove from active gameobjects
	 */
	public void pickUp() {
		attached = false;
		setZindex(-1); // not sure if needed here

		if (targetCreature != null && targetWorm != null) {
			//don't unbind if being eaten
			if(!targetWorm.isBeingEaten()){
				targetWorm.unBind();
			}
			
			targetWorm = null;
			targetCreature = null;
			creature = null;
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
}
