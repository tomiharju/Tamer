package com.me.tamer.gameobjects;

import java.util.ArrayList;

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
		setRenderer("static:tamer");
	
		setSize("1:1");
		String pos = (int)spawn.getPosition().x + ":" + (int)spawn.getPosition().y;
		setPosition(pos);
		setVelocity(spawn.getSpawnVelocity());
		setForce("0:0");
		setMass("10");
		setRigidBody("circle");
		heading.set(getVelocity());
		addPart("head",0);
		for(int i = 0 ; i < 5 ; i++)
			addPart("joint",i+1);
	}


	public void addPart(String type, int ordinal){
		WormPart part = null;
		if(type.equalsIgnoreCase("head")){
			part = new WormPart();
			part.createHead();
			head = part;
		}else if(type.equalsIgnoreCase("joint")){
			part = new WormPart();
			part.createBodyPart();
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
	public void solveForces(float dt){
		head.solveForces(dt);
	}
	public void update(float dt){
	//	head.update(dt);
	//	head.getVelocity().mul(0.99f);
	}
	
	
	
}
