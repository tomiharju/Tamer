package com.me.tamer.gameobjects.tamer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.creatures.Worm;
import com.me.tamer.gameobjects.renderers.AnimatedRenderer;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.services.TextureManager.TamerAnimations;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.RuntimeObjectFactory;

public class Spear extends DynamicObject {

	private Environment environment;
	private Tamer tamer;
	
	private final float SPEED = 25.0f;
	
	private Creature targetCreature = null;
	private Creature creature = null;
	private Worm targetWorm = null;
	private boolean attached = false;

	private Vector2 targetPoint = new Vector2();
	private Vector2 direction = new Vector2();
	
	private Vector2 help = new Vector2();

	public Spear() {
		setGraphics(TamerAnimations.SPEAR);
	}

	public void wakeUp(Environment environment) {
		this.environment = environment;
		tamer = environment.getTamer();
		attached = false;
		setZindex(-1);
		markAsActive();
		throwSpear();
	}
	
	public void setGraphics(TamerAnimations graphics) {
		Renderer render = RenderPool.addRendererToPool("animated", graphics.getFileName());
		render.loadGraphics(graphics, 1, 8);
		setSize(new Vector2(2.4f, 1.5f));
		setRenderType(graphics.getFileName());
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
			}
		}
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		//overrided to adjust spear draw position
		AnimatedRenderer renderer = (AnimatedRenderer)RenderPool.getRenderer(getRenderType());
		renderer.setSize(getSize());
		
		help.set(getPosition());
		help.x -= 0.5f;
		help.y += 0.5;
		help = Helper.worldToScreen(help);
		
		renderer.setPosition(help);
		renderer.setOrientation( solveOrientation() );
		renderer.draw(batch);	
	}

	public void throwSpear() {
		setPosition(tamer.getPosition());
		ArrayList<Creature> creatures = environment.getCreatures();
		for (int i = 0; i < creatures.size(); i++) {
			// Check if there is creature inside the shadow	area
			creature = creatures.get(i).affectedCreature(
					tamer.getShadowPosition(),
					tamer.getShadow().getSize().x / 2);
			
			//remove target if it is worm that is being eaten or already bound or drowning
			if(creatures.get(i).getType() == Creature.TYPE_WORM){
				if(((Worm)creatures.get(i)).isBeingEaten())creature = null;
				else if (((Worm)creatures.get(i)).isBound())creature = null;
				else if (((Worm)creatures.get(i)).isSubmerged())creature = null;
			}
			
			//latter one is because dispose is not implemented
			if (creature != null && !creature.isDecaying()) {
				switch (creature.getType()){
				case (Creature.TYPE_ANT):
					targetCreature = creature;
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
		setHeading(targetPoint.tmp().sub(getPosition()));
		direction.set(targetPoint.tmp().sub(getPosition()));
		direction.nor();
	}

	public void pickUp() {
		attached = false;
		if (targetWorm != null)
			if(!targetWorm.isBeingEaten())targetWorm.unBind();
		
		targetWorm = null;
		targetCreature = null;
		creature = null;
		
		RuntimeObjectFactory.addToObjectPool("spear", this);
		markAsCarbage();
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName()
				+ " :: Spear picked up ");
	}

	public boolean isAttached() {
		return attached;
	}
}
