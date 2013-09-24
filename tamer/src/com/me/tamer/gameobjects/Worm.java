package com.me.tamer.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;

public class Worm extends DynamicObject{
	private SpawnPoint spawn;

	
	
	public Worm(SpawnPoint spawn){
		this.spawn = spawn;
		heading = new Vector2();
	
	}
	
	public void update(float dt){
		position.add(velocity.cpy().mul(dt));
		velocity.set(heading);
	}
	
	public void setup(){
		setRender("static");
		setGraphics("tamer");
		setGraphicSize("1:1");
		String pos = (int)spawn.getPosition().x + ":" + (int)spawn.getPosition().y;
		setPosition(pos);
		setVelocity(spawn.getSpawnVelocity());
		setForce("0:0");
		setMass("10");
		setRigidBody("circle");
		heading.set(velocity);
	}
}
