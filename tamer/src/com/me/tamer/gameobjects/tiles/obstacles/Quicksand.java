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
import com.me.tamer.gameobjects.renders.EffectRenderer;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.utils.Helper;

public class Quicksand extends StaticObject implements Obstacle{
	private ArrayList<SandPart> parts;
	private ArrayList<Creature> creatures_entered;
	private Vector2 bogHoleCenter;
	private Vector2 temp = new Vector2();
	private EffectRenderer effectRenderer;
	
	private final float PULL_MAGNITUDE = 8;
	private boolean activated;
	
	private ArrayList<Creature> deadCreatures = new ArrayList<Creature>();
	private boolean alreadyDead = false;
	private Hud hud;
	
	public Quicksand(){
		parts 			= new ArrayList<SandPart>();
		creatures_entered = new ArrayList<Creature>();
		bogHoleCenter 	= new Vector2();
		activated		= false;
		loadEffectGraphics();
		hud = Hud.instance();
	}

	public void loadEffectGraphics(){
		Renderer render = RenderPool.addRendererToPool("effect", "bubbles");
		render.loadEffect("bubbles",5,3,true,0.2f);
		render.setSize(Helper.TILESIZE);
	
		setRenderType("bubbles");
	}
	public void setup(Environment environment){
		environment.addNewObject(this);
		environment.addObstacle(this);
		setBogHole();
	}
	
	public void addSandPart(SandPart p){
		parts.add(p);
	}
	
	public void setBogHole(){
		for(SandPart s : parts)
			bogHoleCenter.add(s.getCenterPosition());
			//bogHoleCenter.add(Helper.screenToWorld(s.getScreenTileCenter()));

			
		bogHoleCenter.div(parts.size());
		bogHoleCenter.set(bogHoleCenter.x - Helper.TILESIZE.x / 4,bogHoleCenter.y + Helper.TILESIZE.y / 2);
	}
	public void draw(SpriteBatch batch){
		Renderer renderer = RenderPool.getRenderer(getRenderType());
		for(int i = 0 ; i < parts.size() ; i ++){
			renderer.setPosition(Helper.worldToScreen(parts.get(i).getPosition()));
			((EffectRenderer) renderer).setAnimSeed( parts.get(i).stepAnimState());
			renderer.draw(batch);
		}
	}
	
	
	public void resolve(ArrayList<Creature> creatures){
		int size = creatures.size();
		int psize = parts.size();
		for(int i = 0 ; i < size ; i ++){
			
			for(int k = 0; k < psize ; k ++){
				//Check each section of this quicksand if any creature has entered one of them ( 0.5f = sand radius )
				boolean entered = creatures.get(i).isAffected(parts.get(k).getPosition(),0.5f);
				//If some creature is inside this cluster
				if(entered){
					//Check if creature is not already inside this cluster
					if(!creatures_entered.contains(creatures.get(i))){
						//Add creature to this cluster
						creatures_entered.add(creatures.get(i));
						
						alreadyDead = false;
						for (int j = 0; j < deadCreatures.size(); j++){
							if (creatures.get(i) == deadCreatures.get(j))alreadyDead = true;
						}
						if(!alreadyDead){
							Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
									+ " :: updating label remaining");
							hud.updateLabel(Hud.LABEL_REMAINING, -1);
						}
						
						//Do a coinflip
						/*int head = (int) Math.round(Math.random());
						if(head == 1)*/
							activated = true;
						
					}
				}
			}
		}
		//Check if one or more entered creatures have left the cluster
	
		for(int i = 0 ; i < creatures_entered.size() ; i ++){
			Creature targetCreature = creatures_entered.get(i);
			boolean isUnderRadius = false;
			for(int k = 0; k < psize ; k ++){
				//Check if this creature is closer than 1 from any of this clusters parts
				isUnderRadius = targetCreature.isAffected(parts.get(k).getPosition(),0.5f);
				if(isUnderRadius)
					break;
			}
			if(!isUnderRadius){
				creatures_entered.remove(targetCreature);

			}	
		}
	
		if(activated){
			size = creatures_entered.size();
			//Start pulling all the worms that are within the cluster
			for(int i = 0 ; i < size ; i ++){
				creatures_entered.get(i).applyPull(bogHoleCenter,PULL_MAGNITUDE);
			}		
		}
	}
	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		
		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(Helper.worldToScreen(bogHoleCenter));
		shapeRndr.begin(ShapeType.Circle);
		shapeRndr.circle(temp.x, temp.y, 0.1f,30);
		shapeRndr.end();
	}
	
	public boolean getDebug(){
		return false;
	}
	
	public void update(float dt){
		//Override to avoid default action
	}
}