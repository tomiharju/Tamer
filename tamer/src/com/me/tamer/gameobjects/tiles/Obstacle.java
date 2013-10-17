package com.me.tamer.gameobjects.tiles;


import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.physics.RigidBody;

public class Obstacle extends StaticObject {

	

	@Override
	public RigidBody getRigidBody(){
		return body;
	}

}
