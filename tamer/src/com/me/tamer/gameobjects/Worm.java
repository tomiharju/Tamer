package com.me.tamer.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;

public class Worm extends DynamicObject{
	private SpawnPoint spawn;
	private Level level;
	int ordinal = 1;
	private ArrayList<WormPart> parts;
	private WormPart head = null;
	
	
	
	public Worm(SpawnPoint spawn,Level level){
		this.spawn = spawn;
		heading = new Vector2();
		parts = new ArrayList<WormPart>();
		this.level = level;
	}
	
	
	public void setup(){
		String pos = (int)spawn.getPosition().x + ":" + (int)spawn.getPosition().y;
		setPosition(pos);
		setVelocity(spawn.getSpawnVelocity());
		addPart("head",0,position,velocity);
		for(int i = 0 ; i < 5 ; i++)
			addPart("joint",i+1,position,velocity);

		for(WormPart part : parts){
			level.addObject(part);
			level.addRigidBody(part.getRigidBody());
		}
		connectPieces();
	}


	public void addPart(String type, int ordinal,Vector2 pos, Vector2 vel){
		WormPart part = null;
		if(type.equalsIgnoreCase("head")){
			part = new WormPart();
			part.createHead(pos,vel);
			head = part;
		}else if(type.equalsIgnoreCase("joint")){
			part = new WormPart();
			part.createBodyPart(ordinal,pos,vel);
		}else throw new IllegalArgumentException("Wrong partname");
		
		parts.add(part);
		
	}
	
	public void connectPieces(){
		for(int i = 0 ; i < parts.size() ; i++){
			if( (i + 1) < parts.size()){
				parts.get( i + 1 ).attachToParent(parts.get(i));
				//TODO: Create a rigidbodyline between parts
			}
		}
	}
	public void resolveForces(float dt){
		head.solveForces(dt);
	}
	public void update(float dt){
		head.updateChild(dt);
		head.getVelocity().mul(0.99f);
	}
	public void draw(SpriteBatch batch){
		//override to avoid action.
	}
	
	
	
}
