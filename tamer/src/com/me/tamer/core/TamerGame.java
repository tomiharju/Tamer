package com.me.tamer.core;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.utils.GameObjectFactory;


public class TamerGame extends Game{

	@Override
	public void create() {
		// TODO Auto-generated method stub
		setScreen(new PlayScreen(this));
		
		
	}

}




