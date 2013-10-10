package com.me.tamer.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;

public class Worm extends DynamicObject{
	private SpawnPoint spawn;

	int ordinal = 1;
	private ArrayList<WormPart> parts;
	private WormPart head = null;
	
	
	
	public Worm(SpawnPoint spawn){
		this.spawn = spawn;
		heading = new Vector2();
		parts = new ArrayList<WormPart>();
		
	}
	
	
	public void setup(){
	
		String pos = (int)spawn.getPosition().x + ":" + (int)spawn.getPosition().y;
		setPosition(pos);
		setVelocity(spawn.getSpawnVelocity());
		//Check if spawn is more wide than tall
		System.out.println("Worm position "+spawn.getSize().toString());
		if(spawn.getSize().x > spawn.getSize().y)
			position.set((float) (position.x + (-spawn.getSize().x + Math.random() * spawn.getSize().x)),position.y);
		if(spawn.getSize().y > spawn.getSize().x)
			position.set(position.x,(float) (position.y + (-spawn.getSize().y + Math.random() * spawn.getSize().y)));
		
		addPart("head",0,position,velocity);
		for(int i = 0 ; i < 3 ; i++)
			addPart("joint",i+1,position,velocity);
		connectPieces();
	}
	
	public void wakeUp(Level level){
		for(WormPart part : parts){
			level.addObject(part);
			level.getCreatures().add(part);
			level.addRigidBody(part.getRigidBody());
			
		}
		
	}


	public void addPart(String type, int ordinal,Vector2 pos, Vector2 vel){
		WormPart part = null;
		if(type.equalsIgnoreCase("head")){
			part = new WormPart();
			part.createHead(pos,vel,this);
			head = part;
		}else if(type.equalsIgnoreCase("joint")){
			part = new WormPart();
			part.createBodyPart(ordinal,pos,vel,this);
		}else throw new IllegalArgumentException("Wrong partname");
		
		parts.add(part);
		
	}
	
	public void connectPieces(){
		for(int i = 0 ; i < parts.size() ; i++){
			if( (i + 1) < parts.size()){
				parts.get( i + 1 ).attachToParent(parts.get(i));
				//TODO: Create a rigidbodyline between parts
			}else if( (i + 1 ) == parts.size() )
				parts.get(i).setAsTail();
			
		}
	}
	public void resolveForces(float dt){
		head.solveForces(dt);
	}
	
	public void update(float dt){
		head.updateChild(dt);
	
	}
	
	public void draw(SpriteBatch batch){
		//No action
	}

	public void setHead(WormPart part){
		head = part;
	}
	public void dispose(){
		parts = null;
		head = null;
	}
	
	public WormPart getBottom(){
		return parts.get(parts.size()-1);
	}
	
	public WormPart getHead(){
		return head;
	}
	
	public ArrayList<WormPart> getParts(){
		return parts;
	}
	
	
	
}
