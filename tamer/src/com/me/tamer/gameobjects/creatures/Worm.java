package com.me.tamer.gameobjects.creatures;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.superclasses.Creature;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.gameobjects.tiles.SpawnPoint;

public class Worm extends DynamicObject implements Creature{
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
		//No action because this object is not ment to be on game right away
	}
	
	public void wakeUp(Environment environment){
		System.out.println("WORM WOKE UP!");
		addPart("head",0,position,velocity);
		for(int i = 0 ; i < 3 ; i++)
			addPart("joint",i+1,position,velocity);
		connectPieces();
		
		for(WormPart part : parts){
			environment.addObject(part);
			environment.getCreatures().add(part);
			environment.addRigidBody(part.getRigidBody());
			
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

	@Override
	public void spearHit(Spear spear) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unBind() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lassoHit(String lasso) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveToFinish() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
