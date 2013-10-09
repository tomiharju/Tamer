package com.me.tamer.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.utils.RuntimeObjectFactory;

public class Tamer extends DynamicObject{
	
	private final float SPEED = 0.1f;
	private Vector2 heading = new Vector2(0,0);
	private int numSpears = 3;
	private ArrayList<Spear> spears = null;
	
	public Tamer(){
		spears = new ArrayList<Spear>();
		for( int i = 0 ; i < numSpears ; i++){
			RuntimeObjectFactory.addToObjectPool("spear", new Spear());
		}
	}
	
	@Override
	public void update(float dt){
		position.add(force);
		force.mul(0f);
		for(int i = 0 ; i < spears.size() ; i ++){
			if(position.dst(spears.get(i).getPosition()) < 1 ){
				if(spears.get(i).isAttached()){
					spears.get(i).pickUp();
					spears.remove(i);
				}
			
			
		}
		}
		
	}
	
	
	
	
	/**
	 * @param direction
	 */
	public void manouver(Vector2 direction){
		force.set(direction.mul(SPEED));
		heading.set(force);
		heading.nor();
	
	}
	
	public void throwSpear(Spear spear,Vector2 point){
		spears.add(spear);
		spear.setPosition(position.tmp().add(heading.mul(1.5f)));
		spear.throwAt(point);
	}

	public Vector2 getHeading(){
		return heading;
	}
}
