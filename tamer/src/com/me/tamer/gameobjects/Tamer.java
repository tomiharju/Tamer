package com.me.tamer.gameobjects;


import java.util.ArrayList;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.utils.RuntimeObjectFactory;


public class Tamer extends DynamicObject{
	
	private final float SPEED = 1.0f;
	private GryphonScream scream 			= null;
	private Vector2 heading = new Vector2(0,0);
	private int numSpears = 3;
	private ArrayList<Spear> spears = null;

		
	
	public Tamer(){
		spears = new ArrayList<Spear>();
		for( int i = 0 ; i < numSpears ; i++){
			RuntimeObjectFactory.addToObjectPool("spear", new Spear());
		}
		
		scream = new GryphonScream(this);
	}
	
	@Override
	public void update(float dt){
		position.add(force);
		force.mul(0.9f);
		
		for(int i = 0 ; i < spears.size() ; i ++){
			if(position.dst(spears.get(i).getPosition()) < 0.5 ){
				spears.get(i).pickUp();
				spears.remove(i);
				break;
			
			}
		}
		
	}
	
	/**
	 * @param direction
	 */
	public void manouver(Vector2 direction){
		direction.rotate(-45);
		force.set(direction.mul(SPEED));
		heading.set(force);
		heading.nor();
	}
	
	public void throwSpear(Spear spear,Vector2 point){
		spears.add(spear);
		spear.setPosition(position.tmp().add(heading.mul(1)));
		spear.throwAt(point);
	}
	
	public GryphonScream getScream(){
		return scream;
	}

	public Vector2 getHeading(){
		return heading;

	}
}
