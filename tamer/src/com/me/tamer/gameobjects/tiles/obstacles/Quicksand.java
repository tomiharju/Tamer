package com.me.tamer.gameobjects.tiles.obstacles;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.Hud;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.creatures.Worm;
import com.me.tamer.gameobjects.renderers.EffectRenderer;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.utils.Helper;

public class Quicksand extends StaticObject implements Obstacle {
	private ArrayList<SandPart> parts;
	private ArrayList<Creature> submerged_creatures;
	private Vector2 bogHoleCenter;
	private Vector2 temp = new Vector2();
	private EffectRenderer effectRenderer;

	private Hud hud;

	public Quicksand() {
		parts = new ArrayList<SandPart>();
		submerged_creatures = new ArrayList<Creature>();
		bogHoleCenter = new Vector2();
		// loadEffectGraphics();
		hud = Hud.instance();
	}

	public void loadEffectGraphics() {
		Renderer render = RenderPool.addRendererToPool("effect", "bubbles");
		render.loadEffect("bubbles", 20, 1, true, 0.2f);
		render.setSize(Helper.TILESIZE.x, Helper.TILESIZE.y * 2);

		setRenderType("bubbles");
	}

	public void setup(Environment environment) {
		environment.addObstacle(this);
	}

	public void addSandPart(SandPart p) {
		parts.add(p);
	}

	public void resolve(ArrayList<Creature> creatures) {
		int psize = parts.size();
		for (int k = 0; k < psize; k++) {
			for (int i = 0; i < creatures.size(); i++) {
				if(((DynamicObject) creatures.get(i)).getPosition().dst(parts.get(k).getPosition()) > 2)
					continue;
			if(submerged_creatures.contains(creatures.get(i)))
				continue;

				// Check if some wormhead is inside swamp
				if (creatures.get(i).getType() == Creature.TYPE_WORM && !((Worm) creatures.get(i)).isBound()) {
					Worm targetWorm = (Worm) creatures.get(i);
					if (targetWorm.getHead().isWithinRange(
							parts.get(k).getPosition(), 0.5f)) {
						// Set worm heading towards swamp, and prepare to dive.
						Vector2 heading = parts.get(k).getPosition().tmp()
								.sub(targetWorm.getHead().getPosition());
						targetWorm.setHeading(heading);
						targetWorm.submerge();
						// Remove worm from creatures, since its no longer in
						// game, yet it needs to be drawn and updated through
						// gameobjects.
						submerged_creatures.add(targetWorm);
					}

				}
			}
			
//<<<<<<< HEAD
//				for(int k = 0; k < psize ; k ++){
//					if (creatures_entered.get(i).getType() == Creature.TYPE_WORM){
//						Worm casultyWorm = (Worm) creatures_entered.get(i);
//					
//						if(casultyWorm.getHead().isWithinRange(parts.get(k).getPosition(),0.5f) && !casultyWorm.isDrowning()){
//							//flag worm to be drowning, so it is not affected by stuff in the game anymore
//							casultyWorm.setDrowning(true);
//							
//							casultyWorm.applyPull(parts.get(k).getPosition(),PULL_MAGNITUDE);
//						}
//							
//						for(int j = 0 ; j < casultyWorm.getParts().size() ; j ++){
//							if(casultyWorm.getParts().get(j).isWithinRange(parts.get(k).getPosition(),0.5f))
//								casultyWorm.getParts().get(j).decay();
//						}
//					}
//				}
//			}		
//=======

//>>>>>>> 1fba1efddb076442e19d15731d53516028cbe832
		}
		creatures.removeAll(submerged_creatures);
		submerged_creatures.clear();

	}

	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(Helper.worldToScreen(bogHoleCenter));
		shapeRndr.begin(ShapeType.Circle);
		shapeRndr.circle(temp.x, temp.y, 0.1f, 30);
		shapeRndr.end();
	}

	public boolean getDebug() {
		return false;
	}
}