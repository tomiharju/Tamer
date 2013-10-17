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
	
	public Worm(){
		parts = new ArrayList<WormPart>();
	}
	
	public void setVelocity(Vector2 vel){
		this.velocity = new Vector2(vel);
	}
	public void setPosition(Vector2 pos){
		this.position = new Vector2(pos);
	}
	
	
	
	public void setup(){
		//Check if spawn is more wide than tall
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
