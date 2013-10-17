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
		
		RuntimeObjectFactory.addToObjectPool("scream", new GryphonScream());
		
		//Z-index for drawing order
		setZindex(-1);
	}
	
	@Override
	public void update(float dt){
		solveOrientation();
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
		//heading.lerp(direction,0.025f);
		direction.rotate(45);
		heading.set(direction);
		heading.nor();
		force.set(heading);
		force.mul(SPEED);
	}
	/**
	 * @param direction
	 * Used only to turn tamer around his position
	 */
	public void turn(Vector2 direction){
		heading.lerp(direction,0.03f);
	
		heading.nor();
	}
	
	public void throwSpear(Spear spear,Vector2 point,float power){
		spears.add(spear);
		spear.setPosition(position.tmp().add(heading.mul(1.5f)));
		spear.throwAt(point, power);
	}
	
	public void useScream(GryphonScream scream){
		scream.activate();
	}

	public Vector2 getHeading(){
		return heading;

	}
}
