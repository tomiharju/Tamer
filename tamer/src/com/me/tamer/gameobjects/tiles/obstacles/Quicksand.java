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
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.utils.IsoHelper;

public class Quicksand extends StaticObject implements Obstacle{
	private ArrayList<SandPart> parts;
	private ArrayList<Creature> creatures_entered;
	private Vector2 bogHoleCenter;

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
		
		hud = Hud.instance();
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
		bogHoleCenter.div(parts.size());
		//Move center half tile size up so that its realy in the center of the tile
		bogHoleCenter.set(bogHoleCenter.x,bogHoleCenter.y);
	}
	
	public void resolve(ArrayList<Creature> creatures){
		int size = creatures.size();
		int psize = parts.size();
		for(int i = 0 ; i < size ; i ++){
			
			for(int k = 0; k < psize ; k ++){
				//Check each section of this quicksand if any creature has entered one of them ( 1 = sand radius )
				boolean entered = creatures.get(i).isAffected(parts.get(k).getCenterPosition(),1f);
				//If some creature is inside this cluster
				if(entered){
					//Check if creature is not already inside this cluster
					if(!creatures_entered.contains(creatures.get(i))){
						//Add creature to this cluster
						creatures_entered.add(creatures.get(i));
						
						alreadyDead = false;
						for (int j = 0; j < deadCreatures.size(); j++){
							if (creatures.get(i)==deadCreatures.get(j))alreadyDead = true;
						}
						if(!alreadyDead){
							Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
									+ " :: updating label remaining");
							hud.updateLabel("remaining", -1);
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
				isUnderRadius = targetCreature.isAffected(parts.get(k).getCenterPosition(),1f);
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
	
	
	
	public void draw(SpriteBatch batch){
		//Override to avoid default action
	}
	public void update(float dt){
		//Override to avoid default action
	}
}