package com.me.tamer.gameobjects.tamer;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.utils.RuntimeObjectFactory;


public class Tamer extends DynamicObject{
	
	private final float SPEED 	= 0.4f;
	private int numSpears 		= 3;
	private ArrayList<Spear> spears = null;
	private TamerShadow shadow;
	private Environment environment;

	public void setup(){
		
	}
	
	public void wakeUp(Environment environment){
		//Spears
		spears = new ArrayList<Spear>();
		for( int i = 0 ; i < numSpears ; i++){
			RuntimeObjectFactory.addToObjectPool("spear", new Spear());
		}
		//Scream
		RuntimeObjectFactory.addToObjectPool("scream", new GryphonScream());
		
		//Shadow
		shadow = new TamerShadow(this);
		environment.addObject(shadow);
		
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: Tamer has woken up! " + this.toString());
		//Z-index for drawing order
		setZindex(-1);
		setGraphics("tamer");
		setForce("0:0");
		setMass("10");
		setRigidBody("circle");
		this.environment = environment;
		this.environment.setTamer(this);
	}
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("animated",graphics);
		render.loadGraphics(graphics, 1, 8);
		setSize("4:2.7");
		renderType = graphics;
	}
	
	@Override
	public void update(float dt){
		solveOrientation();
		getPosition().add(force);
		force.mul(0f);
		for(int i = 0 ; i < spears.size() ; i ++){
			if(getPosition().dst(spears.get(i).getPosition()) < 1 ){
				if(spears.get(i).isAttached()){
					spears.get(i).pickUp();
					spears.remove(i);
				}
			}
		}
	}
	
	/**
	 * @param direction
	 * Joystick uses this method to move tamer around
	 */
	public void manouver(Vector2 direction){
		direction.rotate(45);
		float lenght = direction.len();
		float power = Math.max(Math.abs(direction.y), Math.min(Math.abs(lenght),0.5f));
		direction.nor().mul(power*SPEED);
		System.out.println("power  " + power);
		if(power > 0.1){
			heading.set(direction);
			heading.nor();
			force.set(direction);
			
		}
	}
	/**
	 * @param direction
	 * Used only to turn tamer around his position when throwing a spear
	 */
	public void turn(Vector2 direction){
		heading.lerp(direction, 0.02f);
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
	
	public TamerShadow getShadow(){
		return shadow;
	}
}
